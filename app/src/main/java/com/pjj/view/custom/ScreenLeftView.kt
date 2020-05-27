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
class ScreenLeftView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
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
        //var height = MeasureSpec.getSize(heightMeasureSpec)
        rate = measuredHeight / 180f
//        var width = 89 * rate
        //setMeasuredDimension(width.toInt(), height)
        /*pathOutline.reset()
        pathOutline.moveTo(0f, 0f)
        pathOutline.lineTo(64 * rate, 0f)
        pathOutline.lineTo(89 * rate, 90 * rate)
        pathOutline.lineTo(64 * rate, measuredHeight.toFloat())
        pathOutline.lineTo(0f, measuredHeight.toFloat())
        pathOutline.close()*/
        Log.e("TAG", "onMeasure: ")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        var path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(64 * rate, 0f)
        path.lineTo(89 * rate, 90 * rate)
        path.lineTo(64 * rate, measuredHeight.toFloat())
        path.lineTo(0f, measuredHeight.toFloat())
        path.close()
        paint.color = colorBg
        canvas.drawPath(path, paint)
        Log.e("TAG", "draw: drawPath")
        var pathTriangle = Path()
        pathTriangle.moveTo(60 * rate, 34 * rate)
        pathTriangle.lineTo(74 * rate, 90 * rate)
        pathTriangle.lineTo(60 * rate, 145 * rate)
        pathTriangle.close()
        canvas.drawPath(pathTriangle, paintWhite)

        var pathLine = Path()
        pathLine.moveTo(57 * rate, 0f)
        pathLine.lineTo(60 * rate, 0f)
        pathLine.lineTo(85 * rate, 90 * rate)
        pathLine.lineTo(60 * rate, measuredHeight.toFloat())
        pathLine.lineTo(57 * rate, measuredHeight.toFloat())
        pathLine.lineTo(82 * rate, 90 * rate)
        pathLine.close()
        canvas.drawPath(pathLine, paintWhite)
    }
}