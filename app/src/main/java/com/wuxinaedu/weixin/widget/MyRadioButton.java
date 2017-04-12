package com.wuxinaedu.weixin.widget;

import com.wuxinaedu.weixin.utils.CoreUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class MyRadioButton extends RadioButton{

	public MyRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		//获取 drawable数组  左上右下的顺序
		Drawable drawable = getCompoundDrawables()[1];
		int size = CoreUtil.dipToPixel(context,24);
		//设置drawable的 图片的大小      保持图片的 宽高比例  
//		L.e("----->>"+size*(96.0/84.0));
		//第一0是距左边距离，第二0是距上边距离，40分别是长宽
		drawable.setBounds(0, 0,(int) (size*(96.0/84.0)),size);
		setCompoundDrawables(null,drawable, null, null);//只放上边
	}

}
