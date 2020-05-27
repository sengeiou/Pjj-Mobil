package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_time_out_item.*

/**
 * Created by XinHeng on 2019/04/11.
 * describe：
 */
class TimeOutDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
    private var rightClickTag = true
    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> {
                if (!rightClickTag) {
                    onTimeOutDialogListener?.callClick()
                }
                dismiss()
            }

            R.id.tv_sure -> {
                if (rightClickTag)
                    onTimeOutDialogListener?.callClick()
                dismiss()
            }
        }
    }

    init {
        setContentView(R.layout.layout_time_out_item)
        tv_cancel.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
        //setCanceledOnTouchOutside(true)
        //setCancelable(true)
    }


    override fun getWindowBgDrawable(): Drawable {
        return GradientDrawable().apply {
            setColor(Color.WHITE)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_5)
        }
    }

    var onTimeOutDialogListener: OnTimeOutDialogListener? = null

    interface OnTimeOutDialogListener {
        fun callClick()
    }
}