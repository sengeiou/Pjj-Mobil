package com.pjj.view.dialog

import android.content.Context
import android.view.View
import com.pjj.R
import com.pjj.utils.TextUtils
import kotlinx.android.synthetic.main.layout_select_start_hour.*

/**
 * Created by XinHeng on 2018/11/24.
 * describe：时间小时选择
 */
class SelectStartHourDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private lateinit var mListLeft: ArrayList<String>
    private var leftIndex: Int = 0
    private var onclick = View.OnClickListener {
        when (it.id) {
            R.id.iv_close -> dismiss()
            R.id.iv_select -> onClickListener?.let { it ->
                it.onSure(leftIndex)
                dismiss()
            }
        }
    }

    init {
        setContentView(R.layout.layout_select_start_hour)
        setHourList()
        wheel_start.setWheelItemList(mListLeft)
        wheel_start.setOnSelectListener { index, _ -> leftIndex = index }
        iv_close.setOnClickListener(onclick)
        iv_select.setOnClickListener(onclick)
    }

    private fun setHourList() {
        mListLeft = ArrayList(24)
        (0..23).forEach {
            mListLeft.add(TextUtils.format(it) + ":00")
        }
    }

    override fun getHeightRate(): Float {
        return -1f
    }

    var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onSure(leftIndex: Int): Boolean
        //fun onCancel(): Boolean
    }
}
