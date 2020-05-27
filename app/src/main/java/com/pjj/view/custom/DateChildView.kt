package com.pjj.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils

class DateChildView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {
    var textBottom: String? = null
        set(value) {
            field = value
            invalidate()
        }

    private val sp11 = ViewUtils.getFDp(R.dimen.sp_11)
    private val paint: Paint by lazy {
        Paint().apply {
            isDither = true
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            textSize = sp11
            color = Color.WHITE
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textBottom?.let {
            val rect = Rect()
            paint.getTextBounds(it, 0, it.length, rect)
            val fontMetrics = paint.fontMetrics
            val baseLine = measuredHeight - rect.height() / 2f - ViewUtils.getFDp(R.dimen.dp_2) - fontMetrics.top / 2f - fontMetrics.bottom / 2f
            canvas.drawText(it, measuredWidth / 2f, baseLine, paint)
        }
    }
}