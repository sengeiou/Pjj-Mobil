package com.pjj.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/11/30.
 * describe：
 */
class ImageStyleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var color = ContextCompat.getColor(context, R.color.color_theme)
    private var colorBg = ContextCompat.getColor(context, R.color.color_f3f3f3)
    private var colorLine = ContextCompat.getColor(context, R.color.color_999999)
    private var dp1 = ViewUtils.getFDp(R.dimen.dp_1)
    private var dpHalf1 = dp1 / 2
    var selectStatue = false
        set(value) {
            field = value
            invalidate()
        }
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        isAntiAlias = true
        isDither = true
        strokeWidth = dp1
    }
    private val paintSelect: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            isDither = true
            strokeWidth = dp1
            color = this@ImageStyleView.color
            style = Paint.Style.STROKE
        }
    }


    companion object {
        val STYLE_ALL = 1
        val STYLE_HALF = 2
        val STYLE_FOUR_SPELL = 3
        val STYLE_MORE_IMAGE = 4
    }

    private var imageStyle = STYLE_ALL

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ImageStyleView, defStyleAttr, 0).run {
            (0 until indexCount).forEach {
                var index = getIndex(it)
                when (index) {
                    R.styleable.ImageStyleView_image_style -> imageStyle = getInteger(index, imageStyle)
                }
            }
            recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (imageStyle) {
            STYLE_ALL -> drawAll(canvas)
            STYLE_HALF -> drawHalf(canvas)
            STYLE_FOUR_SPELL -> drawFourSpell(canvas)
            STYLE_MORE_IMAGE -> drawMoreImage(canvas)
        }
    }

    private fun drawAll(canvas: Canvas) {
        if (selectStatue) {
            canvas.drawColor(color)
            drawSelectBitmap(canvas)
        } else {
            paint.style = Paint.Style.STROKE
            paint.color = color
            canvas.drawColor(colorBg)
            canvas.drawRect(dpHalf1, dpHalf1, measuredWidth - dpHalf1, measuredHeight - dpHalf1, paint)
        }
    }

    private fun drawHalf(canvas: Canvas) {
        //灰色边框
        paint.style = Paint.Style.STROKE
        paint.color = colorLine
        canvas.drawRect(dpHalf1, dpHalf1, measuredWidth - dpHalf1, measuredHeight - dpHalf1, paint)
        //选中区域
        paint.style = Paint.Style.FILL
        if (selectStatue) {
            paint.color = color
            canvas.drawRect(dpHalf1, dpHalf1, measuredWidth - dpHalf1, measuredHeight / 2f, paint)
            drawSelectBitmap(canvas)
        } else {
            paint.color = colorBg
            canvas.drawRect(dpHalf1, dpHalf1, measuredWidth - dpHalf1, measuredHeight / 2f, paint)
            canvas.drawRect(dpHalf1, dpHalf1, measuredWidth - dpHalf1, measuredHeight / 2f, paintSelect)
        }
    }

    private fun drawFourSpell(canvas: Canvas) {
        //灰色边框
        paint.style = Paint.Style.STROKE
        paint.color = colorLine
        canvas.drawRect(dpHalf1, dpHalf1, measuredWidth - dpHalf1, measuredHeight - dpHalf1, paint)
        //十字线条
        paint.style = Paint.Style.FILL
        var heightHalf = measuredHeight / 2f
        canvas.drawLine(dpHalf1, heightHalf, measuredWidth - dpHalf1, heightHalf, paint)
        var widthHalf = measuredWidth / 2f
        canvas.drawLine(widthHalf, dpHalf1, widthHalf, measuredHeight - dpHalf1, paint)
        //选中区域
        if (selectStatue) {
            paint.color = color
            canvas.drawRect(dpHalf1, dpHalf1, widthHalf, heightHalf, paint)
            drawSelectBitmap(canvas)
        } else {
            paint.color = colorBg
            canvas.drawRect(dpHalf1, dpHalf1, widthHalf, heightHalf, paint)
            canvas.drawRect(dpHalf1, dpHalf1, widthHalf, heightHalf, paintSelect)
        }
    }

    private fun drawMoreImage(canvas: Canvas) {
        var dp2 = ViewUtils.getDp(R.dimen.dp_2)
        var dp8 = ViewUtils.getDp(R.dimen.dp_8)
        var dpHalfInt1 = dpHalf1.toInt()
        var rect = Rect(dpHalfInt1, dpHalfInt1, measuredWidth - dp8 - dpHalfInt1, measuredHeight - dp8 - dpHalfInt1)
        paint.style = Paint.Style.FILL
        paint.color = colorBg
        //第一层
        canvas.drawRect(rect, paint)
        canvas.drawRect(rect, paintSelect)
        //剩余4层
        (1..4).forEach {
            rect.left += dp2
            rect.top += dp2
            rect.right += dp2
            rect.bottom += dp2
            if (it == 4 && selectStatue) {
                paintSelect.style = Paint.Style.FILL
                canvas.drawRect(rect, paintSelect)
                drawSelectBitmap(canvas)
            } else {
                canvas.drawRect(rect, paint)
                canvas.drawRect(rect, paintSelect)
            }
        }
    }

    private fun drawSelectBitmap(canvas: Canvas) {
        ContextCompat.getDrawable(context, R.mipmap.select_ad)?.let {
            var dp23 = ViewUtils.getDp(R.dimen.dp_23)
            var left = when (imageStyle) {
                STYLE_MORE_IMAGE -> (measuredWidth - dp23) / 2 + ViewUtils.getDp(R.dimen.dp_8)
                else -> (measuredWidth - dp23) / 2
            }
            var top = when (imageStyle) {
                STYLE_MORE_IMAGE -> (measuredHeight - dp23) / 2 + ViewUtils.getDp(R.dimen.dp_8)
                else -> (measuredHeight - dp23) / 2
            }
            it.setBounds(left, top, left + dp23, top + dp23)
            it.draw(canvas)
        }
    }
}