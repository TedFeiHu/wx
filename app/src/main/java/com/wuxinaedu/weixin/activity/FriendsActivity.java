package com.wuxinaedu.weixin.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.adapter.FriendsAdapter;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.bean.FriendsCircle;
import com.wuxinaedu.weixin.bean.UserInfor;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.DateUtil;
import com.wuxinaedu.weixin.utils.GetJsonString;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends BaseActivity {
	private ListView listView;
	private String backGround;

	@Override
	protected int getContentView() {
		return R.layout.activity_friends;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleName(R.string.friends);
		showRightIv(R.drawable.icon_talk, null);
		// 请求数据
		new MyTask().execute();
	}

	class MyTask extends AsyncTask<Void, Void, List<FriendsCircle>> {

		@Override
		protected List<FriendsCircle> doInBackground(Void... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String jsonStr = GetJsonString.getJsonString(FriendsActivity.this, "userFriendsCircle.js");
			return parserJson(jsonStr);
		}

		@Override
		protected void onPreExecute() {
			showProDialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<FriendsCircle> result) {
			super.onPostExecute(result);
			dismissProDialog();
			init(result);
		}
	}

	/**
	 * 初始化
	 * 
	 * @param jsonStr
	 */
	private void init(List<FriendsCircle> list) {
		 listView = (ListView) findViewById(R.id.lv_id);
		 if (list != null) {
		 listView.setAdapter(new FriendsAdapter(this,list));
		 }else { // 请求救过出错处理
		 toastMessage("请求结果出错，请重试。。。");
		 return;
		 }

		View v = getLayoutInflater().inflate(R.layout.item_friends_head_view, null);

		// TODO 修改背景 头像 用户名
		UserInfor userInfor = (UserInfor) getIntent().getSerializableExtra(Constant.USER_INFOR);

		final ImageView head = (ImageView) v.findViewById(R.id.head_id);
		final ImageView back = (ImageView) v.findViewById(R.id.back_image_id);
		final TextView name = (TextView) v.findViewById(R.id.user_name);
		name.setText(userInfor.getUserName());

		x.image().loadDrawable(userInfor.getHead(), ImageOptions.DEFAULT, new CommonCallback<Drawable>() {
			@Override
			public void onSuccess(Drawable arg0) {
				// 设置头像
				head.setImageDrawable(arg0);
			}

			@Override
			public void onFinished() {
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
			}

			@Override
			public void onCancelled(CancelledException arg0) {
			}
		});

		x.image().loadDrawable(backGround, ImageOptions.DEFAULT, new CommonCallback<Drawable>() {
			@Override
			public void onSuccess(Drawable arg0) {
				// 设置背景
				back.setImageDrawable(arg0);
			}

			@Override
			public void onFinished() {
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
			}

			@Override
			public void onCancelled(CancelledException arg0) {
			}
		});

		 //设置头视图不可被点击，以及不显示头视图分割线
		 listView.addHeaderView(v,null,false);
		// //不显示线，但是可以被点击
		// listView.setHeaderDividersEnabled(false);
	}

	/**
	 * 解析json
	 */
	private List<FriendsCircle> parserJson(String jsonStr) {
		List<FriendsCircle> list = new ArrayList<>();

		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			int status = jsonObject.getInt("status");
			if (status != 0) {
				return null; // 请求结果错误 返回null
			}
			// 返回结果正常，进一步进行信息解析
			JSONArray jsonArray = jsonObject.getJSONArray("friends");

			// 获得背景
			backGround = jsonObject.getString("backGround");

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				FriendsCircle friendsCircle = new FriendsCircle();
				friendsCircle.setName(jsonObj.getString("userName"));
				friendsCircle.setHead(jsonObj.getString("head"));
				friendsCircle.setContent(jsonObj.getString("content"));
				friendsCircle.setTime(DateUtil.parseStringDate(DateUtil.DATE_SDF, jsonObj.getString("time")));
				List<String> imageList = new ArrayList<>();
				JSONArray images = jsonObj.getJSONArray("images");
				for (int j = 0; j < images.length(); j++) {
					String imageUrl = images.getString(j);
					imageList.add(imageUrl);
				}
				friendsCircle.setImages(imageList);
				list.add(friendsCircle);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
