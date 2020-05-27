package com.pjj.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils
import android.graphics.Shader.TileMode


/**
 * Created by XinHeng on 2019/01/03.
 * describe：
 */
class NewGuideView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var paint: Paint = Paint()
    private var paint1: Paint
    private var paint2: Paint

    init {

        paint.isDither = true
        paint.isAntiAlias = true
        paint1 = Paint(paint)
        paint2 = Paint(paint)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        /*
//        LinearGradient(0f, 320f, 0f, (320 + 100).toFloat(), intArrayOf(Color.RED, Color.GREEN, Color.BLUE), floatArrayOf(0f, 0.1f, 0.5f), TileMode.MIRROR)
        var width = measuredHeight.toFloat()
        var width_10=width/6
        var fl = width / 5*2
        var linearGradient = LinearGradient(0f, 0f, 0f, fl,
                intArrayOf(ViewUtils.getColor(R.color.color_707070_10), ViewUtils.getColor(R.color.color_40bbf7)
                       *//* ViewUtils.getColor(R.color.color_40bbf7),ViewUtils.getColor(R.color.color_707070_10)*//*),
                null, TileMode.CLAMP)
        var linearGradient1 = LinearGradient(0f, measuredHeight-fl, 0f, measuredHeight.toFloat(),
                intArrayOf(ViewUtils.getColor(R.color.color_40bbf7), ViewUtils.getColor(R.color.color_707070_10)
                        *//* ViewUtils.getColor(R.color.color_40bbf7),ViewUtils.getColor(R.color.color_707070_10)*//*),
                null, TileMode.CLAMP)
        //floatArrayOf(0.05f, 0.05f, 0.1f)
        paint.shader = linearGradient
        //paint.style=Paint.Style.STROKE
        paint.strokeWidth=30f
        canvas.drawRect(Rect(0, 0, measuredWidth, fl.toInt()), paint)
        paint1.shader=linearGradient1
        canvas.drawRect(Rect(0, measuredHeight-fl.toInt(), measuredWidth, measuredHeight), paint1)
        var rx = measuredWidth / 2f
        var mRadialGradient = RadialGradient(rx, rx, rx, intArrayOf(Color.WHITE, ViewUtils.getColor(R.color.color_40bbf7),ViewUtils.getColor(R.color.color_707070_10)),
                null, Shader.TileMode.CLAMP)
        paint2.shader=mRadialGradient
        paint2.strokeWidth=dp10
        //paint2.style=Paint.Style.STROKE
        canvas.drawCircle(rx, rx, rx-dp10, paint2)
        canvas.drawArc(rectF,0f,-90f,true,paint2)*/
        //canvas.drawRoundRect(RectF(0f,0f,measuredWidth.toFloat(),measuredHeight.toFloat()), 30f, 30f, paint2)
        var dp10 = ViewUtils.getFDp(R.dimen.dp_10)
        var rectF = RectF(0f, 0f, dp10 * 2, dp10 * 2)
        //左上角 弧形
        var mRadialGradientLeftTop = RadialGradient(dp10, dp10, dp10, intArrayOf(ViewUtils.getColor(R.color.color_theme), ViewUtils.getColor(R.color.color_707070_10)), null, Shader.TileMode.REPEAT)
        paint2.shader = mRadialGradientLeftTop
        canvas.drawArc(rectF, -180f, 90f, true, paint2)
        //top 横线
        var rectTop1 = Rect(dp10.toInt(), 0, (measuredWidth - dp10).toInt(), dp10.toInt())
        var linearGradient = LinearGradient(0f, 0f, 0f, dp10, intArrayOf(ViewUtils.getColor(R.color.color_707070_10), ViewUtils.getColor(R.color.color_theme)), null, TileMode.CLAMP)
        paint.shader = linearGradient
        canvas.drawRect(rectTop1, paint)
        //又上角 弧形
        var mRadialGradientRightTop = RadialGradient(dp10, dp10, dp10, intArrayOf(ViewUtils.getColor(R.color.color_theme), ViewUtils.getColor(R.color.color_707070_10)), null, Shader.TileMode.REPEAT)
        paint2.shader = mRadialGradientLeftTop
        var rectFRightTop = RectF(measuredWidth - dp10 * 2, 0f, measuredWidth.toFloat(), dp10 * 2)
        canvas.drawArc(rectFRightTop, -90f, 90f, true, paint2)
        //right 横线
        var rectRight = Rect(rectTop1.right, dp10.toInt(), measuredWidth, (measuredHeight - dp10 * 2).toInt())
        paint.shader =
                LinearGradient(measuredWidth.toFloat(), dp10, measuredWidth.toFloat()-dp10, dp10, intArrayOf(ViewUtils.getColor(R.color.color_707070_10), ViewUtils.getColor(R.color.color_theme)), null, TileMode.CLAMP)
        canvas.drawRect(rectRight, paint)
    }

}