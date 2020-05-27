package com.pjj.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.CalculateUtils
import com.pjj.utils.ViewUtils

class PriceTextView : TextView {

    private var first: String? = null
    private var second = ""
    var pointSize = 20f
        set(value) {
            field = value
            paint.textSize = value
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setPrice(price: Float) {
        val m1 = CalculateUtils.m1(price)
        val split = m1.split(".")
        first = "¥" + split[0] + "."
        second = if (split.size == 2) split[1] else ""
        if (width > 0) {
            invalidate()
        }
    }

    private val paint = Paint().apply {
        isDither = true
        isAntiAlias = true
        color = ViewUtils.getColor(R.color.color_ff4c4c)
    }

    init {
        pointSize = ViewUtils.getFDp(R.dimen.sp_12)
        setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
        //paint.setColorFilter(textColors)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (null == first) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            val paintFirst = getPaint()
            val rectFirst = Rect()
            val rectSecond = Rect()
            paintFirst.getTextBounds(first, 0, first!!.length, rectFirst)
            if (second.isNotEmpty()) {
                paint.getTextBounds(second, 0, second.length, rectSecond)
            }
            val height = if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) MeasureSpec.getSize(heightMeasureSpec) else (rectFirst.height() + paddingTop + paddingBottom)
            setMeasuredDimension(rectFirst.width() + rectSecond.width() + paddingLeft + paddingRight, height)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (null == first) {
            super.onDraw(canvas)
        } else {
            val paintFirst = getPaint()
            paintFirst.color = paint.color
            val rectFirst = Rect()
            val rectSecond = Rect()
            val fm = paintFirst.fontMetrics
            paintFirst.getTextBounds(first, 0, first!!.length, rectFirst)
            val baseLine = rectFirst.height() / 2f - fm.top / 2f - fm.bottom / 2f
            paintFirst.textAlign = Paint.Align.RIGHT
            val secondTag = second.isNotEmpty()
            if (secondTag) {
                paint.getTextBounds(second, 0, second.length, rectSecond)
            }
            val x = (measuredWidth - paddingLeft - paddingRight - rectFirst.width() - rectSecond.width()) / 2f + rectFirst.width() + paddingLeft
            canvas.drawText(first, x, baseLine, paintFirst)
            if (secondTag) {
                canvas.drawText(second, x, baseLine, paint)
            }
        }
    }
}