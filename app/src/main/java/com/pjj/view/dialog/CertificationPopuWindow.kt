package com.pjj.view.dialog

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2019/01/09.
 * describe：认证选择
 */
class CertificationPopuWindow(context: Context) : PopupWindow() {
    //private var color6 = ViewUtils.getColor(R.color.color_666666)
    private var dp9 = ViewUtils.getDp(R.dimen.dp_9)
    private var color3 = ViewUtils.getColor(R.color.color_333333)
    private var colorSelect = ViewUtils.getColor(R.color.color_theme)
    private lateinit var textFirst: TextView
    private lateinit var textSecond: TextView

    private var onClickListener = View.OnClickListener {
        when (it) {
            textFirst -> {
                textFirst.setTextColor(colorSelect)
                textSecond.setTextColor(color3)
                onCertificationListener?.textClick(1)
            }
            textSecond -> {
                textFirst.setTextColor(color3)
                textSecond.setTextColor(colorSelect)
                onCertificationListener?.textClick(2)
            }
        }
        dismiss()
    }

    init {
        var dp117 = ViewUtils.getDp(R.dimen.dp_137)
        var dp112 = ViewUtils.getDp(R.dimen.dp_93)
        val dp1 = ViewUtils.getDp(R.dimen.dp_1)
        var sp13 = ViewUtils.getFDp(R.dimen.sp_12)
        val dp19 = ViewUtils.getDp(R.dimen.dp_19)
        contentView = LinearLayout(context).apply {
            background = ViewUtils.getDrawable(R.mipmap.white_transport_bg)
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(dp117,dp112)
            addView(TextView(context).apply {
                textFirst = this
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
                text = "个人认证"
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sp13)
                setTextColor(color3)
                setPadding(0, dp9, 0, 0)
                setOnClickListener(onClickListener)
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp1).apply {
                    marginStart = dp19
                    marginEnd = dp19
                }
                setBackgroundColor(ViewUtils.getColor(R.color.color_f1f1f1))
            })
            addView(TextView(context).apply {
                textSecond = this
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
                text = "商家认证"
                setPadding(0, 0, 0, dp9)
                setTextColor(color3)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, sp13)
                gravity = Gravity.CENTER
                setOnClickListener(onClickListener)
            })
        }
        width = dp117
        height = dp112
        isFocusable = true
    }

    fun updateTextContent(first: String, second: String, recover: Boolean = false) {
        textFirst.text = first
        textSecond.text = second
        if (recover) {
            textFirst.setTextColor(color3)
            textSecond.setTextColor(color3)
        }
    }

    fun setDefault(index: Int) {
        if (index == 1) {
            textFirst.setTextColor(colorSelect)
            textSecond.setTextColor(color3)
        } else {
            textFirst.setTextColor(color3)
            textSecond.setTextColor(colorSelect)
        }
    }

    var onCertificationListener: OnCertificationListener? = null

    interface OnCertificationListener {
        fun textClick(index: Int)
    }
}
