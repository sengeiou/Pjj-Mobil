package com.pjj.view.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2019/01/12.
 * describe：
 */
class LineProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var startBgColor = Color.parseColor("#8fd8fb")
    private var endBgColor = Color.parseColor("#028ccf")

    private var circleStartColor = Color.parseColor("#949596")
    private var circleEndColor = Color.parseColor("#e1e4e5")

    private var sideColor = Color.parseColor("#bfbfbf")
    private var bgColor = Color.parseColor("#f2f2f2")

    private var circleRadius: Float = 0f
    private var progressHeight: Float = ViewUtils.getFDp(R.dimen.dp_20)
    private var progressWidth: Float = 0f
    private var paddingTextProgress = ViewUtils.getDp(R.dimen.dp_10)
    private var progressTextSize = ViewUtils.getFDp(R.dimen.sp_17)

    private var paintBg = createPaint()
    private val paintProgress: Paint by lazy {
        createPaint().apply {
            style = Paint.Style.FILL
            shader = LinearGradient(0f, 0f, 0f, measuredHeight.toFloat(), intArrayOf(startBgColor, endBgColor), null, Shader.TileMode.CLAMP)
        }
    }
    private val paintText: Paint by lazy {
        createPaint().apply {
            style = Paint.Style.FILL
            color = endBgColor
            textSize = progressTextSize
            textAlign = Paint.Align.CENTER
        }
    }
    private var circleBitmap: Bitmap? = null
    private val dp1 = ViewUtils.getFDp(R.dimen.dp_1)
    var progress: Float = 0f
        set(value) {
            field = value
            if (width > 0) {
                invalidate()
            }
        }

    private fun createCircleBitmap(radius: Float, emptyRadius: Float): Bitmap {
        var bitmap = Bitmap.createBitmap((radius * 2).toInt(), (radius * 2).toInt(), Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT)
        var paint = createPaint()
        paint.shader = LinearGradient(0f, 0f, 0f, radius, intArrayOf(circleStartColor, circleEndColor), null, Shader.TileMode.MIRROR)
        paint.style = Paint.Style.FILL
        canvas.drawCircle(radius, radius, radius, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawCircle(radius, radius, emptyRadius, paint)
        return bitmap
    }

    private fun createPaint(): Paint {
        return Paint().apply {
            isAntiAlias = true
            isDither = true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        progressWidth = (width - paddingLeft - paddingRight).toFloat()
        circleRadius = progressHeight / 2 + dp1 * 2f
        if (circleBitmap != null) {
            if (circleBitmap!!.width != circleRadius.toInt()) {
                circleBitmap = createCircleBitmap(circleRadius, circleRadius - progressHeight / 4.5f)
            }
        } else {
            if (circleRadius > 0)
                circleBitmap = createCircleBitmap(circleRadius, circleRadius - progressHeight / 4.5f)
        }
        paintText.textSize = progressTextSize
        var rect = Rect()
        var s = "100%"
        paintText.getTextBounds(s, 0, s.length, rect)
        var height = rect.height() + paddingTop + paddingBottom + paddingTextProgress + progressHeight
        var padding = (rect.width() / 2 - circleRadius).toInt() + 2
        setPadding(padding, paddingTop, padding, paddingBottom)
        setMeasuredDimension(width, height.toInt())

    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)
        //背景
        var radius = progressHeight / 2f
        var drawableBg = GradientDrawable().apply {
            setColor(bgColor)
            setStroke(dp1.toInt(), sideColor)
            cornerRadius = radius
        }
        drawableBg.setBounds(paddingLeft, (measuredHeight - paddingBottom - progressHeight - dp1).toInt(), (progressWidth + paddingLeft).toInt(), (measuredHeight - dp1 - paddingBottom).toInt())
        //background = drawableBg
        drawableBg.draw(canvas)
        //进度
        var right = progressWidth * progress
        if (right > 0) {
            var rectFProgress = RectF(paddingLeft.toFloat(), measuredHeight - progressHeight - dp1, right + paddingLeft, measuredHeight - dp1)
            canvas.drawRoundRect(rectFProgress, radius, radius, paintProgress)
            //圆环
            canvas.drawBitmap(circleBitmap, paddingLeft + right - circleRadius * 2f + 1, (measuredHeight - circleBitmap!!.height).toFloat(), paintBg)
        }
        //汉字 进度
        var fontMetrics = paintText.fontMetrics
        var baseLine = progressHeight / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2
        canvas.drawText("${(progress * 100).toInt()}%", right - circleRadius + paddingLeft, baseLine, paintText)
    }
}