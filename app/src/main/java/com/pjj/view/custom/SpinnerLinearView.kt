package com.pjj.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/03.
 * describe：
 */
class SpinnerLinearView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var textColor = ContextCompat.getColor(context, R.color.color_555555)
    private var textSelectBg = ContextCompat.getColor(context, R.color.color_f7f5f5)
    private var selectPosition: Int = -1

    init {
        orientation = LinearLayout.VERTICAL
    }

    fun setTextList(list: List<Int>, defaultPosition: Int = -1) {
        list.forEachIndexed { index, it ->
            var view = createTitleView(it)
            view.tag = index
            if (selectPosition == index) {
                view.background = ColorDrawable(textSelectBg)
            }
            addView(view)
        }
        if (defaultPosition > -1) {
            selectPosition = defaultPosition
            setSelectStatue(defaultPosition)
        }
    }


    private fun createTitleView(s: Int): TextView {
        return TextView(context).apply {
            text = if (s >= 1000) {
                "${s / 1000}km"
            } else {
                "${s}m"
            }
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
            gravity = Gravity.CENTER
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
            setTextColor(textColor)
            setOnClickListener(onClick)
        }
    }

    private var onClick = View.OnClickListener {
        var position = it.tag as Int
        if (position != selectPosition) {
            var content = setSelectStatue(position)
            if (selectPosition != -1)
                setUnSelectStatue(selectPosition)
            selectPosition = position
            onSelectListener?.select(selectPosition, content)
        }else{
            onSelectListener?.dismiss()
        }
    }

    private fun setSelectStatue(position: Int): String {
        if (position < childCount) {
            var childAt = getChildAt(position)
            childAt.background = ColorDrawable(textSelectBg)
            return (childAt as TextView).text.toString()
        }
        return ""
    }

    private fun setUnSelectStatue(position: Int) {
        if (position < childCount) {
            var childAt = getChildAt(position)
            childAt.background = ColorDrawable(Color.TRANSPARENT)
        }
    }

    var onSelectListener: OnSelectListener? = null

    interface OnSelectListener {
        fun select(position: Int, content: String)
        fun dismiss()
    }

    fun creatIntList(): List<Int> {
        var list = ArrayList<Int>(5)
        list.add(500)
        list.add(1000)
        list.add(2000)
        list.add(5000)
        list.add(10000)
        return list
    }
}