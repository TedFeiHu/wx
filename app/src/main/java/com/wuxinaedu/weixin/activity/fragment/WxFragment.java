package com.wuxinaedu.weixin.activity.fragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.ChatActivity;
import com.wuxinaedu.weixin.activity.MainActivity;
import com.wuxinaedu.weixin.activity.SubscribeActivity;
import com.wuxinaedu.weixin.activity.adapter.WeAdapter;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.CoreUtil;
import com.wuxinaedu.weixin.utils.DateUtil;
import com.wuxinaedu.weixin.utils.GetJsonString;
import com.wuxinaedu.weixin.utils.L;
import com.wuxinaedu.weixin.utils.PingYinUtil;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class WxFragment extends Fragment {
	private boolean isFirstViVisible = true;
	private View view;
	private MainActivity main;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_wx, null);
			main = ((MainActivity) getActivity());
		}
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		L.i("----onstart -----");
		if (isFirstViVisible) {
			isFirstViVisible = false;
			new MyTask().execute();
		}
	}

	private void init(final List<Contacts> contacts) {

		// 请求结果出错处理
		if (contacts.size() == 0) {
			Toast.makeText(getActivity(), "请求联系人出错，请稍后重试", Toast.LENGTH_SHORT).show();
			return;
		}
		ListView listView = (ListView) view.findViewById(R.id.lv_id);
		listView.setAdapter(new WeAdapter(getActivity(), contacts));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Toast.makeText(getActivity(),
				// contacts.get(position).getName(), Toast.LENGTH_SHORT).show();
				// 订阅号详情
				if (position == 0) {
					Intent intent = new Intent(getActivity(), SubscribeActivity.class);
					startActivity(intent);
					return;
				}
				Intent intent = new Intent(getActivity(), ChatActivity.class);
				intent.putExtra(Constant.GET_SERIALIZABLE, contacts.get(position));
				startActivity(intent);
			}
		});
	}

	/**
	 * 解析 contacts json
	 * 
	 * @param jsonStr
	 * @return
	 */
	private List<Contacts> parserJson(String jsonStr) {
		List<Contacts> list = new ArrayList<>();

		// 模拟出订阅号的设置
		Contacts contacts2 = new Contacts();
		contacts2.setName("订阅号");
		contacts2.setLastStr("[重大新闻]来无限互联学习，月薪过万so easy！");
		contacts2.setNewsNum(1);
		list.add(contacts2);
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			int status = jsonObject.getInt("status");
			if (status != 0) { // 返回结果错误
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

				// 最后 对名字进行一次解析
				String name = contacts.getName();
				String str = PingYinUtil.getPingYin(name).toUpperCase();
				contacts.setNamePinyin(str);
				String first = str.substring(0, 1);
				// 如果名字首字母不为 字母 统称为 #
				if (CoreUtil.isEnglish(first)) {
					contacts.setNameFirst(first);
				} else {
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

	class MyTask extends AsyncTask<Void, Void, List<Contacts>> {

		@Override
		protected List<Contacts> doInBackground(Void... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String str = GetJsonString.getJsonString(main, "wechat.js");
			return parserJson(str);
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
			init(result);
		}
	}
}
