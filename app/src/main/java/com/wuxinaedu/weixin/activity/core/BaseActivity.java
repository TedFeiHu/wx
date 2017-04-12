package com.wuxinaedu.weixin.activity.core;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.utils.CoreUtil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity{
	//加载对话框
	protected ProgressDialog pd;
	protected ImageView leftIv,rightIv;
	protected TextView title, rightTv;
	protected OnClickListener onClickListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(getContentView());
		
		CoreUtil.addToActivityList(this);
		
		leftIv = (ImageView) findViewById(R.id.left_iv_id);
		rightIv = (ImageView) findViewById(R.id.right_iv_id);
		title = (TextView) findViewById(R.id.title_tv_id);
		rightTv = (TextView) findViewById(R.id.right_tv_id);
		
		leftIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onClickListener == null) { //默认的
					//移除已关闭的activity
        			CoreUtil.removeActivity(BaseActivity.this);
					finish();
				} else { //说明用户自定义了点击事件
					onClickListener.onClick(v);
				}
			}
		});
	}
	
	/**
	 * 获取子activity需要绑定的视图ID
	 * @return
	 */
	protected abstract int getContentView();
	
	/**
	 * 设置标题
	 * @param name
	 */
	protected void setTitleName(String name){
		title.setText(name);
	}
	
	protected void setTitleName(int resid) {
		title.setText(resid);
	}
	
	/**
	 * 隐藏左侧的返回键
	 */
	protected void hideLeftIv() {
		leftIv.setVisibility(View.GONE);
	}
	
	/**
	 * 用户调用了，必然会传一个OnClickListener，如果onClickListener==null 则用默认的事件
	 * @param onClickListener
	 */
	protected void setLeftIvOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}
	
	/**
	 * 设置右侧的图片
	 * @param resid
	 * @param onClickListener
	 */
	protected void 	showRightIv(int resid, OnClickListener onClickListener){
		rightIv.setVisibility(View.VISIBLE);
		rightIv.setImageResource(resid);
		rightIv.setOnClickListener(onClickListener);
	}
	/**
	 * 设置右侧的文字
	 * @param resid
	 * @param onClickListener
	 */
	protected void 	showRightTv(int resid, OnClickListener onClickListener){
		rightTv.setVisibility(View.VISIBLE);
		rightTv.setText(resid);
		rightTv.setOnClickListener(onClickListener);
	}
	protected void 	showRightTv(String str, OnClickListener onClickListener){
		rightTv.setVisibility(View.VISIBLE);
		rightTv.setText(str);
		rightTv.setOnClickListener(onClickListener);
	}
	
	/**
     * 设置隐藏右侧按钮
     */
    protected void hideRigehtImage(){
    	rightIv.setVisibility(View.INVISIBLE);
    }
	
	/**
	 * 提示信息
	 */
	protected void toastMessage(Object message){
		Toast.makeText(getApplicationContext(),message.toString(),Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 开启加载提示对话款
	 */
	public void showProDialog() {
		if(pd == null ){
			pd = new ProgressDialog(this);
			pd.setMessage("加载中，请稍后...");
			pd.setCancelable(false);
		}
		if(pd.isShowing()){
//			pd.dismiss();	
			return;
		}
		pd.show();
	}
	/**
	 * 关闭加载提示对话款
	 */
	public void dismissProDialog() {
		if(pd != null ){
			pd.dismiss();	
		}
	}
	
}
