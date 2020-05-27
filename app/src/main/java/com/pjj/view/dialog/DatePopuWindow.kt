package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow
import android.widget.Toast
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.DateSelectViewGroup
import com.pjj.view.custom.DateViewGroup
import com.pjj.view.custom.ToastNoticeView
import java.util.*

/**
 * Created by XinHeng on 2019/04/17.
 * describe：
 */
class DatePopuWindow(context: Context, diss: Boolean = true) : PopupWindow(context) {
    private var onclick = View.OnClickListener { it ->
        when (it.id) {
            R.id.iv_close -> dismiss()
            R.id.iv_select -> onDateSelectListener?.let { it ->
                it.dateSelect(dateView.getSelectDays())
                if (diss)
                    dismiss()
            }
        }
    }

    fun setNumForMonth(num: Int) {
        dateView.setNumForMonth(num)
    }

    fun setHasSelectDate(calendar: Calendar) {
        dateView.setHasSelectDate(calendar)
    }

    private var dateView: DateSelectViewGroup
    private var iv_close: View
    private var iv_select: View
    override fun dismiss() {
        dateView.cancelNotice()
        super.dismiss()
        onDateSelectListener?.dismiss()
    }

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_select_date_dialog, null)
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dateView = contentView.findViewById(R.id.dateView)
        val noticeView = contentView.findViewById<ToastNoticeView>(R.id.toastNotice)
        noticeView.isEnabled = false
        dateView.noticeView = noticeView
        iv_close = contentView.findViewById(R.id.iv_close)
        iv_select = contentView.findViewById(R.id.iv_select)
        iv_close.setOnClickListener(onclick)
        iv_select.setOnClickListener(onclick)
    }

    var onDateSelectListener: OnDateSelectListener? = null

    interface OnDateSelectListener {
        fun dateSelect(dates: MutableList<String>)
        fun dismiss()
    }
}