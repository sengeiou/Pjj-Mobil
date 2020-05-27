package com.pjj.view.adapter

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.module.parameters.HourTime
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils


/**
 * Created by XinHeng on 2018/11/22.
 * describe：
 */
class HourSelectAdapter : BaseAdapter() {
    var mLilst = ArrayList<HourTime>()
    private var mSelectList: ArrayList<Int> = ArrayList()
    private val SELECT_ALL = "全选剩余"
    fun setList(list: List<Int>) {
        mLilst.clear()
        mSelectList.clear()
        /*(6..23).forEach {
            var hourTime = HourTime()
            hourTime.isEnable = list.contains(it)
            hourTime.hour = it
            mLilst.add(hourTime)
        }
        (0..5).forEach {
            var hourTime = HourTime()
            hourTime.isEnable = list.contains(it)
            hourTime.hour = it
            mLilst.add(hourTime)
        }*/
        (0..23).forEach {
            var hourTime = HourTime()
            hourTime.isEnable = list.contains(it)
            hourTime.hour = it
            mLilst.add(hourTime)
        }
        notifyDataSetChanged()
    }

    private val color_ececec: Int by lazy {
        ContextCompat.getColor(PjjApplication.application, R.color.color_ececec)
    }
    private val color_999999: Int by lazy {
        ContextCompat.getColor(PjjApplication.application, R.color.color_999999)
    }

    private fun getIsEnable(): Drawable {
        return ViewUtils.getBgDrawable(Color.WHITE, color_ececec, ViewUtils.getDp(R.dimen.dp_3))
    }

    private fun getUnSelect(): Drawable {
        return ViewUtils.getBgDrawable(Color.WHITE, color_999999, ViewUtils.getDp(R.dimen.dp_3))
    }

    private fun getSelect(): Drawable {
        var color = ContextCompat.getColor(PjjApplication.application, R.color.color_theme)
        return ViewUtils.getBgDrawable(color, color, ViewUtils.getDp(R.dimen.dp_3))
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var viewHolder: ViewHolder
        if (convertView == null) {
            viewHolder = ViewHolder()
            convertView = TextView(parent.context).apply {
                viewHolder.textView = this
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_11))
//                var dp3 = ViewUtils.getDp(R.dimen.dp_3)
                var dp10 = ViewUtils.getDp(R.dimen.dp_10)
                setPadding(0, dp10, 0, dp10)
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
            convertView.setTag(viewHolder)
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        if (position < mLilst.size) {
            with(mLilst[position]) {
                updateChildView(viewHolder)
            }
        } else {
            viewHolder.textView!!.background = getUnSelect()
            viewHolder.textView!!.setTextColor(color_999999)
            viewHolder.textView!!.text = SELECT_ALL
        }
        return convertView
    }

    private fun HourTime.updateChildView(viewHolder: ViewHolder) {
        viewHolder.textView!!.run {
            text = this@updateChildView.toString()
            this.isEnabled = isEnable
            if (!isEnable) {
                background = getIsEnable()
                setTextColor(color_ececec)
            } else {
                if (isSelect) {
                    background = getSelect()
                    setTextColor(Color.WHITE)
                } else {
                    background = getUnSelect()
                    setTextColor(color_999999)
                }
            }
        }
    }

    override fun getItem(position: Int): Any {
        return when (position == mLilst.size) {
            true -> SELECT_ALL
            else -> mLilst[position]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mLilst.size + 1
    }

    private inner class ViewHolder {
        internal var textView: TextView? = null
    }

    var itemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        if (position < mLilst.size) {
            with(mLilst[position]) {
                isSelect = !isSelect
                if (isSelect) {
                    mSelectList.add(position)
                } else {
                    mSelectList.remove(position)
                }
                sendSelectNotice()
                notifyDataSetChanged()
            }
        } else {
            selectAllTime(0, 24)
        }
    }

    fun selectAllTime(start: Int, end: Int) {
        //mSelectList.clear()
        var change = false
        (start until end).forEach {
            with(mLilst[it]) {
                if (isEnable) {
                    isSelect = true
                    if (!mSelectList.contains(it)) {
                        mSelectList.add(it)
                        change = true
                    }
                }
            }
        }
        if (change) {
            sendSelectNotice()
            notifyDataSetChanged()
        }
    }

    private fun sendSelectNotice() {
        onSelectListener?.let {
            TextUtils.compareSmallToMore(mSelectList)
            //onSelectListener?.onSelect(buff.toString())
            onSelectListener?.onSelectList(mSelectList)
        }
    }

    var onSelectListener: OnSelectListener? = null

    interface OnSelectListener {
        /**
         * @param list 选中的时间列表，逗号隔开
         */
        fun onSelect(list: String)

        fun onSelectList(list: List<Int>)
    }
}