package com.wuxinaedu.weixin.activity.core;

import org.xutils.x;
import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		
		x.Ext.init(this);   //初始化xutils
		x.Ext.setDebug(true); // 是否输出debug日志
		
	}
	

}
