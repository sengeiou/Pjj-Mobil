package com.pjj.view.dialog

import android.content.Context
import android.view.View
import com.pjj.R
import kotlinx.android.synthetic.main.layout_two_text_dialog_item.*

/**
 * Created by XinHeng on 2019/03/12.
 * describe：
 */
class TwoTextDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private val click = View.OnClickListener {
        when (it.id) {
            R.id.tv_sure -> {
                onTwoTextListener?.sureClick()
                dismiss()
            }
            R.id.tv_cancel -> dismiss()
        }
    }

    init {
        setContentView(R.layout.layout_two_text_dialog_item)
        tv_sure.setOnClickListener(click)
        tv_cancel.setOnClickListener(click)
    }

    fun setTitleContent(title: String, content: String) {
        tv_title.text = title
        tv_content.text = content
        if (!isShowing) {
            show()
        }
    }

    var onTwoTextListener: OnTwoTextListener? = null

    interface OnTwoTextListener {
        fun sureClick()
    }
}