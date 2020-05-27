package com.pjj.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/18.
 * describe：
 */
class ArcSlideViewGroup @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private var radius = ViewUtils.getDp(R.dimen.dp_125)
    private var radiusChild = ViewUtils.getDp(R.dimen.dp_31)

    private var colorBig = Color.parseColor("#A1B5D1E9")

    init {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = MeasureSpec.makeMeasureSpec(radius * 2 + radiusChild * 2, MeasureSpec.EXACTLY)
        var heightMeasureSpec = MeasureSpec.makeMeasureSpec(radius + radiusChild, MeasureSpec.EXACTLY)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setChildCount(vararg array: String) {
        array.forEach {
            addView(getChildView(it))
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    private var sp13 = ViewUtils.getFDp(R.dimen.sp_13)
    private fun getChildView(s: String): TextView {
        return TextView(context).apply {
            var i = radiusChild * 2
            layoutParams = ViewGroup.LayoutParams(i, i)
            background = getChildDrawable()
            text = s
            setTextSize(TypedValue.COMPLEX_UNIT_PX, sp13)
            setTextColor(Color.WHITE)
        }
    }

    private fun getChildDrawable(): Drawable {
        return ViewUtils.getDrawable(R.drawable.gradient_color_135_bg)
    }
}