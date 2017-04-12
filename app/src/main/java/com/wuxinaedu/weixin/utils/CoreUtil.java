package com.wuxinaedu.weixin.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wuxinaedu.weixin.activity.core.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class CoreUtil {
	
	private static List<BaseActivity> list = new ArrayList<>();
	private static float sDensity;

	/**
	 * 启动activity
	 * @param context
	 * @param clz
	 */
	public static void startActivity(Context context, Class<?> clz){
		Intent intent = new Intent(context, clz);
		context.startActivity(intent);
	}
	
	
	/**
	 * 集合所有打开的activity
	 * @param fActivity
	 */
	public static void addToActivityList(BaseActivity activity){
		if(!list.contains(activity)){
			list.add(activity);
		}
	}
	
	
	/**
	 * 移除关闭的activity
	 * 在调用 finish方法的时候 同时调用这个方法。不要在onDestroy()方法中调用，因为在 遍历集合关闭时也会调用
	 */
	public static void removeActivity(BaseActivity activity){
		list.remove(activity);
	}
	
	/**
	 * 关闭activity集合
	 */
	public static void finishActivityList(){
		L.e("activity集合大小-----》》"+list.size());
		for (int i = 0; i < list.size(); i++) {
			L.e("被关闭Activity-----》》"+list.get(i));
			list.get(i).finish();
		}
		list.clear();
	}
	
	/**
	 * 退出出程序
	 * 关闭activity 并杀死进程。
	 */
	public static void exitApp(){
		for (BaseActivity activity : list) {
			activity.finish();
			L.e("exitApp:-----》》"+activity);
		}
		list.clear();
		L.e("android.os.Process.killProcess(android.os.Process.myPid());");
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	
	
	
	/**public static boolean isEmpty(String str)
	 * 判断字符串是否为空。无视空格
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str){
		if(str == null || str.trim().length()==0){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int spToPixel(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	/**
	 * dp转换为像素
	 * @param context
	 * @param nDip
	 * @return
	 */
	public static int dipToPixel(Context context, int nDip) {
		if (sDensity == 0) {
			final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			wm.getDefaultDisplay().getMetrics(dm);
			sDensity = dm.density;
		}
		return (int) (sDensity * nDip);
	}
	
	/**
	 * 获取屏幕 宽 高
	 * @param activity
	 * @return 数组  0 为宽度 1 为高度
	 */
	public static int[] getDisplay(Activity activity){
		int[] display = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		display[0] = dm.widthPixels;
		display[1] = dm.heightPixels;
		return display;
	}
	
	/**
	 * 判断是否为中文
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str){
		Pattern p=Pattern.compile("^[\u4e00-\u9fa5]*$");
		Matcher m=p.matcher(str);
	    if(m.matches()){
	    	return true;
	    }else{
	    	return false;
	    }
	}
	
	/**
	 * 判断是否为字母
	 * @param str
	 * @return
	 */
	public static boolean isEnglish(String fstrData){   
        char   c   =   fstrData.charAt(0);   
        if(((c>='a'&&c<='z')   ||   (c>='A'&&c<='Z'))){   
              return   true;   
        }else{   
              return   false;   
        }   
	}
	
}
