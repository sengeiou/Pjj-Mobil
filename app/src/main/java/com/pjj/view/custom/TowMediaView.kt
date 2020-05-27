package com.pjj.view.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2019/04/02.
 * describe：
 */
class TowMediaView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        orientation = VERTICAL
        background = ColorDrawable(Color.TRANSPARENT)
        addView(TextImageView(context).apply {
            text = "视频"
            fileType = 2
            background = ColorDrawable(ViewUtils.getColor(R.color.color_f2f2f2))
            scaleType = ImageView.ScaleType.FIT_CENTER
            textHidden = true
            setImageDrawable(getTextDrawableBg(R.mipmap.video_tag, "视频"))
        }, getLp())
        addView(TextImageView(context).apply {
            text = "图片"
            fileType = 1
            textHidden = true
            background = ColorDrawable(ViewUtils.getColor(R.color.color_e3e3e3))
            setImageDrawable(getTextDrawableBg(R.mipmap.image_tag, "图片"))
            scaleType = ImageView.ScaleType.FIT_CENTER
        }, getLp())
    }

    private fun getLp(): LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
    }

    fun getFirstView(): TextImageView {
        return getChildAt(0) as TextImageView
    }

    fun getSecond(): ImageView {
        return getChildAt(1) as ImageView
    }

    var onTowMediaViewListener: OnTowMediaViewListener? = null
        set(value) {
            field = value
            getFirstView().setOnClickListener {
                onTowMediaViewListener?.firstClick()
            }
            getSecond().setOnClickListener {
                onTowMediaViewListener?.secondClick()
            }
        }

    companion object {
        @JvmStatic
        fun getDrawableBg(res: Int, text: String, startColor: Int, endColor: Int): Drawable {

            return object : Drawable() {
                private var paint = Paint().apply {
                    isAntiAlias = true
                    isDither = true
                    style = Paint.Style.FILL
                }
                private var paintText = Paint().apply {
                    isAntiAlias = true
                    isDither = true
                    style = Paint.Style.FILL
                    color = ViewUtils.getColor(R.color.color_333333)
                    textAlign = Paint.Align.CENTER
                }
                private val sp14 = ViewUtils.getFDp(R.dimen.sp_14)
                private val dp14 = ViewUtils.getFDp(R.dimen.dp_14)
                override fun draw(canvas: Canvas) {
                    val width = bounds.width()
                    val height = bounds.height()
                    val gradient = LinearGradient(0f, 0f, 0f, height.toFloat(), startColor, endColor, Shader.TileMode.CLAMP)
                    paint.shader = gradient
                    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
                    val drawable = ViewUtils.getDrawable(res)
                    val rate = width * 1f / 259
                    val widthDrawable = rate * 33
                    val heightDrawable = widthDrawable * drawable.intrinsicHeight / drawable.intrinsicWidth
                    val padding = rate * 14
                    val left = ((width - widthDrawable) / 2).toInt()
                    paintText.textSize = (width / ViewUtils.getDp(R.dimen.dp_259)) * sp14
                    val rect = Rect()
                    paintText.getTextBounds(text, 0, text.length, rect)
                    val top = ((height - heightDrawable - padding - rect.height()) / 2).toInt()
                    drawable.setBounds(left, top, left + widthDrawable.toInt(), top + heightDrawable.toInt())
                    drawable.draw(canvas)
                    val fm = paintText.fontMetrics
                    val baseLine = top + padding + heightDrawable + rect.height() / 2f - fm.bottom / 2f - fm.top / 2f
                    canvas.drawText(text, width / 2f, baseLine, paintText)
                    Log.e("TAG", "width=$width height=$height")
                    setBounds(0, 0, width, height)
                }

                override fun setAlpha(alpha: Int) {
                    paint.alpha = alpha
                }

                override fun getOpacity(): Int {
                    return PixelFormat.TRANSLUCENT
                }

                override fun setColorFilter(colorFilter: ColorFilter?) {
                    paint.colorFilter = colorFilter
                }
            }
        }

        @JvmStatic
        fun getTextDrawableBg(res: Int, text: String): Drawable {
            return object : Drawable() {
                private var paint = Paint().apply {
                    isAntiAlias = true
                    isDither = true
                    style = Paint.Style.STROKE
                    strokeWidth=ViewUtils.getFDp(R.dimen.dp_1)
                }

                override fun draw(canvas: Canvas) {
                    canvas.drawColor(ViewUtils.getColor(R.color.color_f5f8fd))
                    val dp75 = ViewUtils.getFDp(R.dimen.dp_75)
                    val dp11 = ViewUtils.getFDp(R.dimen.dp_11)
                    val radius = dp75 / 2
                    paint.color = ViewUtils.getColor(R.color.color_d3e6fb)
                    val half = bounds.width() / 2f
                    canvas.drawCircle(half, radius + dp11, radius, paint)
                    paint.style=Paint.Style.FILL
                    val img = ViewUtils.getDrawable(res)
                    val left = ViewUtils.getDp(R.dimen.dp_62)
                    val top = ViewUtils.getDp(R.dimen.dp_30)
                    val width = ViewUtils.getDp(R.dimen.dp_22)
                    val height = ViewUtils.getDp(R.dimen.dp_16)
                    img.setBounds(left, top, left + width, top + height)
                    img.draw(canvas)
                    paint.color = ViewUtils.getColor(R.color.color_theme)
                    paint.textSize = ViewUtils.getFDp(R.dimen.sp_12)
                    val fm = paint.fontMetrics
                    val rect = Rect()
                    paint.getTextBounds(text, 0, text.length, rect)
                    val baseLine = rect.height() / 2f + ViewUtils.getFDp(R.dimen.dp_56) - fm.top / 2f - fm.bottom / 2f
                    paint.textAlign = Paint.Align.CENTER
                    canvas.drawText("上传$text", half, baseLine, paint)
                    paint.color = ViewUtils.getColor(R.color.color_999999)
                    paint.textSize = ViewUtils.getFDp(R.dimen.sp_9)
                    val hint1 = "建议尺寸1080*900"
                    paint.getTextBounds(hint1, 0, hint1.length, rect)
                    val base1 = rect.height() / 2f + ViewUtils.getFDp(R.dimen.dp_91) - fm.top / 2f - fm.bottom / 2f
                    canvas.drawText(hint1, half, base1, paint)
                    val textHint = rect.height() + 3
                    val hint2 = text + "大小不能超过20M"
                    paint.getTextBounds(hint1, 0, hint2.length, rect)
                    val base2 = rect.height() / 2f + ViewUtils.getFDp(R.dimen.dp_93) - fm.top / 2f - fm.bottom / 2f
                    canvas.drawText(hint2, half, base2 + textHint, paint)
                }

                override fun setAlpha(alpha: Int) {
                    paint.alpha = alpha
                }

                override fun getOpacity(): Int {
                    return PixelFormat.TRANSLUCENT
                }

                override fun setColorFilter(colorFilter: ColorFilter?) {
                    paint.colorFilter = colorFilter
                }

            }
        }
    }

    interface OnTowMediaViewListener {
        fun firstClick()
        fun secondClick()
    }
}