package com.wuxinaedu.weixin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 不可滚动的GridView
 */
public class GridViewNOScroll extends GridView{

    public GridViewNOScroll(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        if(ev.getAction() == MotionEvent.ACTION_MOVE)  
        {  
            return true;  
        }  
        return super.dispatchTouchEvent(ev);  
    }
    
    /**
     * 重写  onTouchEvent 是的gridView点击效果可以穿透
     */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);
		return false;
	}

    
}