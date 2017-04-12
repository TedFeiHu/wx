package com.wuxinaedu.weixin.activity.fragment;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.FriendsActivity;
import com.wuxinaedu.weixin.activity.MainActivity;
import com.wuxinaedu.weixin.bean.UserInfor;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.L;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class FindFragment extends Fragment {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view != null) {
			return view;
		}
		view = inflater.inflate(R.layout.fragment_find, null);
		view.findViewById(R.id.friends_id).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), FriendsActivity.class);
				UserInfor userBasicInfor = ((MainActivity) getActivity()).getUserInfo();
				intent.putExtra(Constant.USER_INFOR, userBasicInfor);
				startActivity(intent);
				L.i("朋友圈");
			}
		});
		return view;
	}
}
