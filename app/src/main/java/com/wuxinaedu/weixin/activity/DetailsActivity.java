package com.wuxinaedu.weixin.activity;

import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.adapter.DetailsAdapter;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.utils.Constant;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleName("详细资料");
		showRightIv(R.drawable.icon_more,null);
		new MyTask().execute();
	}
	
	private void init(){
		final Intent intent = getIntent();
		Contacts contacts = (Contacts) intent.getSerializableExtra(Constant.GET_SERIALIZABLE);
		if (contacts == null) {
			toastMessage("未知错误请重试");
			return;
		}
		final ImageView head = (ImageView) findViewById(R.id.head_id);
		TextView name = (TextView) findViewById(R.id.name_id);
		TextView wxCode = (TextView) findViewById(R.id.wx_code);
		TextView area = (TextView) findViewById(R.id.area_id);
		TextView autograph = (TextView) findViewById(R.id.autograph_id);
		name.setText(contacts.getName());
		wxCode.append(contacts.getWeCode()+"");
		area.setText(contacts.getArea());
		autograph.setText(contacts.getAutograph());
		
		GridView gridView = (GridView) findViewById(R.id.gv_id);
		gridView.setAdapter(new DetailsAdapter(this, contacts.getImages()));
		
//		findViewById(R.id.chat_id).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				intent.setClass(DetailsActivity.this,ChatActivity.class);
//				startActivity(intent);
//			}
//		});
		x.image().loadDrawable(contacts.getHead(),ImageOptions.DEFAULT, new CommonCallback<Drawable>() {
			
			@Override
			public void onSuccess(Drawable arg0) {
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
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_details;
	}
	
	class MyTask extends AsyncTask<Void,Void,List<Contacts>>{

		@Override
		protected List<Contacts> doInBackground(Void... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
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
			init();
		}
	}
}
