package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_message_item.*
import kotlinx.android.synthetic.main.layout_time_out_item.*
import kotlinx.android.synthetic.main.layout_time_out_item.tv_cancel
import kotlinx.android.synthetic.main.layout_time_out_item.tv_sure

/**
 * Created by XinHeng on 2019/04/11.
 * describe：
 */
class LineMessageDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
    private var cancelTag = true
    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> {
                dismiss()
            }

            R.id.tv_sure -> {
                onMessageDialogListener?.callClick()
                if (cancelTag) {
                    dismiss()
                } else {
                    cancelTag = true
                }
            }
        }
    }

    init {
        setContentView(R.layout.layout_line_message_item)
        tv_sure.setOnClickListener(onClick)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    fun message(msg: String, cancelTag: Boolean = true) {
        tv_message.text = msg
        this.cancelTag = cancelTag
        if (!isShowing) {
            show()
        }
    }

    fun message(): String {
        return tv_message.text.toString()
    }

    override fun getWindowBgDrawable(): Drawable {
        return GradientDrawable().apply {
            setColor(Color.WHITE)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_5)
        }
    }

    var onMessageDialogListener: OnMessageDialogListener? = null

    interface OnMessageDialogListener {
        fun callClick()
    }
}