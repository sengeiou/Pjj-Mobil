package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_call_phone_item.*

/**
 * Created by XinHeng on 2019/04/11.
 * describe：
 */
class PhoneDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
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
                    onCallPhoneDialogListener?.callClick()
                }
                dismiss()
            }

            R.id.tv_call -> {
                if (rightClickTag)
                    onCallPhoneDialogListener?.callClick()
                dismiss()
            }
        }
    }

    init {
        setContentView(R.layout.layout_phone_dialog_item)
        tv_cancel.setOnClickListener(onClick)
        tv_call.setOnClickListener(onClick)
        setCanceledOnTouchOutside(false)
        setCancelable(true)
    }

    var phone: String = ""
        set(value) {
            field = value
            tv_phone.text = value
        }

    override fun getWindowBgDrawable(): Drawable {
        return GradientDrawable().apply {
            setColor(Color.WHITE)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_3)
        }
    }

    var onCallPhoneDialogListener: OnCallPhoneDialogListener? = null

    interface OnCallPhoneDialogListener {
        fun callClick()
    }
}