package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_select_week_month_time.*
import kotlinx.android.synthetic.main.layout_bottom_list_dialog.*

/**
 * Created by XinHeng on 2019/01/03.
 * describe：
 */
class BottomListTextDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private var colorSelect = ViewUtils.getColor(R.color.color_f6f4f4)
    private var tvSelect: TextView? = null
    override fun getDialogGravity(): Int {
        return Gravity.BOTTOM
    }

    private var onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.tv_1 -> changeTvBg(tv_1)
            R.id.tv_2 -> changeTvBg(tv_2)
            R.id.tv_3 -> changeTvBg(tv_3)
            R.id.tv_4 -> changeTvBg(tv_4)
            R.id.iv_close -> dismiss()
            R.id.iv_select -> {
                tvSelect?.let { view ->
                    onSelectListener?.itemSelect(view.text.toString())
                }
                dismiss()
            }
        }
    }

    init {
        setContentView(R.layout.layout_bottom_list_dialog)
        tv_1.setOnClickListener(onClickListener)
        tv_2.setOnClickListener(onClickListener)
        tv_3.setOnClickListener(onClickListener)
        tv_4.setOnClickListener(onClickListener)
        iv_close.setOnClickListener(onClickListener)
        iv_select.setOnClickListener(onClickListener)
        setCanceledOnTouchOutside(false)
    }

    override fun show() {
        super.show()
        tv_1.background = ColorDrawable(Color.WHITE)
        tv_2.background = ColorDrawable(Color.WHITE)
        tv_3.background = ColorDrawable(Color.WHITE)
        tv_4.background = ColorDrawable(Color.WHITE)
        tvSelect = null
    }

    private fun changeTvBg(tv: TextView) {
        tv_1.background = ColorDrawable(Color.WHITE)
        tv_2.background = ColorDrawable(Color.WHITE)
        tv_3.background = ColorDrawable(Color.WHITE)
        tv_4.background = ColorDrawable(Color.WHITE)
        tvSelect = tv
        tv.background = ColorDrawable(colorSelect)
    }

    var onSelectListener: OnSelectListener? = null

    interface OnSelectListener {
        fun itemSelect(msg: String)
    }
}