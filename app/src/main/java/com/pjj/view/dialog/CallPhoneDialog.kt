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
class CallPhoneDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
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
        setContentView(R.layout.layout_call_phone_item)
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

    fun setCancelColor(color: Int) {
        tv_cancel.setTextColor(color)
    }

    var rightText: String = ""
        set(value) {
            field = value
            tv_call.text = value
        }

    fun setTowText(left: String?, right: String?, rightClickTag: Boolean = false) {
        left?.let {
            tv_cancel.text = it
        }
        this.rightClickTag = rightClickTag
        right?.let {
            tv_call.text = it
        }
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