package com.wuxinaedu.weixin.activity.adapter;

import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.bean.Contacts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddFriendsAdapter extends BaseAdapter{

	private List<Contacts> list;
	private Context context;
	
	public AddFriendsAdapter(Context context,List<Contacts> list) {
		this.context = context;
		this.list = list;
	}
	
	public void setList(List<Contacts> list) {
		this.list = list;
		//更新listview的视图
		notifyDataSetChanged();
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
		if(convertView == null){
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_add_friends,null);
			viewholder.index = (TextView) convertView.findViewById(R.id.index_id);
			viewholder.name = (TextView) convertView.findViewById(R.id.name_id);
			viewholder.head = (ImageView) convertView.findViewById(R.id.head_id);
			viewholder.isAdd = (Button) convertView.findViewById(R.id.isadd_id);
			convertView.setTag(viewholder);
		}else{
			viewholder = (Viewholder) convertView.getTag();
		}
		
		final Contacts contacts = list.get(position);
		viewholder.name.setText(contacts.getName());
		final Drawable drawable = context.getResources().getDrawable(R.drawable.login_btn_s_press);
		final Drawable drawable2 = context.getResources().getDrawable(R.drawable.logon_btn);
		if (!contacts.isAdd()) {
			viewholder.isAdd.setBackgroundDrawable(drawable2);
			viewholder.isAdd.setClickable(false);
			viewholder.isAdd.setText(R.string.receive);
			viewholder.isAdd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					viewholder.isAdd.setClickable(false);
					viewholder.isAdd.setBackgroundDrawable(drawable);
					viewholder.isAdd.setText(R.string.received);
					contacts.setAdd(true);
				}
			});
		}else {
			viewholder.isAdd.setBackgroundDrawable(drawable);
			viewholder.isAdd.setClickable(false);
			viewholder.isAdd.setText(R.string.received);
		}
		
		
		x.image().loadDrawable(contacts.getHead(),ImageOptions.DEFAULT,new CommonCallback<Drawable>() {
			
			@Override
			public void onSuccess(Drawable arg0) {
				viewholder.head.setImageDrawable(arg0);
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
	
	class Viewholder{
		TextView index,name;
		ImageView head;
		Button isAdd;
	}
}
