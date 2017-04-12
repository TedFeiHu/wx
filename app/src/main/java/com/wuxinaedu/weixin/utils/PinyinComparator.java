package com.wuxinaedu.weixin.utils;

import java.util.Comparator;

import com.wuxinaedu.weixin.bean.Contacts;

/**
 * 实现中文和英文排序接口
 * 并将联系人中文名对应的拼音，以及首字母进行保存
 * @author Administrator
 *
 */
public class PinyinComparator implements Comparator<Contacts> {

	@Override
	public int compare(Contacts user0, Contacts user1) {
		// 按照名字排序
		String catalog0,catalog1;
		
		catalog0 = user0.getNamePinyin();
		catalog1 = user1.getNamePinyin();
		int flag = catalog0.compareTo(catalog1);
		return flag;
	}

}
