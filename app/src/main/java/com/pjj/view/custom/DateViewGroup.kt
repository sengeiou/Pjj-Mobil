package com.pjj.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by XinHeng on 2018/11/27.
 * describe：月份日期
 */
class DateViewGroup @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    companion object {
        val ONLY_ONE = 1
        val MORE = 2
    }

    private var hasCalendarSelect: Calendar? = null
    private var columns: Int = 7
    private var childSlideLength = ViewUtils.getDp(R.dimen.dp_38)
    private var paddingLine = 5
    private var paddingColumn: Int = 0
    private var nowDate: Int = -1
    private var paddingChildHead = 0
    //    private var unSelectColor=ContextCompat.getColor(PjjApplication.application, R.color.color_df3629)
    private var selectTextColor = Color.WHITE
    //private var selectBgColor = ContextCompat.getColor(PjjApplication.application, R.color.color_40bbf7)
    private var weekTextColor = Color.BLACK // ContextCompat.getColor(PjjApplication.application, R.color.color_df3629)
    private var enableColor = ContextCompat.getColor(PjjApplication.application, R.color.color_999999)
    private var dateList = ArrayList<DateDay>()
    private var selectList = ArrayList<Int>()
    //private var unSelectDateList = ArrayList<Calendar>()
    var selectType = ONLY_ONE
    private var onChildClick = View.OnClickListener {
        var position = it.getTag(R.id.position) as Int
        var textView = it as TextView
        updateClickChildDateUI(position, textView)
    }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.DateViewGroup, defStyleAttr, 0).run {
            (0 until childCount).forEach {
                var index = getIndex(it)
                when (index) {
                    R.styleable.DateViewGroup_date_num_columns -> columns = getInt(index, columns)
                    R.styleable.DateViewGroup_date_child_length -> childSlideLength = getDimensionPixelSize(index, childSlideLength)
                    R.styleable.DateViewGroup_date_padding_line -> paddingLine = getDimensionPixelOffset(index, paddingLine)
                }
            }
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        var size = MeasureSpec.getSize(widthMeasureSpec)
        paddingColumn = (size - paddingLeft - paddingRight - columns * childSlideLength) / (columns - 1)
        paddingLine = paddingColumn
        var height = 0
        var dateChildCount = 0
        (1..childCount).forEach {
            var childAt = getChildAt(it - 1)
            if ("head" == childAt.tag) {
                dateChildCount = 0
                height += childAt.measuredHeight
            } else {
                ++dateChildCount
                if (dateChildCount % 7 == 1) {//新的一行
                    height += childAt.measuredHeight + paddingLine
                }
            }
        }
        //Log.e("TAG", "onMeasure: with = $size, height = $height")
        setMeasuredDimension(size, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = paddingTop
        var left = paddingLeft
        var childDateNum = 0
        var bottom: Int
        var right: Int
        Log.e("TAG", "childCount=$childCount, ")
        var i = childSlideLength + paddingLine
        (1..childCount).forEach {
            val childAt = getChildAt(it - 1)
            right = left + childAt.measuredWidth
            bottom = top + childAt.measuredHeight
            if ("head" == childAt.tag) {
                if (childDateNum % 7 != 0) {
                    left = paddingLeft
                    top += i
                    bottom += i
                }
                childDateNum = 0
                Log.e("TAG", "onLayout：$left $top $right $bottom")
                childAt.layout(left, top, right, bottom)
                top = bottom + paddingChildHead
            } else {
                ++childDateNum
                //Log.e("TAG", "onLayout1：$left $top $right $bottom")
                childAt.layout(left, top, right, bottom)
                if (childDateNum % columns == 0) {
                    left = paddingLeft
                    top += i
                } else {
                    left = right + paddingColumn
                }
            }
        }
    }


    fun setNumForMonth(num: Int) {
        dateList.clear()
        var calendar = Calendar.getInstance()
        nowDate = calendar[Calendar.DATE]
        calendar[Calendar.DATE] = 1
        addChildList(calendar)
        nowDate = -1//下个月重新初始化
        (1..num).forEach {
            var next = calendar.clone() as Calendar
            next.add(Calendar.MONTH, it)
            addChildList(next)
        }
        dateList.forEachIndexed { index, it ->
            //println(index)
            addView(when {
                it.isHead -> getChildHeadView(it.toString())
                it.isEmpty -> getEmptyView()
                else -> getChildView(it.getDate(), when {
                    it.isEnable -> enableColor
                    it.isWeek -> weekTextColor
                    else -> Color.BLACK
                }).apply {
                    setTag(R.id.position, index)
                    isEnabled = !it.isEnable
                    hasCalendarSelect?.run {
                        if (this[Calendar.YEAR] == it.year && this[Calendar.MONTH] == it.month - 1 && this[Calendar.DATE] == it.date) {
                            background = getSelectDrawable()
                            setTextColor(selectTextColor)
                            it.isSelect = true
                            selectList.add(index)
                        }
                    }
                }
            })
        }
    }

    private fun getSelectDrawable(): Drawable {
        return ViewUtils.getDrawable(R.drawable.shape_theme_bg_circle)
    }

    private fun addChildList(calendar: Calendar) {
        //添加头部日期
        dateList.add(DateDay().apply {
            isHead = true
            setDateDayDate(calendar, this)
        })
        //添加日期子元素
        var actualMaximum = calendar.getActualMaximum(Calendar.DATE)//当月最大日期数
        var week = calendar[Calendar.DAY_OF_WEEK]
        var headDays = 8 - week
        for (it in 1 until week) {//月份开头空白部分
            dateList.add(DateDay().apply {
                isEmpty = true
            })
        }
        var i: Int
        (1..actualMaximum).forEach {
            i = it - headDays
            dateList.add(DateDay().apply {
                isEnable = it < nowDate
                isWeek = (i % 7 == 0) or (i % 7 == 1)
                setDateDayDate(calendar, this, it)
            })
        }
    }

    private fun getChildHeadView(s: String): TextView {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, childSlideLength)
            gravity = Gravity.CENTER
            setTextColor(ViewUtils.getColor(R.color.color_333333))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
            text = s
            tag = "head"
        }
    }

    private fun getChildView(s: String, color: Int): TextView {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(childSlideLength, childSlideLength)
            gravity = Gravity.CENTER
            setTextColor(color)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_13))
            text = s
            setOnClickListener(onChildClick)
        }
    }

    private fun getEmptyView(): TextView {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(childSlideLength, childSlideLength)
        }
    }

    private fun setDateDayDate(calendar: Calendar, dateDay: DateDay, date: Int = -1) {
        dateDay.year = calendar[Calendar.YEAR]
        dateDay.month = calendar[Calendar.MONTH] + 1
        dateDay.date = date
    }

    private fun updateClickChildDateUI(position: Int, textView: TextView) {
        with(dateList[position]) {
            isSelect = !isSelect
            when (isSelect) {
                true -> {
                    textView.background = getSelectDrawable()
                    textView.setTextColor(selectTextColor)
                    addSelect(position)
                }
                else -> {
                    textView.background = ColorDrawable(Color.TRANSPARENT)
                    textView.setTextColor(when (isWeek) {
                        true -> weekTextColor
                        else -> Color.BLACK
                    })
                    removeSelect(position)
                }
            }
        }
    }

    private fun updateChildDateUI(position: Int, textView: TextView) {
        //println(dateList[position])
        with(dateList[position]) {
            isSelect = !isSelect
            when (isSelect) {
                true -> {
                    textView.background = getSelectDrawable()
                    textView.setTextColor(selectTextColor)
                }
                else -> {
                    textView.background = ColorDrawable(Color.TRANSPARENT)
                    textView.setTextColor(when (isWeek) {
                        true -> weekTextColor
                        else -> Color.BLACK
                    })
                }
            }
        }
    }

    private fun addSelect(position: Int) {
//        if (selectList.contains(position)) {
//            return
//        }
        if (selectType == ONLY_ONE) {
            selectList.forEach { updateChildDateUI(it, getChildAt(it) as TextView) }
            selectList.clear()
            selectList.add(position)
        } else {
            selectList.add(position)
            //updateChildDateUI(position, getChildAt(position) as TextView)
        }
    }

    private fun removeSelect(position: Int) {
//        if (selectList.contains(position)) {
        //updateChildDateUI(position, getChildAt(position) as TextView)
        selectList.remove(position)
        //}
    }

    private class DateDay {
        var isHead = false
        var isWeek = false
        var isEmpty = false
        var isEnable = false
        var isSelect = false
        var year = -1
        var month = -1
        var date = -1
        fun getDate(): String {
            return when (date) {
                -1 -> ""
                else -> date.toString()
            }
        }

        override fun toString(): String {
            return "$year-${TextUtils.format(month)}"
        }

        fun toDateString(): String {
            return "$year-${TextUtils.format(month)}-${TextUtils.format(date)}"
        }

        fun toCalendar(): Calendar {
            return Calendar.getInstance().apply {
                this[Calendar.YEAR] = year
                this[Calendar.MONTH] = month - 1
                this[Calendar.DATE] = date
            }
        }
    }

    fun getSelectDay(): Calendar? {
        return if (selectList.size > 0)
            dateList[selectList[0]].toCalendar()
        else null
    }

    fun setHasSelectDate(calendar: Calendar?) {
        hasCalendarSelect = calendar
        setNumForMonth(5)
    }

    fun setMountDate(num: Int) {
        setNumForMonth(num)
    }

    fun getSelectDays(): String {
        TextUtils.compareSmallToMore(selectList)
        var buff = StringBuffer()
        selectList.forEach {
            buff.append(dateList[it].toDateString())
            buff.append(",")
        }
        if (buff.isNotEmpty()) {
            buff.deleteCharAt(buff.length - 1)
        }
        return buff.toString()
    }
}
