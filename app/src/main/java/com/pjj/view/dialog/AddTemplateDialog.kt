package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import com.pjj.R
import kotlinx.android.synthetic.main.layout_add_template_dialog.*

/**
 * Created by XinHeng on 2018/12/20.
 * describe：
 */
class AddTemplateDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
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
        setContentView(R.layout.layout_add_template_dialog)
        tv_cancel.setOnClickListener(onClick)
        tv_create.setOnClickListener(onClick)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_create -> {
                onItemClickListener?.leftClick()
                dismiss()
            }
        }
    }

    var notice: String = "添加模板后才能发布哦~"
        set(value) {
            tv_notice.text = value
        }
    var leftText: String = "去创建"
        set(value) {
            tv_create.text = value
        }

    fun setImageResource(resource: Int, tag: Boolean = true, width: Int = 0, height: Int = 0) {
        iv.setImageResource(resource)
        if (width > 0) {
            var layoutParams = iv.layoutParams
            layoutParams.width = width
            layoutParams.height = height
            iv.layoutParams = layoutParams
        }
        if (tag && !isShowing) {
            show()
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        /**
         * 左侧按钮点击
         */
        fun leftClick()
    }
}