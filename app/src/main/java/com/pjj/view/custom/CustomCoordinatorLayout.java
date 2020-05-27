package com.pjj.view.custom;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by XinHeng on 2018/12/08.
 * describe：
 */
public class CustomCoordinatorLayout extends CoordinatorLayout {
    public CustomCoordinatorLayout(Context context) {
        super(context);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*float y = ev.getY();
        int dp = ViewUtils.getDp(R.dimen.dp_266);
        int top = getTop() - ViewUtils.getDp(R.dimen.dp_42);
        Log.e("TAG", "onInterceptTouchEvent: " + top);
        int bottom = dp + top;
        Log.e("TAG", "onTouchEvent: bottom=" + bottom + ", y=" + y);
        if (y > 0 && y < bottom) {
            return false;
        }*/
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
}
