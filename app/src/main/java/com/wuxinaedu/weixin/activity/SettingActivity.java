package com.wuxinaedu.weixin.activity;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.CoreUtil;
import com.wuxinaedu.weixin.utils.FileLocalCache;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;

public class SettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleName(R.string.setting);

		// 退出登录按钮
		findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDial();
			}
		});
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_setting;
	}
	
	/**
	 * 弹出对话框
	 */
	private void showDial(){
		AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
		aBuilder.setItems(new String[]{"退出登录","关闭应用程序"},new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					//清除登录状态
//					MySharedPreferencesUntil.removeData(SettingActivity.this,Constant.IS_LOGON);
					//清除登录信息
					FileLocalCache.delSerializableData(SettingActivity.this,Constant.USER_INFOR);
					startActivity(new Intent(SettingActivity.this,LoginActivity.class));
					//关闭 activity
					CoreUtil.finishActivityList();
					break;
				case 1:
					CoreUtil.exitApp();
					break;

				default:
					break;
				}
			}
		});
		aBuilder.show();
	}
}
