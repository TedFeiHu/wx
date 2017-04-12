package com.wuxinaedu.weixin.activity.adapter;

import java.util.Date;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.bean.SubscribeDetails;
import com.wuxinaedu.weixin.utils.DateUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubscribeDetailsAdapter extends BaseAdapter {
	private Context context;
	private List<SubscribeDetails> list;
	
	public SubscribeDetailsAdapter( Context context,List<SubscribeDetails> list){
		this.context=context;
		this.list=list;
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
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_sub_details,null);
			viewHolder.time=(TextView) convertView.findViewById(R.id.sub_det_time);
			viewHolder.name=(TextView) convertView.findViewById(R.id.sub_name);
			viewHolder.date=(TextView) convertView.findViewById(R.id.sub_time);
			viewHolder.content=(TextView) convertView.findViewById(R.id.sub_content);
			viewHolder.image=(ImageView) convertView.findViewById(R.id.user_ba);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		SubscribeDetails sb=list.get(position);
		Date date = sb.getDate();
		viewHolder.time.setText(DateUtil.dateToString(DateUtil.DATE_LONG, date));
		viewHolder.name.setText(sb.getTitle());
		viewHolder.date.setText(DateUtil.dateToString(DateUtil.DF, date));
		viewHolder.content.setText(sb.getContent());
		
		x.image().loadDrawable(sb.getImage(),ImageOptions.DEFAULT,new CommonCallback<Drawable>() {
			
			@Override
			public void onSuccess(Drawable arg0) {
				viewHolder.image.setImageDrawable(arg0);
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
	
	class ViewHolder{
		TextView name;
		TextView time;
		TextView date;
		TextView content;
		ImageView image;
	}

}
