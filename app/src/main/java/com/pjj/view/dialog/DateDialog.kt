package com.pjj.view.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast

import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.utils.OSUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.DateViewGroup
import kotlinx.android.synthetic.main.layout_date_dialog.*
import java.util.*

class DateDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0, type: Int = DateViewGroup.ONLY_ONE, diss: Boolean = true) : FullWithNoTitleDialog(context, themeResId) {
    private var onclick = View.OnClickListener { it ->
        when (it.id) {
            R.id.iv_close -> dismiss()
            R.id.iv_select -> onDateSelectListener?.let { it ->
                if (type == DateViewGroup.ONLY_ONE) {
                    var calendar = dateView.getSelectDay()
                    if (null == calendar) {
                        var makeText = Toast.makeText(PjjApplication.application, "请选择日期", Toast.LENGTH_SHORT)
                        makeText.setGravity(Gravity.CENTER, 0, 0)
                        makeText.show()
                    } else {
                        it.dateSelect(calendar)
                    }
                } else {
                    it.dateSelect(dateView.getSelectDays())
                }
                if (diss)
                    dismiss()
            }
        }
    }

    fun setSelectDay(calendar: Calendar?) {
        dateView.setHasSelectDate(calendar)
    }

    fun setNumForMonth(num: Int) {
        dateView.setNumForMonth(num)
    }

    init {
        setContentView(R.layout.layout_date_dialog)
        iv_close.setOnClickListener(onclick)
        iv_select.setOnClickListener(onclick)
        dateView.selectType = type
    }

    override fun getDialogGravity(): Int {
        return Gravity.BOTTOM
    }

    override fun getHeightRate(): Float {
        return -1f
    }

    override fun isCleanBg(): Boolean {
        return true
    }

    override fun getOtherHeightSize(): Int {
        return (if (OSUtils.isEmui()) 0 else PjjApplication.application.statueHeight) + ViewUtils.getDp(R.dimen.dp_42)
    }

    var onDateSelectListener: OnDateSelectListener? = null

    interface OnDateSelectListener {
        fun dateSelect(calendar: Calendar)
        fun dateSelect(dates: String)
    }
}
