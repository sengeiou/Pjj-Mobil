package com.pjj.view.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2019/03/08.
 * describe：
 */
class TextImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {
    private var paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        textAlign = Paint.Align.CENTER
    }
    var text: String? = null
        set(value) {
            field = value
            if (measuredWidth > 0) {
                invalidate()
            }
        }
    var fileType: Int = 0
    var select = false
        set(value) {
            field = value
            invalidate()
        }
    private var dp1 = resources.getDimension(R.dimen.dp_1)
    private val paintLine: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            isDither = true
            color = ViewUtils.getColor(R.color.color_b5b5b5)
            //strokeWidth = dp1 * 2
            style = Paint.Style.FILL
            val array = FloatArray(2)
            array[0] = 4f
            array[1] = 4f
            pathEffect = DashPathEffect(array, 0f)
        }
    }
    var lineBottom = false
    var lineRight = false
    private var textColor = ViewUtils.getColor(R.color.color_666666)
    var textSize = resources.getDimension(R.dimen.sp_13)
        set(value) {
            field = value
            paint.textSize = textSize
        }

    init {
        paint.textSize = textSize
        paint.color = textColor
        scaleType = ScaleType.FIT_CENTER
    }

    var textHidden = false
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //Log.e("TAG", "onDraw: ${drawable == null} $text")
        if (null == drawable) {
            if (null != text) {
                if (textHidden) {

                } else {
                    val fontMetrics = paint.fontMetrics
                    canvas.drawText(text!!, measuredWidth / 2f, measuredHeight / 2f - fontMetrics.top / 2f - fontMetrics.bottom / 2f, paint)
                }
            }
        }
        if (lineRight) {
            //canvas.drawLine(measuredWidth.toFloat(), 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintLine)
            canvas.drawRect(measuredWidth - dp1, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paintLine)
        }
        if (lineBottom) {
            //canvas.drawLine(0f, measuredHeight.toFloat(), measuredWidth.toFloat(), measuredHeight.toFloat(), paintLine)
            canvas.drawRect(0f, measuredHeight - dp1, measuredWidth.toFloat(), measuredHeight.toFloat(), paintLine)
        }
        if (fileType == 2 && null != drawable && !textHidden) {//视频
            //280 80 28 8 7 2
            var drawable = ViewUtils.getDrawable(R.drawable.video)
            var side = measuredWidth * 2 / 7
            var left = (measuredWidth - side) / 2
            var top = (measuredHeight - side) / 2
            drawable.setBounds(left, top, left + side, top + side)
            drawable.draw(canvas)
        }

        if (select && null != drawable) {
            canvas.drawColor(ViewUtils.getColor(R.color.color_000000_70))
            var drawable = ViewUtils.getDrawable(R.mipmap.select_white_1)
            var side = ViewUtils.getDp(R.dimen.dp_24)
            var left = (measuredWidth - side) / 2
            var top = (measuredHeight - side) / 2
            drawable.setBounds(left, top, left + side, top + side)
            drawable.draw(canvas)
        }
    }

}