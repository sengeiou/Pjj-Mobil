package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.TextUtils
import kotlinx.android.synthetic.main.layout_image_notice_dialog.*

/**
 * Created by XinHeng on 2018/12/20.
 * describe：消息通知 ，资料认证-审核中
 */
class ImageNoticeDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    init {
        setContentView(R.layout.layout_image_notice_dialog)
        tv_cancel.setOnClickListener(onClick)
        setCanceledOnTouchOutside(false)
    }

    var notice: String? = ""
        set(value) {
            tv_notice.text = TextUtils.clean(value)
        }

    fun setImageResource(resource: Int) {
        iv.setImageResource(resource)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> dismiss()
        }
    }

}