package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import com.pjj.utils.Log
import com.pjj.view.custom.ToastNoticeView

class ToastNoticePopuWindow(context: Context) : PopupWindow() {
    private val noticeView: ToastNoticeView = ToastNoticeView(context)
    private var textHeight = 0
    private var textWidth = 0

    init {
        contentView = noticeView
        width = WindowManager.LayoutParams.WRAP_CONTENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isFocusable = false
        isTouchable = false
    }

    var noticeText: String = "toast"
        set(value) {
            field = value
            noticeView.text = value
            noticeView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            textHeight = noticeView.measuredHeight
            textWidth = noticeView.measuredWidth
        }

    fun showAsDropDown(anchor: View, tranX: Int) {
        //Log.e("TAG", "showAsDropDown tranX=$tranX")
        noticeView.tranX = tranX
        showAsDropDown(anchor, -textWidth / 2 + anchor.measuredWidth / 2, -anchor.measuredHeight - textHeight)
    }
}