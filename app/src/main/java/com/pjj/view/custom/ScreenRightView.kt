package com.pjj.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2019/02/14.
 * describe：
 */
class ScreenRightView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var colorBg = Color.parseColor("#F61212")
    var pathOutline: Path = Path()
    private var paint = Paint().apply {
        isDither = true
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private var paintWhite = Paint(paint).apply {
        color = Color.WHITE
    }
    private var rate = 1f
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        rate = measuredHeight / 180f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        var path = Path()
        path.moveTo(17 * rate, 0f)
        path.lineTo(measuredWidth.toFloat(), 0f)
        path.lineTo(measuredWidth.toFloat(), measuredHeight.toFloat())
        path.lineTo(17 * rate, measuredHeight.toFloat())
        path.lineTo(40 * rate, 90 * rate)
        path.close()
        paint.color = colorBg
        canvas.drawPath(path, paint)
        Log.e("TAG", "draw: drawPath")

        var pathLine = Path()
        pathLine.moveTo(21 * rate, 0f)
        pathLine.lineTo(24 * rate, 0f)
        pathLine.lineTo(47 * rate, 90 * rate)
        pathLine.lineTo(24 * rate, measuredHeight.toFloat())
        pathLine.lineTo(21 * rate, measuredHeight.toFloat())
        pathLine.lineTo(44 * rate, 90 * rate)
        pathLine.close()
        canvas.drawPath(pathLine, paintWhite)
    }
}