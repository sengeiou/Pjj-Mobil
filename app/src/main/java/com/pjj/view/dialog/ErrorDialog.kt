package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_error_show_item.*

/**
 * Created by XinHeng on 2019/04/11.
 * describe：
 */
class ErrorDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
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
        }
    }

    init {
        setContentView(R.layout.layout_error_show_item)
        tv_cancel.setOnClickListener(onClick)
        //setCanceledOnTouchOutside(true)
        //setCancelable(true)
    }

    fun showError(msg: String) {
        tv_error.text = msg
        if (!isShowing) {
            show()
        }
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