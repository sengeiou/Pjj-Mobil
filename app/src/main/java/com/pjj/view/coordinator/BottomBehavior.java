package com.pjj.view.coordinator;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.pjj.view.custom.TitleView;

public class BottomBehavior extends CoordinatorLayout.Behavior<AppBarLayout> {

    public BottomBehavior() {
    }

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        return dependency instanceof TitleView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppBarLayout child, View dependency) {
        //计算列表y坐标，最小为0
        float y = dependency.getHeight() + dependency.getTranslationY();
        if (y < 0) {
            y = 0;
        }
        child.setY(y);
        return true;
    }
}