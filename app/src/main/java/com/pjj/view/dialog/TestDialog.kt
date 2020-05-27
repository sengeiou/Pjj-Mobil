package com.pjj.view.dialog

import android.content.Context
import android.view.ViewGroup
import android.widget.ScrollView
import com.pjj.view.custom.DateViewGroup

/**
 * Created by XinHeng on 2018/11/30.
 * describe：
 */
class TestDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    init {
        var scrollView= ScrollView(context)
        scrollView.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        var dateViewGroup=DateViewGroup(context)
        dateViewGroup.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        scrollView.addView(dateViewGroup)
        setContentView(scrollView)
    }

}