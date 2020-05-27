package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_remind_notice_dialog.*

class RemindNoticeDialog(context: Context, themeResId: Int) : FullWithNoTitleDialog(context, themeResId) {

    init {
        setContentView(R.layout.layout_remind_notice_dialog)
        tv_sure.setOnClickListener(onClick)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun getWindowBgDrawable(): Drawable {
        return GradientDrawable().apply {
            setColor(Color.WHITE)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_3)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_sure -> dismiss()
        }
    }
}