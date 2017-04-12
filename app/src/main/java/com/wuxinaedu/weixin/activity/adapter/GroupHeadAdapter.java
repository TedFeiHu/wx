package com.wuxinaedu.weixin.activity.adapter;

import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.bean.Contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GroupHeadAdapter extends BaseAdapter {
	private Context context;
	private List<Contacts> list;

	public GroupHeadAdapter(Context context, List<Contacts> list) {
		super();
		this.context = context;
		this.list = list;
	}
	public void setList(List<Contacts> list) {
		this.list = list;
		//更新listview的视图
		notifyDataSetChanged();
	}
	
	public List<Contacts> getList() {
		return list;
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
		final Viewholder viewholder;
		if (convertView == null) {
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_group_header_image, null);
			viewholder.head = (ImageView) convertView.findViewById(R.id.image_id);
			convertView.setTag(viewholder);
		} else {
			viewholder = (Viewholder) convertView.getTag();
		}

		final Contacts contacts = list.get(position);
		x.image().loadDrawable(contacts.getHead(), ImageOptions.DEFAULT, new CommonCallback<Drawable>() {

			@Override
			public void onSuccess(Drawable arg0) {
				// TODO xUntil 会对图片进行缓存，同一个url返回同一个Drawable对象，设置一个透明度会改变另个，
				// 故 为了将视觉上的bug消除 将Drawable对象转换为新的对象 (真实情况下不会出现此视觉上的bug)
				Bitmap bitmap = ((BitmapDrawable)arg0).getBitmap();
				viewholder.head.setImageDrawable(arg0);
				viewholder.head.setAlpha(contacts.getAlpha());
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

	class Viewholder {
		ImageView head;
	}

}
