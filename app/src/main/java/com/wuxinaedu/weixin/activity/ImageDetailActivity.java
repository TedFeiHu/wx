package com.wuxinaedu.weixin.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.widget.TouchImageView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ImageDetailActivity extends BaseActivity {

	private List<String> list;
	private int position;
	private List<View> listView;
	/**
	 * 是否是重朋友圈界面过来 因为朋友圈传的url 聊天界面传的uri
	 */
	private boolean isFriends;

	@Override
	protected int getContentView() {
		return R.layout.activity_image_detail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleName("图片浏览");

		final TextView textView = (TextView) findViewById(R.id.id_index);
		final ViewPager viewPager = (ViewPager) findViewById(R.id.view_page);

		Intent intent = getIntent();
		isFriends = intent.getBooleanExtra(Constant.GET_ISFRIENDS, false);
		list = (List<String>) intent.getSerializableExtra(Constant.GET_SERIALIZABLE);
		position = getIntent().getIntExtra(Constant.GET_POSITION, 0);
		if (list == null) {
			return;
		}

		listView = new ArrayList<>();
		// 添加视图到 集合
		for (int i = 0; i < list.size(); i++) {
			View view = getLayoutInflater().inflate(R.layout.item_image_detail, null);
			listView.add(view);
		}
		// 设置第几张
		textView.setText((position + 1) + "/" + list.size());

		viewPager.setAdapter(new MyAdapter());

		// 选中
		viewPager.setCurrentItem(position);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				// 更改选中第几张
				textView.setText((position + 1) + "/" + list.size());
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	/**
	 * adapter
	 */
	class MyAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return listView.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(listView.get(position));
			final TouchImageView touchImageView = (TouchImageView) listView.get(position).findViewById(R.id.image_id);
			if (isFriends) {
				x.image().loadDrawable(list.get(position), ImageOptions.DEFAULT, new CommonCallback<Drawable>() {

					@Override
					public void onSuccess(Drawable arg0) {
						touchImageView.setImageDrawable(arg0);
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

				// x.image().bind(touchImageView,list.get(position),ImageOptions.DEFAULT);
			} else {
				// touchImageView.setImageBitmap(ImageUtil.getSmallBitmap(list.get(position),480,800));
			}
			return listView.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(listView.get(position));
		}
	}

}
