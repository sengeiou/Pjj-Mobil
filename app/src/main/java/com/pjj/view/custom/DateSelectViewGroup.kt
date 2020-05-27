package com.pjj.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
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
class DateSelectViewGroup @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    companion object {
        val START_SELECT = 3
        val END_SELECT = 4
        val NONE_SELECT = 5

        val BOTH_CIRCLE = 6
        val LEFT_CIRCLE = 7
        val RIGHT_CIRCLE = 8
    }

    private var startView: DateChildView? = null
    private var endView: DateChildView? = null
    private var selectStatue = NONE_SELECT
    private var hasCalendarSelect: Calendar? = null
    private var hasSelectPosition: Int = -1
    private var columns: Int = 7
    private var childSlideLength = ViewUtils.getDp(R.dimen.dp_38)
    private var paddingLine = 5
    private var paddingColumn: Int = 0
    private var nowDate: Int = -1
    private var paddingChildHead = 0
    private var selectTextColor = Color.WHITE
    private var weekTextColor = Color.BLACK // ContextCompat.getColor(PjjApplication.application, R.color.color_df3629)
    private var enableColor = ContextCompat.getColor(PjjApplication.application, R.color.color_999999)
    private var dateList = ArrayList<DateDay>()
    private var selectList = ArrayList<Int>()
    var noticeView: ToastNoticeView? = null

    private var onChildClick = OnClickListener {
        if (it is DateChildView) {
            val textView = it
            when (selectStatue) {
                NONE_SELECT -> {
                    cleanDataView()
                    selectStatue = START_SELECT
                    startView = textView
                    textView.textBottom = "开始"
                    textView.background = getSelectDrawable()
                    showNotice("请选择结束日期", textView)
                }
                START_SELECT -> {
                    if (textView != startView) {
                        selectStatue = END_SELECT
                        endView = textView
                    }
                }
                END_SELECT -> {
                    recover()
                    selectStatue = START_SELECT
                    startView = textView
                    textView.textBottom = "开始"
                    showNotice("请选择结束日期", textView)
                    textView.background = getSelectDrawable()
                }
            }
            updateSelectChild()
        }
    }
    private val rectList = ArrayList<DrawableStatue>()
    private fun updateSelectChild() {
        startView?.let {
            it.setTextColor(Color.WHITE)
            selectList.clear()
            selectList.add(it.getTag(R.id.position) as Int)
        }
        endView?.let {
            it.setTextColor(Color.WHITE)
        }
        if (selectStatue == END_SELECT) {
            selectList.clear()
            var startPosition = startView!!.getTag(R.id.position) as Int
            var endPosition = endView!!.getTag(R.id.position) as Int
            //构建矩形列表
            if (startPosition > endPosition) {
                val i = endPosition
                endPosition = startPosition
                startPosition = i
                val view = startView!!
                startView = endView
                endView = view
            }
            startView!!.textBottom = "开始"
            endView!!.textBottom = "结束"
            startView!!.background = null
            endView!!.background = null
            var top = -1
            var left = -1
            var right = -1
            var bottom = -1
            var leftCircleTag = true
            (startPosition..endPosition).forEach {
                val child = getChildAt(it) as? TextView ?: return@forEach
                Log.e("TAG", "forEach it=$it tag=${child.tag}")
                if (top == -1 && child.tag != "head") {
                    //leftCircleTag = true
                    top = child.top
                    left = child.left
                    right = child.right
                    bottom = child.bottom
                    selectList.add(it)
                    if (it == endPosition) {
                        rectList.add(DrawableStatue(left, top, right, bottom, when {
                            leftCircleTag -> BOTH_CIRCLE
                            else -> RIGHT_CIRCLE
                        }))
                    }
                } else {
                    if (child.tag != "head") {
                        selectList.add(it)
                        if (top != child.top) {
                            rectList.add(DrawableStatue(left, top, right, bottom, when {
                                leftCircleTag -> LEFT_CIRCLE
                                else -> -1
                            }))
                            leftCircleTag = false
                            top = child.top
                            left = child.left
                            right = child.right
                            bottom = child.bottom
                        } else {
                            right = child.right
                        }
                        if (it == endPosition) {
                            rectList.add(DrawableStatue(left, top, right, bottom, when {
                                leftCircleTag -> BOTH_CIRCLE
                                else -> RIGHT_CIRCLE
                            }))
                        }
                    } else {
                        rectList.add(DrawableStatue(left, top, right, bottom, when {
                            leftCircleTag -> BOTH_CIRCLE
                            else -> RIGHT_CIRCLE
                        }))
                        leftCircleTag = true
                        top = -1
                    }
                }
                if (child is DateChildView) {
                    child.setTextColor(Color.WHITE)
                }
            }
            showNotice("共${selectList.size}天", endView!!)
            invalidate()
        }
    }

    private val TR = ViewUtils.getDp(R.dimen.dp_3)
    private fun getTransX(view: View): Int {
        return when {
            view == endView -> 0
            isAligeParentLeft(view) -> -view.measuredWidth / 2 - TR
            isAligeParentRight(view)
            -> view.measuredWidth / 2 + TR
            else -> 0
        }
    }

    private fun isAligeParentRight(view: View): Boolean {
        return view.right >= measuredWidth - paddingRight - 20
    }

    private fun isAligeParentLeft(view: View): Boolean {
        return view.left <= paddingLeft + 20
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
        setMeasuredDimension(size, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = paddingTop
        var left = paddingLeft
        var childDateNum = 0
        var bottom: Int
        var right: Int
        //Log.e("TAG", "childCount=$childCount, ")
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
                //Log.e("TAG", "onLayout：$left $top $right $bottom")
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
        removeAllViews()
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
                it.isHead -> getChildHeadView(it.toChinaDate())
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
                            hasSelectPosition = index
                        }
                    }
                }
            })
        }
        if (hasSelectPosition >= 0) {
            post {
                onChildClick.onClick(getChildAt(hasSelectPosition))
            }
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
            layoutParams = LayoutParams(MATCH_PARENT, childSlideLength)
            gravity = Gravity.CENTER
            setTextColor(ViewUtils.getColor(R.color.color_333333))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
            text = s
            tag = "head"
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
    }

    private fun getChildView(s: String, color: Int): TextView {
        return DateChildView(context).apply {
            layoutParams = LayoutParams(childSlideLength, childSlideLength)
            gravity = Gravity.CENTER
            setTextColor(color)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_13))
            text = s
            setOnClickListener(onChildClick)
        }
    }

    private fun getEmptyView(): View {
        return View(context).apply {
            layoutParams = LayoutParams(childSlideLength, childSlideLength)
        }
    }

    private fun setDateDayDate(calendar: Calendar, dateDay: DateDay, date: Int = -1) {
        dateDay.year = calendar[Calendar.YEAR]
        dateDay.month = calendar[Calendar.MONTH] + 1
        dateDay.date = date
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

        fun toChinaDate(): String {
            return "${year}年${TextUtils.format(month)}月"
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

    fun setHasSelectDate(calendar: Calendar?) {
        hasCalendarSelect = calendar
        setNumForMonth(5)
    }

    fun setMountDate(num: Int) {
        setNumForMonth(num)
    }

    fun getSelectDays(): MutableList<String> {
        TextUtils.compareSmallToMore(selectList)
        val list = ArrayList<String>(selectList.size)
        selectList.forEach {
            list.add(dateList[it].toDateString())
        }
        return list
    }

    fun cancelNotice() {
        //noticeView?.visibility = View.GONE
    }

    override fun dispatchDraw(canvas: Canvas) {
        rectList.forEach {
            Log.e("TAG", "rect=$it")
            val drawable = getRectBackground(it.mode)
            drawable.setBounds(it.left, it.top, it.right, it.bottom)
            drawable.draw(canvas)
        }
        super.dispatchDraw(canvas)
    }

    private val dp19 = ViewUtils.getFDp(R.dimen.dp_19)
    private fun getRectBackground(mode: Int): Drawable {
        return GradientDrawable().apply {
            setColor(ViewUtils.getColor(R.color.color_theme))
            when (mode) {
                //1、2两个参数表示左上角，3、4表示右上角，5、6表示右下角，7、8表示左下角
                LEFT_CIRCLE -> cornerRadii = floatArrayOf(dp19, dp19, 0f, 0f, 0f, 0f, dp19, dp19)
                RIGHT_CIRCLE -> cornerRadii = floatArrayOf(0f, 0f, dp19, dp19, dp19, dp19, 0f, 0f)
                BOTH_CIRCLE -> cornerRadius = dp19
            }
        }

    }

    private fun recover() {
        cleanDataView()
        invalidate()
        selectStatue = NONE_SELECT
    }

    private fun cleanDataView() {
        startView?.let {
            it.background = ColorDrawable(Color.TRANSPARENT)
            it.setTextColor(Color.BLACK)
            it.textBottom = null
        }
        endView?.let {
            it.background = ColorDrawable(Color.TRANSPARENT)
            it.setTextColor(Color.BLACK)
            it.textBottom = null
        }
        selectList.forEach {
            val childView = getChildAt(it)
            if (childView is DateChildView) {
                childView.setTextColor(Color.BLACK)
            }
        }
        startView = null
        endView = null
        rectList.clear()
        selectList.clear()
    }

    private class DrawableStatue(left: Int, top: Int, right: Int, bottom: Int, mode: Int) {
        var left = -1
        var top = -1
        var right = -1
        var bottom = -1
        var mode = -1

        init {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
            this.mode = mode
        }

        override fun toString(): String {
            return "DrawableStatue(left=$left, top=$top, right=$right, bottom=$bottom, mode=$mode)"
        }

    }

    private fun showNotice(text: String, view: View) {
        noticeView?.let {
            it.text = text
            it.tranX = getTransX(view)
            it.visibility = View.VISIBLE
            val leftMiddle = view.left + view.measuredWidth / 2f
            val top = view.top
            it.post {
                val widthHalf = it.measuredWidth / 2f
                var fl = leftMiddle - widthHalf
                if (fl < 0) {
                    fl = 0f
                } else {
                    if (leftMiddle + widthHalf > measuredWidth) {
                        fl = (measuredWidth - it.measuredWidth).toFloat()
                    }
                }
                it.x = fl
                it.y = (top - it.measuredHeight).toFloat()
            }
        }
    }
}
