package com.wuxinaedu.weixin.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.adapter.GroupAdapter;
import com.wuxinaedu.weixin.activity.adapter.GroupHeadAdapter;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.CoreUtil;
import com.wuxinaedu.weixin.utils.DateUtil;
import com.wuxinaedu.weixin.utils.FileLocalCache;
import com.wuxinaedu.weixin.utils.GetJsonString;
import com.wuxinaedu.weixin.utils.L;
import com.wuxinaedu.weixin.utils.PingYinUtil;
import com.wuxinaedu.weixin.widget.ClearEditText;
import com.wuxinaedu.weixin.widget.HorizontalListView;
import com.wuxinaedu.weixin.widget.SideBar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class GroupActivity extends BaseActivity {
	private GroupAdapter adapter;
	private List<Contacts> contacts;
	private boolean isTextEmpty = true, canRemove;
	private List<Contacts> addGroupList = new ArrayList<>();
	private ClearEditText clearEdit;
	private ImageView searchBar;
	private GroupHeadAdapter headAdapter;
	private HorizontalListView horizontalListView;
	private int conunt;

	private OnClickListener onClickListener;

	@Override
	protected int getContentView() {
		return R.layout.activity_group;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitleName(R.string.group);
		showRightTv(R.string.queding, null);
		onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				toastMessage(conunt);
			}
		};
		contacts = (List<Contacts>) FileLocalCache.getSerializableData(this, Constant.GET_CONTACTS);

		if (contacts == null) {
			new MyTask().execute();

		} else {
			init();
		}
	}

	private void init() {
		ListView listView = (ListView) findViewById(R.id.lv_id);

		// 添加输入文字改变的监听器
		clearEdit = (ClearEditText) findViewById(R.id.serch_ed_id);
		clearEdit.addTextChangedListener(new MyTextWatcher());

		adapter = new GroupAdapter(this, contacts);
		listView.setAdapter(adapter);

		// 添加头部 并设置头部各个组件的监听
		View v = getLayoutInflater().inflate(R.layout.item_group_head, null);
		listView.addHeaderView(v, null, true);

		// 将自定义的SideBar与 联系人列表的listView关联起来，实现联动 以及关联中间提示字母TextView
		SideBar sideBar = (SideBar) findViewById(R.id.sb_id);
		TextView t = (TextView) findViewById(R.id.tv_id);
		sideBar.setListView(listView);
		sideBar.setTextView(t);

		searchBar = (ImageView) findViewById(R.id.search_bar);
		horizontalListView = (HorizontalListView) findViewById(R.id.serch_lv_id);

		headAdapter = new GroupHeadAdapter(GroupActivity.this, addGroupList);
		horizontalListView.setAdapter(headAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_id);
				boolean isChecked = checkBox.isChecked();
				// 改变 checkBox状态
				checkBox.setChecked(!isChecked);
				Contacts contact = adapter.getList().get(position - 1);
				if (!isChecked) { // 如果是选中 加入到集合中
					addData(contact);
				} else if (addGroupList.contains(contact)) { // 如果是取消选中 移除
					removeData(contact);
				}
			}
		});
		
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//移出数据
				removeData(addGroupList.get(position));
			}
		});
	}

	/**
	 * 移出数据 更新列表项
	 * 
	 * @param position
	 */
	private void removeData(Contacts contacts) {
		// 修改选中状态
		contacts.setAddGroup(false);
		contacts.setAlpha(255);
		// 移出集合
		addGroupList.remove(contacts);
		// 更新选中状态
		adapter.notifyDataSetChanged();
		// 移除顶部头像
		notifiChanged();

		changeTitle(false);
	}

	/**
	 * 改变标题右上角的数字
	 * 
	 * @param isAdd
	 *            true + ; flase -
	 */
	private void changeTitle(boolean isAdd) {
		if (isAdd) {
			// 改变标题右上角的数字
			showRightTv("确定（" + ++conunt + "）", onClickListener);
		} else {
			if (conunt > 1) {
				showRightTv("确定（" + --conunt + "）", onClickListener);
			} else {
				--conunt;
				showRightTv("确定", null);
			}
		}
	}

	/**
	 * 添加数据 更新列表项
	 * 
	 * @param position
	 */
	private void addData(Contacts contacts) {
		if (addGroupList.size() != 0) {
			// 设置上一个的透明度
			addGroupList.get(addGroupList.size() - 1).setAlpha(255);
			canRemove = false;
		}
		// 修改选中状态
		contacts.setAddGroup(true);
		// 添加集合
		addGroupList.add(contacts);
		// 更新选中状态
		// adapter.notifyDataSetChanged();
		// 添加到顶部
		notifiChanged();
		changeTitle(true);
	}

	/**
	 * 更新顶部视图
	 */
	private void notifiChanged() {
		// headAdapter.setList(addGroupList);
		int size = addGroupList.size();
		horizontalListView.setSelection(headAdapter.getCount() - 1);
		LinearLayout.LayoutParams l = (LayoutParams) horizontalListView.getLayoutParams();
		// 获取屏幕宽度
		int pWidth = CoreUtil.getDisplay(this)[0];
		// 计算出list每个需要显示的宽度
		int eachWidth = CoreUtil.dipToPixel(this, 42);

		int maxWidth = pWidth - CoreUtil.dipToPixel(this, 60);

		if (size == 0) { // 默认状态（没有选中好友时）
			searchBar.setVisibility(View.VISIBLE);// 显示搜索图片
			headAdapter.setList(addGroupList);
			l.width = eachWidth;

		} else {
			searchBar.setVisibility(View.GONE);// 隐藏搜索图片
			headAdapter.setList(addGroupList);
			int needWidth = eachWidth * size;
			// 如下计算结果
			if (needWidth <= maxWidth) {
				l.width = needWidth;
			}
		}
		// 改变横向listview 的宽度
		horizontalListView.setLayoutParams(l);
	}

	/**
	 * 返回键删除第一个
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DEL && addGroupList.size() != 0 && isTextEmpty) {
			if (canRemove) {
				removeData(addGroupList.get(addGroupList.size() - 1));
				canRemove = false;
			} else {
				canRemove = true;
				Contacts contacts = addGroupList.get(addGroupList.size() - 1);
				contacts.setAlpha(100);
				headAdapter.notifyDataSetChanged();
			}
			horizontalListView.setSelection(headAdapter.getCount() - 1);
		}
		if (CoreUtil.isEmpty(clearEdit.getText().toString())) {
			isTextEmpty = true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class MyTextWatcher implements TextWatcher {
		private List<Contacts> searchList = new ArrayList<>();
		private List<Contacts> searchSubList = new ArrayList<>();

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			isTextEmpty = false;
			// 获得搜索框输入的数字
			String input = s.toString();
			// 是否文中文
			boolean isChinese = CoreUtil.isChinese(input);

			// 如果为空
			if (CoreUtil.isEmpty(input)) {
				adapter.setList(contacts);
				return;
			}
			// 每次数据改变 清空集合
			searchList.clear();
			searchSubList.clear();
			// 遍历集合 匹配数据
			for (Contacts contacts2 : contacts) {
				if (isChinese) { // 如果为中文 匹配以此文字开头的数据
					String name = contacts2.getName();
					if (name.startsWith(input)) { // 如果为指定开头 加入到集合中
						searchList.add(contacts2);
					} else if (name.indexOf(input) != -1) {// 如果包含该文字 加入到另个集合中
						searchSubList.add(contacts2);
					}
				} else {
					input = input.toUpperCase();
					String namePy = contacts2.getNamePinyin();
					String namePyFirst = contacts2.getNameFirstPinyin();
					if (namePy.startsWith(input)) {// 如果为指定开头 加入到集合中
						searchList.add(contacts2);
					} else if (namePyFirst.startsWith(input)) { // 如果包含该文字
																// 加入到另个集合中
						searchSubList.add(contacts2);
					}

				}
			}
			// 将以搜索文字 开头的数据 和包含搜索文字的数据集合到一起
			if (searchSubList.size() != 0) {
				searchList.addAll(searchSubList);
			}
			adapter.setList(searchList);
		}
	}

	/**
	 * 解析 contacts json
	 * 
	 * @param jsonStr
	 * @return
	 */
	private List<Contacts> parserJson(String jsonStr) {
		List<Contacts> list = new ArrayList<>();
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			int status = jsonObject.getInt("status");
			if (status != 0) { // 返回结果错误
				return list;
			}
			JSONArray jsonArray = jsonObject.getJSONArray("contacts");
			for (int i = 0; i < jsonArray.length(); i++) {
				Contacts contacts = new Contacts();

				JSONObject jsonObj = jsonArray.getJSONObject(i);

				contacts.setName(jsonObj.getString("name"));
				contacts.setArea(jsonObj.getString("area"));
				contacts.setWeCode(jsonObj.getInt("weCode"));
				contacts.setHead(jsonObj.getString("head"));
				contacts.setAutograph(jsonObj.getString("autograph"));
				contacts.setLastStr(jsonObj.getString("lastStr"));
				contacts.setNewsNum(jsonObj.getInt("newsNum"));
				contacts.setAdd(jsonObj.getBoolean("isAdd"));
				String time = jsonObj.getString("lastTime");
				Date date = DateUtil.parseStringDate(DateUtil.DATE_SDF, time);
				contacts.setLastTime(date);
				JSONArray jsonArr = jsonObj.getJSONArray("images");
				List<String> list2 = new ArrayList<>();
				for (int j = 0; j < jsonArr.length(); j++) {
					list2.add(jsonArr.getString(j));
				}
				contacts.setImages(list2);

				// 最后 对名字进行一次解析
				String name = contacts.getName();
				String str = PingYinUtil.getPingYin(name).toUpperCase();
				contacts.setNamePinyin(str);
				String first = str.substring(0, 1);
				// 如果名字首字母不为 字母 统称为 #
				if (CoreUtil.isEnglish(first)) {
					contacts.setNameFirst(first);
				} else {
					contacts.setNameFirst("#");
				}
				contacts.setNameFirstPinyin(PingYinUtil.converterToFirstSpell(name).toUpperCase());

				list.add(contacts);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	class MyTask extends AsyncTask<Void, Void, List<Contacts>> {

		@Override
		protected List<Contacts> doInBackground(Void... params) {
			try {
				Thread.sleep(1200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String jsonStr = GetJsonString.getJsonString(GroupActivity.this, "contacts.js");
			return parserJson(jsonStr);
		}

		@Override
		protected void onPreExecute() {
			showProDialog();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(List<Contacts> result) {
			super.onPostExecute(result);
			dismissProDialog();
			contacts = result;
			init();
		}
	}
}
