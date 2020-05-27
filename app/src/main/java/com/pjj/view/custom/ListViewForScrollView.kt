package com.pjj.view.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

/**
 * Created by XinHeng on 2018/11/22.
 * describe：
 */
class ListViewForScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ListView(context, attrs, defStyleAttr) {
    /**
     * 重写onMeasure方法，重新计算高度，达到使GridView适应ScrollView的效果
     *
     * @param widthMeasureSpec  宽度测量规则
     * @param heightMeasureSpec 高度测量规则
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}