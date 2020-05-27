package com.pjj.view.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2019/03/19.
 * describe：
 */
class MediaSelectDialog(context: Context,themeResId:Int=0):FullWithNoTitleDialog(context,themeResId) {
    override fun isFullScreen(): Boolean {
        return false
    }

    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    init {
        setContentView(LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewUtils.getDp(R.dimen.dp_278), ViewGroup.LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            addView(TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_62))
                text = "拍摄"
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_15))
                setTextColor(ViewUtils.getColor(R.color.color_555555))
                gravity = Gravity.CENTER
                setOnClickListener {
                    onMediaSelectDialog?.takePhoto()
                    dismiss()
                }
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_1))
                background = ColorDrawable(ViewUtils.getColor(R.color.color_bfbfbf))
            })
            addView(TextView(context).apply {
                id = R.id.position
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_62))
                text = "文件"
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_15))
                setTextColor(ViewUtils.getColor(R.color.color_555555))
                gravity = Gravity.CENTER
                setOnClickListener {
                    onMediaSelectDialog?.selectMedia()
                    dismiss()
                }
            })
        })
    }
    var onMediaSelectDialog:OnMediaSelectListener?=null
    interface OnMediaSelectListener{
        fun selectMedia()
        fun takePhoto()
    }
}