package com.wuxinaedu.weixin.activity;


import org.json.JSONException;
import org.json.JSONObject;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.bean.UserInfor;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.CoreUtil;
import com.wuxinaedu.weixin.utils.FileLocalCache;
import com.wuxinaedu.weixin.utils.GetJsonString;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText userName,passWord;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleName("登陆");
		
		//用户名 验证码 密码
		userName = (EditText) findViewById(R.id.user_name);
		passWord = (EditText) findViewById(R.id.pass);
		
		//点击登录按钮
		findViewById(R.id.logon).setOnClickListener(this);
		//跳转注册页面
		findViewById(R.id.login).setOnClickListener(this);
		//帮助
		findViewById(R.id.problem).setOnClickListener(this);
	}

	/**
	 * 绑定视图
	 */
	@Override
	public int getContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logon: //登陆
			judge();
			break;
		case R.id.login:  //注册
			toastMessage("跳转注册");
			break;
		case R.id.problem:
			goWeb();
			break;
		default:
			break;
		}
	}

	/**
	 * 登陆信息的判断 
	 */
	private void judge() {
		String user = userName.getText().toString();
		String pwd = passWord.getText().toString();
		
		if(CoreUtil.isEmpty(user)){
			toastMessage("手机号码不能为空");
			return;
		}
		if (user.length()!=11) {
			toastMessage("请输入手机号");
		}
		if(CoreUtil.isEmpty(pwd)){
			toastMessage("密码不能为空");
			return;
		}
		if(pwd.trim().length() < 6){
			toastMessage("密码不能少于6位");
			return;
		}	
		
		//真正的请求，请求----服务器验证----返回验证结果
		new MyTask().execute("userLogin.js");
	}
	
	/**
	 * 异步任务，模拟验证结果
	 */
	class MyTask extends AsyncTask<String,Void,UserInfor>{
		
		@Override
		protected void onPreExecute() {
			showProDialog();
		}

		@Override
		protected UserInfor doInBackground(String... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String json = GetJsonString.getJsonString(LoginActivity.this, params[0]); //把json加载到内存
			return parserJson(json); //解析json
		}

		@Override
		protected void onPostExecute(UserInfor result) {
			dismissProDialog();
			//登录
			logon(result);
			CoreUtil.finishActivityList();
		}
	}
	
	/**
	 * 解析json
	 * @param jsonStr
	 * @return
	 */
	private UserInfor parserJson(String jsonStr){
		UserInfor userBasicInfor = new UserInfor();
		
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			int status = jsonObj.getInt("status");
			if(status != 0){ //返回结果错误
				return null;
			}
			JSONObject jsonObject = jsonObj.getJSONObject("userInfor");
			userBasicInfor.setId(jsonObject.getString("userId"));
			userBasicInfor.setUserName(jsonObject.getString("userName"));
			userBasicInfor.setHead(jsonObject.getString("head"));
			userBasicInfor.setUserPhoneNum(jsonObject.getString("userPhoneNum"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return userBasicInfor;
	}
	
	private void logon(UserInfor userInfor){
		if (userInfor == null) {
			toastMessage("登录失败，请稍后重试。");
			return;
		}
		//先不做缓存
		CoreUtil.startActivity(LoginActivity.this, MainActivity.class);
		//2 请求成功 保存数据
		FileLocalCache.setSerializableData(LoginActivity.this, userInfor, Constant.USER_INFOR);
	}
	
	/**
	 * 跳转帮助页面
	 */
	private void goWeb(){
		Uri uri=Uri.parse("http://weixin.qq.com/");
		Intent intent=new Intent(Intent.ACTION_VIEW,uri);
		startActivity(intent);
	}
}




















