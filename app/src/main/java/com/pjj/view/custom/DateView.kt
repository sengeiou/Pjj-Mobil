package com.pjj.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.pjj.R
import com.pjj.PjjApplication

import com.pjj.utils.ViewUtils
import com.pjj.view.fragment.SelectDayTimeFragment

import java.util.Calendar

class DateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : LinearLayout(context, attrs, defStyle) {
    var year: Int = 0
    var month: Int = 0
    private var day = -1
    val days: ArrayList<Int> = ArrayList()
    private lateinit var adapter: MyAdapter
    private lateinit var dateList: ArrayList<DateViewBean>
    var type = SelectDayTimeFragment.MORE_DAY
    var onSelectOneListener: OnSelectOneListener? = null

    fun getDay(position: Int): Int {
        return dateList[position].day
    }

    init {
        orientation = LinearLayout.VERTICAL
    }

    fun setDate(calendar: Calendar, isThisMonth: Boolean = true) {
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH) + 1
        if (isThisMonth) {
            day = calendar.get(Calendar.DATE)
        }
        //添加星期
        //addView(LayoutInflater.from(context).inflate(R.layout.layout_date_item, this, false))
        //添加年月
        addView(ViewUtils.createTextView(context, "${year}年${month}月").apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setPadding(0, ViewUtils.getDp(R.dimen.dp_27), 0, ViewUtils.getDp(R.dimen.dp_27))
            background = ColorDrawable(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_13))
            setTextColor(ViewUtils.getColor(R.color.color_333333))
        })
        //添加日期
        val maxDay = calendar.getActualMaximum(Calendar.DATE)//当月最大日期数
        calendar.set(Calendar.DATE, 1)
        var week = calendar.get(Calendar.DAY_OF_WEEK)
        dateList = getDateList(maxDay, week)
        //Log.e("TAG", "日期数：" + dateList.size + ", " + maxDay + ", " + week)
        addGridRecycleView(dateList)
    }

    private fun getDateList(maxDay: Int, week: Int): ArrayList<DateViewBean> {
        var list = ArrayList<DateViewBean>()
        for (it in 1 until week) {
            list.add(DateViewBean())
        }
        var headDays = 8 - week
        (1..maxDay).forEach {
            var bean = DateViewBean()
            bean.isEnble = (it >= day)
            bean.day = it
            bean.isWeek = ((it - headDays) % 7 == 0) or ((it - headDays) % 7 == 1)
            list.add(bean)
        }
        return list
    }

    inner class MyAdapter(private val list: ArrayList<DateViewBean>) : BaseAdapter() {
        private val colorEnable = ContextCompat.getColor(PjjApplication.application, R.color.color_999999)
        private val colorRed = ContextCompat.getColor(PjjApplication.application, R.color.color_df3629)
        private val colorBlack = ContextCompat.getColor(PjjApplication.application, R.color.color_333333)
        private val color_40bbf7 = ContextCompat.getColor(PjjApplication.application, R.color.color_theme)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            //无回收
            return TextView(parent.context).apply {
                gravity = Gravity.CENTER
                layoutParams = LayoutParams(ViewUtils.getDp(R.dimen.dp_28), ViewUtils.getDp(R.dimen.dp_28))
                with(list[position]) {
                    text = string
                    when {
                        !isEnble -> {
                            setTextColor(colorEnable)
                            setBackgroundColor(Color.WHITE)
                            isEnabled = isEnble
                            return@with
                        }
                        isSelect -> {
                            setTextColor(Color.WHITE)
                            setBackgroundColor(color_40bbf7)
                            return@with
                        }
                        isWeek -> {
                            setTextColor(colorRed)
                            setBackgroundColor(Color.WHITE)
                            return@with
                        }
                        else -> {
                            setTextColor(colorBlack)
                            setBackgroundColor(Color.WHITE)
                            return@with
                        }
                    }
                }
            }
        }

        override fun getItem(position: Int): Any {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return list.size
        }

        var onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            with(list[position]) {
                if (isEnble) {
                    isSelect = !isSelect
                    if (isSelect) {
                        //days.add(position)
                        addSelectDay(position)
                        //days.remove(position)
                        removeSelectedDay(position)
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    private fun addSelectDay(position: Int) {
        if (type == SelectDayTimeFragment.ONE_DAY) {
            days.clear()
            onSelectOneListener?.selectOne(position)
        }
        days.add(position)
    }

    private fun removeSelectedDay(position: Int) {
        days.remove(position)
    }

    fun clearSelectedDay() {
        days.clear()
        adapter.notifyDataSetChanged()
    }

    private fun addGridRecycleView(list: ArrayList<DateViewBean>) {
        var gridView = GridViewForScrollView(context)
        gridView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        //gridView.horizontalSpacing=30
        gridView.verticalSpacing = ViewUtils.getDp(R.dimen.dp_12)
        var dp15 = ViewUtils.getDp(R.dimen.dp_15)
        gridView.setPadding(dp15, 0, -ViewUtils.getDp(R.dimen.dp_10), dp15)
        gridView.numColumns = 7
        adapter = MyAdapter(list)
        gridView.onItemClickListener = adapter.onItemClickListener
        gridView.adapter = adapter
        addView(gridView)
    }

    inner class DateViewBean {
        internal var day = -1
        internal var isEnble: Boolean = false
        internal var isWeek: Boolean = false
        internal var isSelect: Boolean = false
        val string: String
            get() = if (-1 == day) "" else day.toString()

        fun equals(dateViewBean: DateViewBean?): Boolean {
            return if (null == dateViewBean) {
                false
            } else {
                day == dateViewBean.day
            }
        }
    }

    interface OnSelectOneListener {
        fun selectOne(position: Int)
    }
}
