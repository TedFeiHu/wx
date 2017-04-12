package com.wuxinaedu.weixin.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.adapter.AddFriendsAdapter;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.DateUtil;
import com.wuxinaedu.weixin.utils.GetJsonString;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AddFriendsActivity extends BaseActivity {

	@Override
	protected int getContentView() {
		return R.layout.activity_add_friend;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleName(R.string.new_friend);
		showRightTv(R.string.addnewfriend,null);
		
		new MyTask().execute();
		
	}
	
	/**
	 * 解析数据初始化视图
	 * @param str
	 */
	private void init(final List<Contacts> list){
		ListView listView = (ListView) findViewById(R.id.lv_id);
		View view = getLayoutInflater().inflate(R.layout.item_add_friends_head, null);
		listView.addHeaderView(view);
		
		listView.setAdapter(new AddFriendsAdapter(this,list));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(AddFriendsActivity.this, DetailsActivity.class);
				intent.putExtra(Constant.GET_SERIALIZABLE, list.get(position-1));
				startActivity(intent);
			}
		});
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
				list.add(contacts);			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	class MyTask extends AsyncTask<Void,Void,List<Contacts>>{

		@Override
		protected List<Contacts> doInBackground(Void... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String jsonStr = GetJsonString.getJsonString(AddFriendsActivity.this, "addFriends.js");
			return parserJson(jsonStr);
		}

		@Override
		protected void onPreExecute() {
			showProDialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<Contacts> result) {
			super.onPostExecute(result);
			dismissProDialog();
			init(result);
		}
	}
	
}
