package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/18.
 * describe：消息通知
 */
class NoticeDialog(context: Context, imgResource: Int = R.mipmap.cry_white, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private val mainHandler: Handler by lazy {
        Handler()
    }
    var synchronizedImage = false
    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }


    override fun isFullScreen(): Boolean {
        return false
    }

    override fun isCleanBg(): Boolean {
        return true
    }

    override fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    private var iv: ImageView
    private var tv: TextView

    init {
        val dp35 = ViewUtils.getDp(R.dimen.dp_38)
        val dp173 = ViewUtils.getDp(R.dimen.dp_173)
        val dp11 = ViewUtils.getDp(R.dimen.dp_11)
        val dp125 = ViewUtils.getDp(R.dimen.dp_125)
        setContentView(LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            background = getCircleDrawable()
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            addView(ImageView(context).apply {
                iv = this
                layoutParams = LinearLayout.LayoutParams(dp35, dp35)
                setImageResource(imgResource)
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp173, dp11)
            })
            addView(TextView(context).apply {
                tv = this
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                minHeight = ViewUtils.getDp(R.dimen.dp_35)
                gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                text = "没有监播中的屏幕"
                setTextColor(Color.WHITE)
                setPadding(dp11, 0, dp11, 0)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_13))
            })
        })
        val params = window.attributes
        params.width = dp173
        params.height = dp125
        window.attributes = params
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    private fun getCircleDrawable(): Drawable {
        return GradientDrawable().apply {
            setColor(ViewUtils.getColor(R.color.color_707070_10))
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_5)
        }
    }

    fun updateImage(imgResource: Int, filter: Boolean = false) {
        if (!synchronizedImage) {
            if (filter) {
                iv.setColorFilter(Color.WHITE)
            }
            iv.setImageResource(imgResource)
        }
    }

    fun setNotice(msg: String?, showTime: Int = 2000) {
        var clean = TextUtils.clean(msg)
        tv.text = clean
        if (!isShowing) {
            show(showTime)
        }

    }

    fun show(showTime: Int) {
        show()
        mainHandler.postDelayed({
            if (isShowing) {
                dismiss()
            }
        }, showTime.toLong())
    }

    override fun show() {
        super.show()
        /*mainHandler.postDelayed({
            if (isShowing) {
                dismiss()
            }
        }, 2000)*/
    }
}