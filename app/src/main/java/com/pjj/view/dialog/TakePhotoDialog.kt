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
class TakePhotoDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private var tvFirst: TextView
    private var tvSecond: TextView
    private var tvThird: TextView
    private var iv: ImageView
    private var resultTakePhoto = false
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
        var dp192 = ViewUtils.getDp(R.dimen.dp_192)
        var red = ViewUtils.getColor(R.color.color_ea4a4a)
        var gray = ViewUtils.getColor(R.color.color_777777)
        var sp15 = ViewUtils.getFDp(R.dimen.sp_15)
        var parent = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            background = ColorDrawable(Color.TRANSPARENT)
            addView(ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp192, dp192).apply {
                    bottomMargin = ViewUtils.getDp(R.dimen.dp_122)
                    gravity = Gravity.CENTER_HORIZONTAL
                }
                scaleType = ImageView.ScaleType.FIT_XY
                iv = this
                visibility = View.GONE
            })
            addView(TextView(context).apply {
                tvFirst = this
                layoutParams = LinearLayout.LayoutParams(dp288, dp38)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sp15)
                setTextColor(red)
                text = "拍照"
                background = whiteDrawableTop
                gravity = Gravity.CENTER
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp288, dp1)
                background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
            })
            addView(TextView(context).apply {
                tvSecond = this
                layoutParams = LinearLayout.LayoutParams(dp288, dp38)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sp15)
                setTextColor(gray)
                text = "从相册上传"
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
                    if (resultTakePhoto) {
                        onItemClickListener?.surePhoto(nowPath!!)
                    } else {
                        onItemClickListener?.takePhoto()
                    }
                }
                tvSecond -> {
                    onItemClickListener?.selectPhoto()
                }
                tvThird -> dismiss()
            }
        }
        tvFirst.setOnClickListener(onClickListener)
        tvSecond.setOnClickListener(onClickListener)
        tvThird.setOnClickListener(onClickListener)
        setCanceledOnTouchOutside(false)
    }

    private var nowPath: String? = null
    fun setPhotoResult(path: String?) {
        if (TextUtils.isEmpty(path)) {
            tvFirst.text = "拍照"
            tvSecond.text = "从相册上传"
            resultTakePhoto = false
            iv.visibility = View.GONE
        } else {
            nowPath = path
            tvFirst.text = "确定"
            tvSecond.text = "重新上传"
            resultTakePhoto = true
            iv.visibility = View.VISIBLE
            Glide.with(context).load(path).into(iv)
        }
        if (!isShowing)
            show()
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun takePhoto()
        fun selectPhoto()
        fun surePhoto(path: String)
    }
}