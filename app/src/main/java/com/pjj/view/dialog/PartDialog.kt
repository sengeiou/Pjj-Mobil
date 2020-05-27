package com.pjj.view.dialog

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.CardView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * 局部弹窗 放popuwindow效果
 */
class PartDialog(context: Context) : PopupWindow(context) {
    private val dp120 = ViewUtils.getDp(R.dimen.dp_120)
    private val dp38 = ViewUtils.getDp(R.dimen.dp_38)
    private val dp10 = ViewUtils.getDp(R.dimen.dp_10)
    private val dp1 = ViewUtils.getDp(R.dimen.dp_1)

    init {
        width = dp120
        height = WindowManager.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val ll = LinearLayout(context).apply {
            setPadding(dp10, dp10, dp10, 0)
            background = getArrowBackground()
            orientation = LinearLayout.VERTICAL
            //1地方自营 2联合运营 3 公司运营 4自用
            addView(createTextView(context, "地方自营").apply {
                setOnClickListener {
                    onPartDialogListener?.select("地方自营", "1")
                    dismiss()
                }
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp1)
                background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
            })
            addView(createTextView(context, "自用").apply {
                setOnClickListener {
                    onPartDialogListener?.select("自用", "4")
                    dismiss()
                }
            })
        }
        contentView = CardView(context).apply {
            addView(ll)
            radius = 0f
            cardElevation = dp10 / 2f
//            useCompatPadding = true
//            preventCornerOverlap = false
//            radius = 0f
//            setCardBackgroundColor(Color.TRANSPARENT)
        }
        isOutsideTouchable = true
        isFocusable = true
    }

    private fun createTextView(context: Context, content: String): TextView {
        return TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp38)
            text = content
            gravity = Gravity.CENTER
            setTextColor(ViewUtils.getColor(R.color.color_555555))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
        }
    }

    var onPartDialogListener: OnPartDialogListener? = null


    interface OnPartDialogListener {
        fun select(index: String, value: String)
    }

    fun show(view: View, x: Int) {
        showAsDropDown(view, x, 0, Gravity.TOP or Gravity.END)
    }

    private fun getArrowBackground(): Drawable {
        return object : Drawable() {
            var fl = ViewUtils.getFDp(R.dimen.dp_1)
            private var paint = Paint().apply {
                isAntiAlias = true
                isDither = true
                style = Paint.Style.STROKE
                color = ViewUtils.getColor(R.color.color_f1f1f1)
                strokeWidth = fl
            }
            private var paintWhite = Paint().apply {
                isAntiAlias = true
                isDither = true
                style = Paint.Style.FILL
                color = Color.WHITE
                strokeWidth = fl
            }

            override fun draw(canvas: Canvas) {
//                canvas.drawColor(Color.WHITE)
                val dp5 = ViewUtils.getFDp(R.dimen.dp_5)
                val dp8 = ViewUtils.getFDp(R.dimen.dp_8)
                //val dp3 = ViewUtils.getDp(R.dimen.dp_3)
                val drawable = GradientDrawable().apply {
                    setColor(Color.WHITE)
                    cornerRadius = ViewUtils.getFDp(R.dimen.dp_3)
                    setStroke(dp1, ViewUtils.getColor(R.color.color_f1f1f1))
                    setBounds(0, dp10, dp120, dp38 * 2 + dp10 )
                }
                /*var intArray = IntArray(2)
                intArray[0] = ViewUtils.getColor(R.color.color_f1f1f1)
                val drawableBg = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArray).apply {
                    //setStroke(dp1, ViewUtils.getColor(R.color.color_f1f1f1))
                    setBounds(dp10 / 2, dp10, dp120, dp38 * 2 + dp10)
                }*/
                //背景 圆角
                //drawableBg.draw(canvas)
                drawable.draw(canvas)
                //三角
                val path = Path()
                path.moveTo(dp120 - dp5 - dp8 * 2, dp10.toFloat())
                path.lineTo(dp120 - dp5 - dp8, 0f)
                path.lineTo(dp120 - dp5, dp10.toFloat())
                val pathFill = Path().apply {
                    moveTo(dp120 - dp5 - dp8 * 2, dp10 + fl)
                    lineTo(dp120 - dp5 - dp8, 0f)
                    lineTo(dp120 - dp5, dp10 + fl)
                    close()
                }
                canvas.drawPath(pathFill, paintWhite)
                canvas.drawPath(path, paint)
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
        }/*.apply {
            setBounds(0, 0, width, height)
        }*/
    }
}