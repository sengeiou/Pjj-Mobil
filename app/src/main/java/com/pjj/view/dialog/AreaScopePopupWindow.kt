package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils

class AreaScopePopupWindow(private var context: Context) : PopupWindow(context) {
    private lateinit var ll_parent: LinearLayout
    private val dp38 = ViewUtils.getDp(R.dimen.dp_38)
    private val dp25 = ViewUtils.getDp(R.dimen.dp_25)
    val textColor = ViewUtils.getColor(R.color.color_333333)
    val textColorSelect = ViewUtils.getColor(R.color.color_theme)
    private var selectTextView: TextView? = null
    private val spaceView: View
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_reset -> {
                selectTextView?.let { view ->
                    if (view.text.toString() != "不限") {
                        recover(view)
                        selectTextView = this@AreaScopePopupWindow.ll_parent.getChildAt(0) as TextView
                        selectTextView!!.setTextColor(textColorSelect)
                    }
                }
                //selectTextView = null
                onAreaScopeListener?.reset()
                dismiss()
                return@OnClickListener
            }
            R.id.tv_complete -> {
                if (unClick) {
                    dismiss()
                    return@OnClickListener
                }
                if (null == selectTextView) {
                    onAreaScopeListener?.notice("请选择投放区域")
                    return@OnClickListener
                }
                val text = selectTextView!!.text.toString()
                onAreaScopeListener?.select(text, when (text) {
                    "不限" -> null
                    else -> {
                        text.replace("km", "000")
                    }
                })
                dismiss()
                return@OnClickListener
            }
        }
        if (it is TextView) {
            if (unClick) {
                return@OnClickListener
            }
            selectTextView?.let { view ->
                recover(view)
            }
            selectTextView = it
            it.setTextColor(textColorSelect)
        } else {
            dismiss()
        }
    }
    private var unClick = false
    fun setIsUnClick(tag: Boolean) {
        unClick = tag
        if (unClick) {//不可点
            selectTextView?.let {
                if (it.text.toString() != "不限") {
                    recover(it)
                    (ll_parent.getChildAt(0) as TextView).setTextColor(textColorSelect)
                }
            }
        }
    }

    init {
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        isFocusable = true
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_area_scope_item, null, false)
        ll_parent = contentView.findViewById(R.id.ll_parent)
        spaceView = contentView.findViewById(R.id.space_view)
        (0 until ll_parent.childCount).forEach {
            ll_parent.getChildAt(it).setOnClickListener(onClick)
        }
        selectTextView = ll_parent.getChildAt(0) as TextView
        selectTextView!!.setTextColor(textColorSelect)
        spaceView.setOnClickListener(onClick)
        contentView.findViewById<TextView>(R.id.tv_reset).setOnClickListener(onClick)
        contentView.findViewById<TextView>(R.id.tv_complete).setOnClickListener(onClick)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnDismissListener {
            onAreaScopeListener?.dismiss()
        }
    }


    fun setData(list: MutableList<String>?, default: String) {
        list?.forEach {
            ll_parent.addView(createTextView(it).apply {
                setOnClickListener(onClick)
            })
        }
    }

    private fun createTextView(content: String): TextView {
        return TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp38)
            setPadding(dp25, 0, 0, 0)
            gravity = Gravity.CENTER_VERTICAL
            setTextColor(textColor)
        }
    }

    private fun recover(tv: TextView) {
        tv.setTextColor(textColor)
    }

    var onAreaScopeListener: OnAreaScopeListener? = null

    interface OnAreaScopeListener {
        fun notice(msg: String)
        fun select(text: String, canshu: String?)
        fun dismiss()
        fun reset()
    }
}