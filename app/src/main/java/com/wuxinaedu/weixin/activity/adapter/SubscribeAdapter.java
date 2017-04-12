package com.wuxinaedu.weixin.activity.adapter;

import java.util.List;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.utils.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SubscribeAdapter extends BaseAdapter{

	private List<Contacts> list;
	private Context context;
	
	public SubscribeAdapter(Context context,List<Contacts> list) {
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
		final Viewholder viewholder;
		if(convertView == null){
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_we,null);
			viewholder.newsNum = (TextView) convertView.findViewById(R.id.news_num_id);
			viewholder.name = (TextView) convertView.findViewById(R.id.name_id);
			viewholder.head = (ImageView) convertView.findViewById(R.id.head_id);
			viewholder.time = (TextView) convertView.findViewById(R.id.time_id);
			viewholder.lastStr = (TextView) convertView.findViewById(R.id.last_str_id);
			convertView.setTag(viewholder);
		}else{
			viewholder = (Viewholder) convertView.getTag();
		}
		
		final Contacts contacts = list.get(position);
		
		viewholder.name.setText(contacts.getName());
		viewholder.time.setText(DateUtil.getDay(contacts.getLastTime()));
		viewholder.lastStr.setText(contacts.getLastStr());
		int num = contacts.getNewsNum();
		//消息数处理
		if(num == 0){
			viewholder.newsNum.setVisibility(View.INVISIBLE); 
		}else{
			viewholder.newsNum.setText(num+"");
			viewholder.newsNum.setVisibility(View.VISIBLE); 
		}
		viewholder.head.setImageResource(R.drawable.icon_public);
		return convertView;
	}
	
	class Viewholder{
		TextView newsNum,name,lastStr,time;
		ImageView head;
	}
}
