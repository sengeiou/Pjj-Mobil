package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.TextUtils
import kotlinx.android.synthetic.main.layout_no_pass_dialog.*

/**
 * Created by XinHeng on 2018/12/20.
 * describe：
 */
class NoPassDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
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
        setContentView(R.layout.layout_no_pass_dialog)
        tv_go.setOnClickListener(onClick)
        tv_cancel.setOnClickListener(onClick)
        setCanceledOnTouchOutside(false)
    }

    var errorText: String? = ""
        set(value) {
            tv_error.text = TextUtils.clean(value)
            if (!isShowing) {
                show()
            }
        }

    fun hiddenLeft() {
        tv_go.visibility = View.GONE
    }
    fun showLeft() {
        tv_go.visibility = View.VISIBLE
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_go -> {
                onItemClickListener?.go(tv_error.text.toString())
                dismiss()
            }
            R.id.tv_cancel -> dismiss()
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        /**
         * 去认证
         */
        fun go(msg: String)
    }
}