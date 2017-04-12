package com.wuxinaedu.weixin.activity.adapter;

import java.util.Collections;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CancelledException;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.utils.PinyinComparator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter implements SectionIndexer {
	private List<Contacts> list;
	private Context context;

	public GroupAdapter(Context context, List<Contacts> list) {
		this.context = context;
		// 对联系人进行排序 并将联系人中文名对应的拼音，以及首字母进行保存
		Collections.sort(list, new PinyinComparator());
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_group, null);
			viewholder.index = (TextView) convertView.findViewById(R.id.index_id);
			viewholder.name = (TextView) convertView.findViewById(R.id.name_id);
			viewholder.head = (ImageView) convertView.findViewById(R.id.head_id);
			viewholder.cBox = (CheckBox) convertView.findViewById(R.id.cb_id);

			convertView.setTag(viewholder);
		} else {
			viewholder = (Viewholder) convertView.getTag();
		}

		Contacts contacts = list.get(position);

		// 设置选中状态
		viewholder.cBox.setChecked(contacts.isAddGroup());

		viewholder.name.setText(contacts.getName());
		// 对联系人上显示的字母进行设置
		String first = contacts.getNameFirst();
		if (position != 0) {
			// 获取上一个联系人的姓名首字母
			String lastFirst = list.get(position - 1).getNameFirst();
			// 和当前联系人进行比较 如果相同隐藏 如果不同 显示
			if (!lastFirst.equals(first)) {
				viewholder.index.setVisibility(View.VISIBLE);
				viewholder.index.setText(first);
			} else {
				viewholder.index.setVisibility(View.GONE);
			}
		} else {
			viewholder.index.setVisibility(View.VISIBLE);
			viewholder.index.setText(first);
		}
		x.image().loadDrawable(contacts.getHead(), ImageOptions.DEFAULT, new CommonCallback<Drawable>() {

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

	class Viewholder {
		TextView index, name;
		ImageView head;
		CheckBox cBox;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		if('#' == sectionIndex){ //如果 触摸为# 返回1
			return 1;
		}
		for (int i = 0; i < list.size(); i++) {
			Contacts contacts = list.get(i);
			String name = contacts.getNameFirst();
			int first = name.charAt(0);
			if(first == sectionIndex){
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
