package com.pjj.view.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.utils.TextUtils
import kotlinx.android.synthetic.main.layout_time_dialog.*

/**
 * Created by XinHeng on 2018/11/24.
 * describe：时间小时选择
 */
class HourTimeDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private lateinit var mListLeft: ArrayList<String>
    private lateinit var mListRight: ArrayList<String>
    private var leftIndex: Int = 0
    private var rightIndex: Int = 0
    private var onclick = View.OnClickListener {
        when (it.id) {
            R.id.iv_close -> dismiss()
            R.id.iv_select -> onClickListener?.let { it ->
                if (leftIndex < rightIndex) {
                    it.onSure(leftIndex, rightIndex)
                    dismiss()
                } else {
                    var makeText = Toast.makeText(PjjApplication.application, "结束时间应大于开始时间", Toast.LENGTH_SHORT)
                    makeText.setGravity(Gravity.CENTER, 0, 0)
                    makeText.show()
                }
            }
        }
    }

    init {
        setContentView(R.layout.layout_time_dialog)
        setHourList()
        wheel_start.setWheelItemList(mListLeft)
        wheel_end.setWheelItemList(mListRight)
        wheel_start.setOnSelectListener { index, _ -> leftIndex = index }
        wheel_end.setOnSelectListener { index, _ -> rightIndex = index }
        iv_close.setOnClickListener(onclick)
        iv_select.setOnClickListener(onclick)
    }

    private fun setHourList() {
        mListLeft = ArrayList(24)
        mListRight = ArrayList(24)
        (0..23).forEach {
            mListLeft.add(TextUtils.format(it) + ":00")
        }
        (0..23).forEach {
            mListRight.add(TextUtils.format(it) + ":00")
        }
    }

    override fun getHeightRate(): Float {
        return -1f
    }

    var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onSure(leftIndex: Int, rightIndex: Int): Boolean
        //fun onCancel(): Boolean
    }
}
