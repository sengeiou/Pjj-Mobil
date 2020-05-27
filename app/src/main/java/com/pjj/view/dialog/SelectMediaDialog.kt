package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.R
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/18.
 * describe：
 */
class SelectMediaDialog(context: Context, type: Int = -1) : FullWithNoTitleDialog(context, 0) {
    private var tvFirst: TextView
    private var tvSecond: TextView
    private var tvThird: TextView
    private var tvFourth: TextView
    private var line: View
    override fun isFullScreen(): Boolean {
        return false
    }

    override fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    private var dp3 = ViewUtils.getFDp(R.dimen.dp_3)

    private var whiteDrawable: Drawable = GradientDrawable().apply {
        setColor(Color.WHITE)
        cornerRadius = dp3
    }
    private var whiteDrawableTop: Drawable = GradientDrawable().apply {
        setColor(Color.WHITE)
        //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
        cornerRadii = floatArrayOf(dp3, dp3, dp3, dp3, 0f, 0f, 0f, 0f)
    }
    private var whiteDrawableBottom: Drawable = GradientDrawable().apply {
        setColor(Color.WHITE)
        //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
        cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, dp3, dp3, dp3, dp3)
    }

    init {
        var dp288 = ViewUtils.getDp(R.dimen.dp_288)
        var dp38 = ViewUtils.getDp(R.dimen.dp_38)
        var dp1 = ViewUtils.getDp(R.dimen.dp_1)
        //var red = ViewUtils.getColor(R.color.color_ea4a4a)
        var gray = ViewUtils.getColor(R.color.color_777777)
        var sp15 = ViewUtils.getFDp(R.dimen.sp_15)
        var parent = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ColorDrawable(Color.TRANSPARENT)
            addView(TextView(context).apply {
                tvFirst = this
                layoutParams = LinearLayout.LayoutParams(dp288, dp38)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sp15)
                setTextColor(gray)
                text = "拍照"
                background = whiteDrawableTop
                gravity = Gravity.CENTER
            })
            addView(View(context).apply {
                line = this
                layoutParams = LinearLayout.LayoutParams(dp288, dp1)
                background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
            })
            addView(TextView(context).apply {
                tvSecond = this
                layoutParams = LinearLayout.LayoutParams(dp288, dp38)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sp15)
                setTextColor(gray)
                text = "从相册上传图片"
                background = ColorDrawable(Color.WHITE)
                gravity = Gravity.CENTER
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp288, dp1)
                background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
            })
            addView(TextView(context).apply {
                tvFourth = this
                layoutParams = LinearLayout.LayoutParams(dp288, dp38)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sp15)
                setTextColor(gray)
                text = "从相册上传视频"
                background = whiteDrawableBottom
                gravity = Gravity.CENTER
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp288, ViewUtils.getDp(R.dimen.dp_29))
                background = ColorDrawable(Color.TRANSPARENT)
            })
            addView(TextView(context).apply {
                tvThird = this
                layoutParams = LinearLayout.LayoutParams(dp288, dp38)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sp15)
                setTextColor(gray)
                text = "取消"
                background = whiteDrawable
                gravity = Gravity.CENTER
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp288, ViewUtils.getDp(R.dimen.dp_29))
                background = ColorDrawable(Color.TRANSPARENT)
            })
        }
        setContentView(parent)
        setCancelable(false)
        var onClickListener = View.OnClickListener {
            when (it) {
                tvFirst -> {
                    onItemClickListener?.takePhoto()
                }
                tvSecond -> {
                    onItemClickListener?.selectPhoto()
                }
                tvFourth -> {
                    onItemClickListener?.selectVideo()
                }
                //tvThird -> dismiss()
            }
            dismiss()
        }
        tvFirst.setOnClickListener(onClickListener)
        tvSecond.setOnClickListener(onClickListener)
        tvThird.setOnClickListener(onClickListener)
        tvFourth.setOnClickListener(onClickListener)
        setCanceledOnTouchOutside(false)
    }

    fun setType(type: Int) {
        tvFirst.visibility = View.VISIBLE
        line.visibility = View.VISIBLE
        tvSecond.visibility = View.VISIBLE
        tvFourth.visibility = View.VISIBLE
        if (type == 2) {
            tvFirst.visibility = View.GONE
            line.visibility = View.GONE
            tvSecond.visibility = View.GONE
        } else if (type == 1) {
            tvFourth.visibility = View.GONE
        }
        if(!isShowing){
            show()
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun takePhoto()
        fun selectPhoto()
        fun selectVideo()
    }
}