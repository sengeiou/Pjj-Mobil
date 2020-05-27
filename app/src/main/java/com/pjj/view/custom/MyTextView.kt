package com.pjj.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils

class MyTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = TextPaint().apply {
        isDither = true
        isAntiAlias = true
        strokeWidth = ViewUtils.getFDp(R.dimen.dp_1)
        typeface = Typeface.createFromAsset(context.assets, "SOURCEHANSANSCN-REGULAR.TTF")
    }
    var text: String? = null
    var textSize: Float = 20f
        set(value) {
            field = value
            paint.textSize = value
        }
    var color: Int = Color.WHITE
        set(value) {
            field = value
            paint.color = value
        }
    var gravity: String = "1"// Gravity.CENTER //对齐 0 左 1中 2右 3上左 4上中 5上右 6下左 7下中 8下右
    private var hint: String? = null
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e("TAG", "measure: height=$measuredHeight")

    }

    @SuppressLint("RtlHardcoded", "DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val text = if (null != text) text else hint
        if (!TextUtils.isEmpty(text)) {
            paint.textSize = textSize
            //val rect = Rect()
            //paint.getTextBounds(text!!, 0, text.length, rect)
            //Log.e("TAG", "height=${rect.height()}  viewHeight=$measuredHeight")
            var alignment = Layout.Alignment.ALIGN_CENTER
            canvas.save()
            var gravityV = 0// 0top 1 center 2bottom
            paint.textAlign = when (gravity) {//0 左 1中 2右 3上左 4上中 5上右 6下左 7下中 8下右
                "2" -> {
                    gravityV = 1
                    Paint.Align.RIGHT
                }
                "0" -> {
                    gravityV = 1
                    alignment = Layout.Alignment.ALIGN_NORMAL
                    Paint.Align.LEFT
                }
                "1" -> {
                    gravityV = 1
                    alignment = Layout.Alignment.ALIGN_CENTER
                    Paint.Align.LEFT
                }
                "4" -> {
                    gravityV = 0
                    alignment = Layout.Alignment.ALIGN_CENTER
                    Paint.Align.LEFT
                }
                "7" -> {
                    gravityV = 2
                    alignment = Layout.Alignment.ALIGN_CENTER
                    Paint.Align.LEFT
                }
                "3" -> {//上左
                    gravityV = 0
                    alignment = Layout.Alignment.ALIGN_NORMAL
                    Paint.Align.LEFT
                }
                "5" -> {//上右
                    gravityV = 0
                    alignment = Layout.Alignment.ALIGN_NORMAL
                    Paint.Align.RIGHT
                }
                "6" -> {//下左
                    gravityV = 2
                    alignment = Layout.Alignment.ALIGN_NORMAL
                    Paint.Align.LEFT
                }
                else /*"8"*/ -> {
                    gravityV = 2
                    alignment = Layout.Alignment.ALIGN_NORMAL
                    Paint.Align.RIGHT
                }
            }
            val dp = ViewUtils.getFDp(R.dimen.dp_2)
            val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder.obtain(text!!, 0, text!!.length, paint, measuredWidth)
                        .setAlignment(alignment)
                        .setIncludePad(false)
                        .setLineSpacing(dp, 1f)
                        .build()
            } else {
                StaticLayout(text!!, paint, measuredWidth, alignment, 1f, dp, true)
            }
            val height = staticLayout.height
            val dy = when (gravityV) {
                0 -> 0f
                1 -> (measuredHeight - height) / 2f
                else -> measuredHeight * 1f - height
            }
            canvas.translate(0f, dy)
            staticLayout.draw(canvas)
            canvas.restore()
        }
    }

    fun setHintText(s: String?) {
        hint = s
        invalidate()
    }
}