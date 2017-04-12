package com.wuxinaedu.weixin.activity.fragment;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.AddFriendsActivity;
import com.wuxinaedu.weixin.activity.DetailsActivity;
import com.wuxinaedu.weixin.activity.GroupActivity;
import com.wuxinaedu.weixin.activity.MainActivity;
import com.wuxinaedu.weixin.activity.SubscribeActivity;
import com.wuxinaedu.weixin.activity.adapter.ContactsAdapter;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.CoreUtil;
import com.wuxinaedu.weixin.utils.DateUtil;
import com.wuxinaedu.weixin.utils.FileLocalCache;
import com.wuxinaedu.weixin.utils.GetJsonString;
import com.wuxinaedu.weixin.utils.PingYinUtil;
import com.wuxinaedu.weixin.widget.ClearEditText;
import com.wuxinaedu.weixin.widget.SideBar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContactsFragment extends Fragment implements OnClickListener{
	
	private View view;
	private List<Contacts> contacts;
	private boolean isFirstViVisible = true;
	private MainActivity main;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			main = ((MainActivity)getActivity());
			view = inflater.inflate(R.layout.fragment_contact, null);
		}
		return view ;
	}
	
	/**
	 * 用户可见时调用 isVisibleToUser= true
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		//首次可见
		if(isVisibleToUser && isFirstViVisible){
			isFirstViVisible = false;
			new MyTask().execute();
        }
	}
	
	class MyTask extends AsyncTask<Void,Void,List<Contacts>>{

		@Override
		protected List<Contacts> doInBackground(Void... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String jsonStr = GetJsonString.getJsonString(getActivity(), "contacts.js");
			return parserJson(jsonStr);
		}

		@Override
		protected void onPreExecute() {
			main.showProDialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<Contacts> result) {
			super.onPostExecute(result);
			main.dismissProDialog();
			contacts = result;
			refreshContacts();
		}
	}
	
	/**
	 * 解析 contacts json
	 * @param jsonStr
	 * @return
	 */
	private List<Contacts> parserJson(String jsonStr){
		List<Contacts> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			int status = jsonObject.getInt("status");
			if(status != 0){ //返回结果错误
				return list;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("contacts");
			for (int i = 0; i < jsonArray.length(); i++) {
				Contacts contacts = new Contacts();
				
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				
				contacts.setName(jsonObj.getString("name"));
				contacts.setArea(jsonObj.getString("area"));
				contacts.setWeCode(jsonObj.getInt("weCode"));
				contacts.setHead(jsonObj.getString("head"));
				contacts.setAutograph(jsonObj.getString("autograph"));
				contacts.setLastStr(jsonObj.getString("lastStr"));
				contacts.setNewsNum(jsonObj.getInt("newsNum"));
				contacts.setAdd(jsonObj.getBoolean("isAdd"));
				String time = jsonObj.getString("lastTime");
				Date date = DateUtil.parseStringDate(DateUtil.DATE_SDF, time);
				contacts.setLastTime(date);
				JSONArray jsonArr = jsonObj.getJSONArray("images");
				List<String> list2 = new ArrayList<>();
				for (int j = 0; j < jsonArr.length(); j++) {
					list2.add(jsonArr.getString(j));
				}
				contacts.setImages(list2);
				
				//最后    对名字进行一次解析
				String name = contacts.getName();
				String str = PingYinUtil.getPingYin(name).toUpperCase();
				contacts.setNamePinyin(str);
				String first = str.substring(0, 1);
				//如果名字首字母不为 字母 统称为 #
				if(CoreUtil.isEnglish(first)){
					contacts.setNameFirst(first);
				}else {
					contacts.setNameFirst("#");
				}
				contacts.setNameFirstPinyin(PingYinUtil.converterToFirstSpell(name).toUpperCase());
				
				list.add(contacts);			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 数据解析及设置
	 */
	private void refreshContacts(){
		/**
		 * 获取本地对象 判断是否登录过（是否为null），如果登陆过直接获取本地对象。没有，请求网络
		 * TODO 请求网络 获取用户信息   将对象保存到本地
		 */
		view.setVisibility(View.VISIBLE);
		
		
		//请求结果出错处理
		if(contacts.size() == 0){
			Toast.makeText(getActivity(), "请求联系人出错，请稍后重试", Toast.LENGTH_SHORT).show();
			return;
		}
		//保存联系人信息
		FileLocalCache.setSerializableData(getActivity(),contacts,Constant.GET_CONTACTS);
		
		final ListView listView = (ListView) view.findViewById(R.id.lv_id);
		
		ContactsAdapter adapter = new ContactsAdapter(getActivity(),contacts);
		listView.setAdapter(adapter);
		
		//添加头部 并设置头部各个组件的监听
		final View v = getActivity().getLayoutInflater().inflate(R.layout.item_contacts_head_view, null);
		listView.addHeaderView(v,null,false);
		
		//头部三个选项监听
		v.findViewById(R.id.new_friend_id).setOnClickListener(this);
		v.findViewById(R.id.group_id).setOnClickListener(this);
		v.findViewById(R.id.public_id).setOnClickListener(this);
		//添加输入文字改变的监听器
		ClearEditText clearEdit = (ClearEditText) v.findViewById(R.id.ed_id);
		clearEdit.addTextChangedListener(new MyTextWatcher(adapter,v));
		
		//TODO 
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Contacts contact = (Contacts) listView.getAdapter().getItem(position);
				Intent intent = new Intent(getActivity(), DetailsActivity.class);
				intent.putExtra(Constant.GET_SERIALIZABLE, contact);
				startActivity(intent);
			}
		});
		
		//将自定义的SideBar与 联系人列表的listview关联起来，实现联动  以及关联中间提示字母TextView
		SideBar sideBar = (SideBar) view.findViewById(R.id.sb_id);
		TextView t = (TextView) view.findViewById(R.id.tv_id);
		sideBar.setListView(listView);
		sideBar.setTextView(t);
	}
	
	class MyTextWatcher implements TextWatcher{
		private List<Contacts> searchList  = new ArrayList<>();
		private List<Contacts> searchSubList  = new ArrayList<>();
		private ContactsAdapter adapter;
		private View headBody;
		public MyTextWatcher(ContactsAdapter adapter,View v) {
			this.adapter =adapter;
			headBody =  v.findViewById(R.id.head_view_id);
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			//获得搜索框输入的数字
			String input = s.toString();
			//是否文中文
			boolean isChinese = CoreUtil.isChinese(input);
			
			//如果为空
			if(CoreUtil.isEmpty(input)){
				adapter.setList(contacts);
				headBody.setVisibility(View.VISIBLE);
				return;
			}
			//TODO 把其他三项 gone掉 
			headBody.setVisibility(View.GONE);
			//每次数据改变  清空集合
			searchList.clear();
			searchSubList.clear();
			//遍历集合 匹配数据
			for (Contacts contacts2 : contacts) {
				
				if(isChinese){ //如果为中文    匹配以此文字开头的数据
					String name = contacts2.getName();
					if(name.startsWith(input)){  //如果为指定开头  加入到集合中
						searchList.add(contacts2);
					}else if (name.indexOf(input) != -1) {//如果包含该文字  加入到另个集合中
						searchSubList.add(contacts2);
					}
				}else {
					input = input.toUpperCase();
					String namePy = contacts2.getNamePinyin();
					String namePyFirst = contacts2.getNameFirstPinyin();
					if(namePy.startsWith(input)){//如果为指定开头  加入到集合中
						searchList.add(contacts2);
					}else if(namePyFirst.startsWith(input)){ //如果包含该文字  加入到另个集合中
						searchSubList.add(contacts2);
					}
				}
			}
			//将以搜索文字 开头的数据   和包含搜索文字的数据集合到一起
			if(searchSubList.size() != 0){
				searchList.addAll(searchSubList);
			}
			adapter.setList(searchList);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.new_friend_id:  // 跳转到添加朋友界面
			startActivity(new Intent(getActivity(),AddFriendsActivity.class));
			break;
		case R.id.group_id:  //点击群聊
			Intent intent = new Intent(getActivity(),GroupActivity.class);
			startActivity(intent);
			break;
		case R.id.public_id:
			intent = new Intent(getActivity(),SubscribeActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
