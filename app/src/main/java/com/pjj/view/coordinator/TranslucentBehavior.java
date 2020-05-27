package com.pjj.view.coordinator;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pjj.R;
import com.pjj.utils.ViewUtils;
import com.pjj.view.custom.TitleView;

public class TranslucentBehavior extends CoordinatorLayout.Behavior<TitleView> {

    /**
     * 标题栏的高度
     */
    private int mToolbarHeight = 0;

    public TranslucentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

 /*   @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull TitleView child, @NonNull View dependency) {
        return dependency instanceof RelativeLayout;
    }*/

    /**
     * 必须要加上  layout_anchor，对方也要layout_collapseMode才能使用
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TitleView child, View dependency) {

        // 初始化高度
        if (mToolbarHeight == 0) {
            mToolbarHeight = child.getBottom() * 2;//为了更慢的 126, 126, 126
            Log.e("TAG" ,"dp42="+ ViewUtils.getDp(R.dimen.dp_42)+", "+child.getMeasuredHeight()+", "+child.getBottom());
        }
        //
        //计算toolbar从开始移动到最后的百分比
        Log.e("TAG", "onDependentViewChanged dependency=" + dependency);
        Log.e("TAG", "onDependentViewChanged dependency=" + dependency.getY() +", "+dependency.getMeasuredHeight());
        float percent = dependency.getY() / mToolbarHeight;
        Log.e("TAG", "onDependentViewChanged percent=" + percent);
        //百分大于1，直接赋值为1
        if (percent >= 1) {
            percent = 1f;
        }

        // 计算alpha通道值
        float alpha = percent * 255;


        //设置背景颜色
        child.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
        //child.
        return true;
    }
}