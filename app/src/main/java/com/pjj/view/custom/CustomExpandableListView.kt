package com.pjj.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ExpandableListView

/**
 * Created by XinHeng on 2018/11/22.
 * describe：
 */
class CustomExpandableListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ExpandableListView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //不限制尺寸
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}