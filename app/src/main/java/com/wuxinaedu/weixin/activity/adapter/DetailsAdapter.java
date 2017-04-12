package com.wuxinaedu.weixin.activity.adapter;

import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class DetailsAdapter extends BaseAdapter{

	//图片url集合
	private List<String> list;
	private Context context;
	
	public DetailsAdapter(Context context,List<String> list) {
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
		convertView = LayoutInflater.from(context).inflate(R.layout.item_details_iamge,null);
		final ImageView imageView = (ImageView) convertView.findViewById(R.id.im_id);
		
		//加载图片
		x.image().loadDrawable(list.get(position),ImageOptions.DEFAULT,new CommonCallback<Drawable>() {
			
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
		return convertView;
	}
}
