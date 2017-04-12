package com.wuxinaedu.weixin.activity.fragment;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.MainActivity;
import com.wuxinaedu.weixin.activity.SettingActivity;
import com.wuxinaedu.weixin.bean.UserInfor;
import com.wuxinaedu.weixin.utils.L;
import com.wuxinaedu.weixin.widget.RoundImageView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.view.ViewGroup;

public class MeFragment extends Fragment {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_me, null);
			init(view);
		}
		return view;
	}

	/**
	 * 初始化控件
	 * 
	 * @param view2
	 */
	private void init(View view) {
		// 设置按钮监听
		view.findViewById(R.id.setting_id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				L.i("跳转设置界面");
				Intent intent = new Intent(getContext(),SettingActivity.class);
				startActivity(intent);
			}
		});

		UserInfor userBasicInfor = ((MainActivity) getActivity()).getUserInfo();

		// 加载头像
		final RoundImageView imageView = (RoundImageView) view.findViewById(R.id.head);
		String uri = userBasicInfor.getHead();
		x.image().loadDrawable(uri, ImageOptions.DEFAULT, new CommonCallback<Drawable>() {

			@Override
			public void onSuccess(Drawable arg0) {
				imageView.setImageDrawable(arg0);
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
		// 用户名 及 微信号
		TextView name = (TextView) view.findViewById(R.id.wx_name);
		name.setText(userBasicInfor.getUserName());
		TextView phone = (TextView) view.findViewById(R.id.wx_code);
		phone.append(userBasicInfor.getUserPhoneNum());

	}
}
