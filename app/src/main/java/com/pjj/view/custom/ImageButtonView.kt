package com.pjj.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.pjj.R

/**
 * Created by XinHeng on 2019/02/28.
 * describe：
 */
class ImageButtonView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var drawable: Drawable? = null
    private var drawablePadding = 0
    private var textSize = 30
    private var text: String? = null
    private var textColor = Color.BLACK
    private var drawableWidth = 0
    private var drawableHeight = 0
    private var textSplitTag = false
    private var paint = Paint().apply {
        isDither = true
        isAntiAlias = true
    }

    init {
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageButtonView, defStyleAttr, 0)
        (0..typedArray.indexCount).forEach {
            var index = typedArray.getIndex(it)
            when (index) {
                R.styleable.ImageButtonView_button_left_drawable -> drawable = typedArray.getDrawable(index)
                R.styleable.ImageButtonView_button_text -> text = typedArray.getString(index)
                R.styleable.ImageButtonView_button_text_color -> textColor = typedArray.getColor(index, textColor)
                R.styleable.ImageButtonView_button_text_size -> textSize = typedArray.getDimensionPixelSize(index, textSize)
                R.styleable.ImageButtonView_button_left_drawable_width -> drawableWidth = typedArray.getDimensionPixelSize(index, drawableWidth)
                R.styleable.ImageButtonView_button_left_drawable_height -> drawableHeight = typedArray.getDimensionPixelSize(index, drawableHeight)
                R.styleable.ImageButtonView_button_left_drawable_padding -> drawablePadding = typedArray.getDimensionPixelSize(index, drawablePadding)
                R.styleable.ImageButtonView_button_text_split_tag -> textSplitTag = typedArray.getBoolean(index, textSplitTag)
            }
        }
        typedArray.recycle()
        paint.textSize = textSize.toFloat()
        paint.color = textColor

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawable?.let {
            val top = (measuredHeight - drawableHeight) / 2
            it.setBounds(paddingLeft, top, paddingLeft + drawableWidth, top + drawableHeight)
            it.draw(canvas)
        }
        text?.let {
            val fontMetrics = paint.fontMetrics
            val baseLine = measuredHeight / 2f - fontMetrics.top / 2 - fontMetrics.bottom / 2
            if (!textSplitTag) {
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText(text, (measuredWidth - paddingRight).toFloat(), baseLine, paint)
            } else {
                //为了英文的扩展
                paint.textAlign = Paint.Align.CENTER
                val startX: Float = (paddingLeft + drawableWidth + drawablePadding).toFloat()
                val textLengthSum = measuredWidth - paddingRight - startX
                val rect = Rect()
                paint.getTextBounds("蓝", 0, 1, rect)
                val textOneLength = rect.width()
                val textOneLengthHale = textOneLength / 2f
                val length = it.length
                val textLength = textOneLength * length
                val textPadding = (textLengthSum - textLength) / (length - 1f)
                var x: Float
                (0 until length).forEach { index ->
                    x = startX + index * textOneLength + index * textPadding
                    canvas.drawText(it[index].toString(), x + textOneLengthHale, baseLine, paint)
                }
            }
        }
    }
}