package com.wuxinaedu.weixin.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.adapter.SubscribeDetailsAdapter;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.bean.SubscribeDetails;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.DateUtil;
import com.wuxinaedu.weixin.utils.GetJsonString;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class SubscribeDetailsActivity extends BaseActivity {
	@Override
	protected int getContentView() {
		return R.layout.activity_subscribe_details;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		Contacts contacts = (Contacts) intent.getSerializableExtra(Constant.GET_SERIALIZABLE);
		if(contacts == null){
			return;
		}
		//设置标题和右侧图片
		setTitleName(contacts.getName());
	}
	
	private void init(List<SubscribeDetails> result){
		ListView listView = (ListView) findViewById(R.id.lv_id);
		listView.setAdapter(new SubscribeDetailsAdapter(this, result));
	}
	
	/**
	 * 解析 contacts json
	 * @param jsonStr
	 * @return
	 */
	private List<SubscribeDetails> parserJson(String jsonStr){
		List<SubscribeDetails> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			int status = jsonObject.getInt("status");
			if(status != 0){ //返回结果错误
				return list;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("subscribeDetails");
			for (int i = 0; i < jsonArray.length(); i++) {
				SubscribeDetails contacts = new SubscribeDetails();
				
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				contacts.setTitle(jsonObj.getString("title"));
				contacts.setImage(jsonObj.getString("image"));
				contacts.setContent(jsonObj.getString("content"));
				String time = jsonObj.getString("time");
				Date date = DateUtil.parseStringDate(DateUtil.DATE_SDF, time);
				contacts.setDate(date);
				list.add(contacts);			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	class MyTask extends AsyncTask<Void,Void,List<SubscribeDetails>>{

		@Override
		protected List<SubscribeDetails> doInBackground(Void... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String jsonStr = GetJsonString.getJsonString(SubscribeDetailsActivity.this, "subscribeDetails.js");
			return parserJson(jsonStr);
		}

		@Override
		protected void onPreExecute() {
			showProDialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<SubscribeDetails> result) {
			super.onPostExecute(result);
			dismissProDialog();
			init(result);
		}
	}
	

}
