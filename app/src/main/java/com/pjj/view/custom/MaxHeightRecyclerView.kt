package com.pjj.view.custom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * Created by XinHeng on 2018/12/05.
 * describe：
 */
class MaxHeightRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    var mMaxHeight = 0
        set(value) {
            field = value
            requestLayout()
        }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var heightSpec = heightSpec
        if (mMaxHeight > 0) {
            heightSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthSpec, heightSpec)
    }
}