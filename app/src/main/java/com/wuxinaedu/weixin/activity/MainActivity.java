package com.wuxinaedu.weixin.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.activity.fragment.ContactsFragment;
import com.wuxinaedu.weixin.activity.fragment.FindFragment;
import com.wuxinaedu.weixin.activity.fragment.MeFragment;
import com.wuxinaedu.weixin.activity.fragment.WxFragment;
import com.wuxinaedu.weixin.bean.UserInfor;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.FileLocalCache;
import com.wuxinaedu.weixin.utils.L;
import com.wuxinaedu.weixin.widget.MyRadioButton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;

public class MainActivity extends BaseActivity {

	private MyRadioButton[] bottoms;
	private RadioGroup radioGroup;
	private int[] strIds = { R.string.app_name, R.string.contact, R.string.find, R.string.me };
	private PopupWindow pop;
	private Fragment[] fragments;
	private ViewPager viewPager;
	private UserInfor userBasicInfor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bottoms = new MyRadioButton[4];
		bottoms[0] = (MyRadioButton) findViewById(R.id.wx_id);
		bottoms[1] = (MyRadioButton) findViewById(R.id.contacts_id);
		bottoms[2] = (MyRadioButton) findViewById(R.id.find_id);
		bottoms[3] = (MyRadioButton) findViewById(R.id.me_id);
		radioGroup = (RadioGroup) findViewById(R.id.rg_id);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int selector;
				switch (checkedId) {
				case R.id.wx_id: // 微信
					selector = 0;
					break;
				case R.id.contacts_id: // 联系人
					selector = 1;
					break;
				case R.id.find_id: // 发现
					selector = 2;
					break;
				case R.id.me_id: // 我的
					selector = 3;
					break;
				default:
					selector = 0;
					break;
				}

				select(selector);
				

				if(viewPager.getCurrentItem() != selector){
					L.e("切换---onCheckedChanged-------->"+selector);
					viewPager.setCurrentItem(selector,false);
				}
			}

		});
		select(0);

		// 初始化fragment
		fragments = new Fragment[4];
		fragments[0] = new WxFragment();
		fragments[1] = new ContactsFragment();
		fragments[2] = new FindFragment();
		fragments[3] = new MeFragment();

		viewPager = (ViewPager) findViewById(R.id.fl_id);
		viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

		viewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {
				L.e("-onPageSelected---》》"+arg0);
//				if(viewPager.getCurrentItem() != arg0){
//					radioGroup.check(rasdioIds[arg0]);
				bottoms[arg0].setChecked(true);
//				}

			}
		});

	}

	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	private void select(int selector) {
		setTitleName(strIds[selector]);

		switch (selector) {
		case 0:
			showRightIv(R.drawable.icon_add, new OnClickListener() {
				@Override
				public void onClick(View v) {
					showPop();
				}
			});
			break;
		case 1:
			showRightIv(R.drawable.icon_menu_addfriend, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
			break;
		default:
			hideRigehtImage();
			break;
		}
	}

	protected void showPop() {
		// 构造PopupWindow对象以及设置其内容
		if (pop == null) {
			View view = this.getLayoutInflater().inflate(R.layout.item_pop, null);
			ListView lv = (ListView) view.findViewById(R.id.liv);
			SimpleAdapter simpleAdapter = initPopAdapter();
			lv.setAdapter(simpleAdapter);

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					switch (position) {
					case 0: // 打开群聊界面
						toastMessage("群聊界面");
						// startActivity(new
						// Intent(MainActivity.this,GroupActivity.class));
						break;
					case 1: // 打开添加好友界面
						toastMessage("添加好友");
						// startActivity(new
						// Intent(MainActivity.this,AddFriendsActivity.class));
						break;
					case 2:
						toastMessage("启动相机");
						break;
					case 3:
						toastMessage("支付");
						break;

					default:
						break;
					}
					// 关闭
					pop.dismiss();
				}
			});

			pop = new PopupWindow(view, 480, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//			pop.setBackgroundDrawable(new BitmapDrawable());
			// 设置动画
			pop.setAnimationStyle(R.style.popwin_anim_style);
		}

		pop.showAsDropDown(rightIv);
	}

	/**
	 * 初始化PopupWindow的ListView数据。
	 */
	private SimpleAdapter initPopAdapter() {
		Map<String, Object> map;
		List<Map<String, Object>> data = new ArrayList<>();
		map = new HashMap<>();
		map.put("image", R.drawable.icon_menu_group);
		map.put("text", "发起群聊");
		data.add(map);
		map = new HashMap<>();
		map.put("image", R.drawable.icon_menu_addfriend);
		map.put("text", "添加好友");
		data.add(map);
		map = new HashMap<>();
		map.put("image", R.drawable.icon_menu_sao);
		map.put("text", "扫一扫");
		data.add(map);
		map = new HashMap<>();
		map.put("image", R.drawable.abv);
		map.put("text", "微信支付");
		data.add(map);
		SimpleAdapter sa = new SimpleAdapter(this, data, R.layout.item_pop_item, new String[] { "image", "text" },
				new int[] { R.id.image_id, R.id.tv_id });
		return sa;
	}

	class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragments[arg0];
		}

		@Override
		public int getCount() {
			return fragments.length;
		}

	}
	
	/**
	 * 获得登录用户的基本信息
	 * @return
	 */
	public UserInfor getUserInfo(){
		//获取登录或注册成功保存的用户数据
		if(userBasicInfor == null){
			userBasicInfor = (UserInfor) FileLocalCache.getSerializableData(this,Constant.USER_INFOR);
//			userBasicInfor= (UserInfor) FileLocalCache.getSerializableData(this, Constant.USER_INFOR);
		}
		return userBasicInfor;
	}
}
