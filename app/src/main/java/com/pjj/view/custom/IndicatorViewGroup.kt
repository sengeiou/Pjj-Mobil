package com.pjj.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import java.util.*

class IndicatorViewGroup @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private val dp5 = ViewUtils.getDp(R.dimen.dp_5)
    private val dp11 = ViewUtils.getDp(R.dimen.dp_11)
    private val viewList = LinkedList<View>()
    private var viewPadding = dp5
    private var selectColor = ViewUtils.getColor(R.color.color_theme)
    private var unSelectColor = ViewUtils.getColor(R.color.color_b4b4b4)
    private var mIndex = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        var width = 0
        (0 until childCount).forEach {
            width += getChildAt(it).measuredWidth
        }

        //Log.e("TAG", "onMeasure childcount=$childCount  width=$width")
        setMeasuredDimension(width + viewPadding * (childCount - 1), dp5)
    }

    fun setChildCounts(count: Int) {
        if (count < 1) {
            return
        }
        viewList.clear()
        removeAllViews()
        //Log.e("TAG", "setChildCounts count=$count")
        mIndex = 0
        val selectView = getSelectView()
        viewList.add(selectView)
        addView(selectView)
        (2..count).forEach { _ ->
            val unSelectView = getUnSelectView()
            viewList.add(unSelectView)
            addView(unSelectView)
        }
        //requestLayout()
        //invalidate()
    }

    fun selectIndex(index: Int) {
        if (index == mIndex || index >= childCount) {
            return
        }
        //Log.e("TAG", "selectIndex index=$index mIndex=$mIndex")
        val removeAt = viewList.removeAt(mIndex)
        viewList.add(index, removeAt)
        mIndex = index
        requestLayout()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var right: Int
        viewList.forEach {
            right = left + it.measuredWidth
            //Log.e("TAG", "onLayout left=$left right=$right  bottom=$dp5")
            it.layout(left, 0, right, dp5)
            left = right + viewPadding
        }
    }

    private fun getSelectView(): View {
        return View(context).apply {
            layoutParams = LayoutParams(dp11, dp5)
            background = getBgDrawable(selectColor).apply {
                setBounds(0, 0, dp11, dp5)
            }
            //background=ColorDrawable(Color.RED)
        }
    }

    private fun getUnSelectView(): View {
        return View(context).apply {
            layoutParams = LayoutParams(dp5, dp5)
            background = getBgDrawable(unSelectColor).apply {
                setBounds(0, 0, dp5, dp5)
            }
            // background=ColorDrawable(Color.BLUE)
        }
    }

    private fun getBgDrawable(color: Int): Drawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_2)
        }
    }
}