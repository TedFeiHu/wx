package com.wuxinaedu.weixin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

public class GetJsonString {  //可以手动解析
	/**
	 * 通过JSON文件获得文件内的数据，返回一个String。
	 * 然后 new JSONObject(String);获得json对象，再解析。
	 * @param jsonFileName 文件名
	 * @param context 上下文
	 * @return
	 */
	public static String getJsonString(Context context,String jsonFileName){
		StringBuilder sb = new StringBuilder();
		AssetManager  manager = context.getResources().getAssets();
		BufferedReader bReader = null;
		try {
			 InputStream is=  manager.open(jsonFileName);
			 bReader = new BufferedReader(new InputStreamReader(is));
			 String sub;
			while (( sub=bReader.readLine()) != null) {
				sb.append(sub);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(bReader!=null){
				try {
					bReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	

}
