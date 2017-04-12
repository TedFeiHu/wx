package com.wuxinaedu.weixin.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.adapter.SubscribeAdapter;
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

public class SubscribeActivity extends BaseActivity {
	@Override
	protected int getContentView() {
		return R.layout.activity_subscribe;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleName("订阅号");

		new MyTask().execute();
	}

	private void init(final List<Contacts> contacts) {
		// 请求结果出错处理
		if (contacts.size() == 0) {
			toastMessage("请求联系人出错，请稍后重试");
			return;
		}
		ListView listView = (ListView) findViewById(R.id.lv_id);
		listView.setAdapter(new SubscribeAdapter(this, contacts));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Toast.makeText(getActivity(),
				// contacts.get(position).getName(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(SubscribeActivity.this, SubscribeDetailsActivity.class);
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
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			int status = jsonObject.getInt("status");
			if (status != 0) { // 返回结果错误
				return list;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("subscribe");
			for (int i = 0; i < jsonArray.length(); i++) {
				Contacts contacts = new Contacts();

				JSONObject jsonObj = jsonArray.getJSONObject(i);

				contacts.setName(jsonObj.getString("name"));
				contacts.setWeCode(jsonObj.getInt("weCode"));
				contacts.setLastStr(jsonObj.getString("lastStr"));
				contacts.setNewsNum(jsonObj.getInt("newsNum"));
				String time = jsonObj.getString("lastTime");
				Date date = DateUtil.parseStringDate(DateUtil.DATE_SDF, time);
				contacts.setLastTime(date);
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
			String jsonStr = GetJsonString.getJsonString(SubscribeActivity.this, "subscribe.js");
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
