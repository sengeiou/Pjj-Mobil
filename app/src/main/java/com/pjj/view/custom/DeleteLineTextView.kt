package com.pjj.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import com.pjj.R

class DeleteLineTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private var noDeleteText: String? = null
    private var noDeleteTextColor = Color.GRAY

    init {
        paint.isAntiAlias = true
        paint.isDither = true
        paint.strokeWidth = 2f
        var typedArray = context.obtainStyledAttributes(attrs, R.styleable.DeleteLineTextView, defStyleAttr, 0)
        (0 until typedArray.indexCount).forEach {
            var index = typedArray.getIndex(it)
            when (index) {
                R.styleable.DeleteLineTextView_text_no_delete -> noDeleteText = typedArray.getString(index)
                R.styleable.DeleteLineTextView_text_no_delete_color -> noDeleteTextColor = typedArray.getColor(index, noDeleteTextColor)
            }
        }
        typedArray.recycle()
    }

    fun setNoDeleteText(text: String, color: Int) {
        this.noDeleteText = text
        noDeleteTextColor = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.textSize = textSize
        var noDeleteLength = noDeleteText?.let {
            paint.measureText(it)
        } ?: 0f
        var y = measuredHeight / 2f
        canvas.drawLine(paddingLeft + noDeleteLength, y, (measuredWidth - paddingRight).toFloat(), y, paint)
    }
}
