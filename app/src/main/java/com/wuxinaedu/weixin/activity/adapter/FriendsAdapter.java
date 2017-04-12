package com.wuxinaedu.weixin.activity.adapter;

import java.io.Serializable;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.ImageDetailActivity;
import com.wuxinaedu.weixin.bean.FriendsCircle;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.DateUtil;
import com.wuxinaedu.weixin.widget.GridViewForScroll;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendsAdapter extends BaseAdapter {

	private Context context;
	private List<FriendsCircle> list;

	public FriendsAdapter(Context context, List<FriendsCircle> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_friends, null);
			viewHolder.head = (ImageView) convertView.findViewById(R.id.head_id);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name_id);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content_id);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time_id);
			viewHolder.gridView = (GridViewForScroll) convertView.findViewById(R.id.gv_id);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final FriendsCircle friends = list.get(position);
		viewHolder.time.setText(DateUtil.getDay(friends.getTime()));
		viewHolder.name.setText(friends.getName());
		viewHolder.content.setText(friends.getContent());

		// 加载头像
		String head = friends.getHead();
		x.image().loadDrawable(head, ImageOptions.DEFAULT, new CommonCallback<Drawable>() {

			@Override
			public void onSuccess(Drawable arg0) {
				viewHolder.head.setImageDrawable(arg0);
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
		
		//加载配图
		viewHolder.gridView.setAdapter(new FriendsImageAdapter(context,friends.getImages()));
		viewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context,ImageDetailActivity.class);
				intent.putExtra(Constant.GET_SERIALIZABLE,(Serializable)friends.getImages());
				intent.putExtra(Constant.GET_POSITION, position);
				intent.putExtra(Constant.GET_ISFRIENDS,true);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView head;
		TextView name, content, time;
		GridViewForScroll gridView;
	}

}
