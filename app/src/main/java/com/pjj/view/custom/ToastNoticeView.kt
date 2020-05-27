package com.pjj.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils


class ToastNoticeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = TextPaint().apply {
        isDither = true
        isAntiAlias = true
        textSize = ViewUtils.getFDp(R.dimen.sp_12)
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
    }
    var tranX: Int = 0
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }
    private val colorDefault = ViewUtils.getColor(R.color.color_707070_10)
    private val paintBg = Paint().apply {
        isDither = true
        isAntiAlias = true
        color = colorDefault
        style = Paint.Style.FILL
    }
    var bgColor: Int = colorDefault
        set(value) {
            field = value
            paintBg.color = value
        }
    var text: String? = null
        set(value) {
            field = value
            requestLayout()
        }
    private val bottomPadding = ViewUtils.getFDp(R.dimen.dp_6)
    private val dp11 = ViewUtils.getDp(R.dimen.dp_11)
    private val dp9 = ViewUtils.getDp(R.dimen.dp_9)
    private val dp4 = ViewUtils.getDp(R.dimen.dp_4)
    private var textHeightLine = 0f
    private var textHeight = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
//            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
//            return
//        }
        var height = 0f
        var width = 0
        text?.let {
            val rect = Rect()
            val array = it.split("\n")
            var maxWidth = 0
            array.forEach { item ->
                paint.getTextBounds(item, 0, item.length, rect)
                maxWidth = Math.max(maxWidth, rect.width())
                textHeight = Math.max(textHeight, rect.height())
            }
            textHeightLine = textHeight / 2f + dp9
            width = maxWidth + dp11 * 2
            val textSize = array.size
            height = textHeight * textSize + (textSize - 1) * dp4 + dp9 * 2 + bottomPadding
        }
        setMeasuredDimension(width, height.toInt())
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT)
        super.onDraw(canvas)
        text?.let {
            val baseLine = textHeightLine - paint.fontMetrics.bottom / 2f - paint.fontMetrics.top / 2f
            var bottom = measuredHeight - bottomPadding
            canvas.drawRoundRect(0f, 0f, measuredWidth.toFloat(), bottom, bottomPadding, bottomPadding, paintBg)
            val path = Path()
            path.moveTo(measuredWidth / 2f + tranX, measuredHeight.toFloat())
            path.lineTo(measuredWidth / 2f - bottomPadding / 2 + tranX, bottom)
            path.lineTo(measuredWidth / 2f + bottomPadding / 2 + tranX, bottom)
            path.close()
            canvas.drawPath(path, paintBg)
//            val sl = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                StaticLayout.Builder.obtain(it, 0, it.length, paint, measuredWidth).build()
//            } else {
//                StaticLayout(it, paint, measuredWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true)
//            }
            val array = it.split("\n")
            var line = baseLine
            array.forEachIndexed { index, item ->
                line = textHeight * index + dp4 * index + baseLine
                canvas.drawText(item, measuredWidth / 2f, line, paint)
            }
        }
    }
}