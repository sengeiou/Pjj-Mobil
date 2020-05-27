package com.pjj.view.dialog

import android.content.Context
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_sex_dialog.*

/**
 * Created by XinHeng on 2019/03/20.
 * describe：
 */
class SexDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
    private var sex: String? = null
    private var sexTag = ""
    private var onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.iv_close -> dismiss()
            R.id.iv_select -> {
                if (null == sex) {
                    onSexDialogListener?.unSelectNotice()
                    return@OnClickListener
                }
                onSexDialogListener?.selectSex(sex!!, sexTag)
                dismiss()
            }
            R.id.tv_boy -> {
                if ("男" != sex) {
                    sex = "男"
                    sexTag = "1"
                    tv_boy.setTextColor(ViewUtils.getColor(R.color.color_theme))
                    tv_girl.setTextColor(ViewUtils.getColor(R.color.color_333333))
                }
            }
            R.id.tv_girl -> {
                if ("女" != sex) {
                    sex = "女"
                    sexTag = "0"
                    tv_girl.setTextColor(ViewUtils.getColor(R.color.color_theme))
                    tv_boy.setTextColor(ViewUtils.getColor(R.color.color_333333))
                }
            }
        }
    }

    init {
        setContentView(R.layout.layout_sex_dialog)
        tv_girl.setOnClickListener(onClickListener)
        tv_boy.setOnClickListener(onClickListener)
        iv_close.setOnClickListener(onClickListener)
        iv_select.setOnClickListener(onClickListener)
    }

    var onSexDialogListener: OnSexDialogListener? = null

    interface OnSexDialogListener {
        fun selectSex(sex: String, sexTag: String)
        fun unSelectNotice()
    }
}