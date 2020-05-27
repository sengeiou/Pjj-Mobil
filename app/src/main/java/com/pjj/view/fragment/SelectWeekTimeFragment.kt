package com.pjj.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.pjj.R
import com.pjj.contract.ElevatorDateTimeContract
import com.pjj.module.parameters.ElevatorTime
import com.pjj.module.xspad.XspManage
import com.pjj.present.ElevatorDateTimePresent
import com.pjj.utils.CalculateUtils
import com.pjj.utils.DateUtils
import com.pjj.utils.TextUtils
import com.pjj.view.dialog.DateDialog
import com.pjj.view.dialog.SelectStartHourDialog
import kotlinx.android.synthetic.main.fragment_select_week_month_time.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by XinHeng on 2018/11/22.
 * describe：选择日期
 * 使用 [SelectDayTimeFragment.newInstance] 方法创建
 */

class SelectWeekTimeFragment : ABTimeFragment<ElevatorDateTimePresent>(), ElevatorDateTimeContract.View {
    private var mTimeType: String? = null
    private var selectStarHour = 0
    private lateinit var nowDay1: Calendar
    /**
     * 已选择的日期
     * yyyy-MM-dd
     */
    private lateinit var selectDays: String

    companion object {
        private const val ARG_PARAM1 = "mTimeType"
        /**
         *
         * 整周
         */
        const val WEEK_DAY = "week"
        /**
         * 整月
         */
        const val MONTH_DAY = "month"

        @JvmStatic
        fun newInstance(type: String) = SelectWeekTimeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, type)
            }
            Log.e("TAG", "tv_zheng_zhou")
        }
    }

    /**
     * 当前选择时间数
     * 周/月
     */
    private var nowNum = 1
    private lateinit var tv_date_end_hour: TextView
    private lateinit var tv_date: TextView
    private lateinit var tv_week: TextView
    private lateinit var tv_week_start: TextView

    private val dateDialog: DateDialog by lazy {
        DateDialog(activity as Context).apply {
            setSelectDay(nowDay1)
            onDateSelectListener = object : DateDialog.OnDateSelectListener {
                override fun dateSelect(dates: String) {}

                override fun dateSelect(calendar: Calendar) {
                    this@SelectWeekTimeFragment.calendar = calendar
                    this@SelectWeekTimeFragment.tv_date.text = DateUtils.getSf(calendar.time)
                    daysClick(nowNum, field)
                }
            }
        }
    }
    private val timeDialog: SelectStartHourDialog by lazy {
        SelectStartHourDialog(activity as Context).apply {
            onClickListener = object : SelectStartHourDialog.OnClickListener {
                override fun onSure(leftIndex: Int): Boolean {
                    selectStarHour = leftIndex
                    var s = "${TextUtils.format(selectStarHour)}:00"
                    this@SelectWeekTimeFragment.tv_date_end_hour.text = s
                    this@SelectWeekTimeFragment.tv_week.text = s
                    XspManage.getInstance().startDate = "${this@SelectWeekTimeFragment.tv_date.text} ${this@SelectWeekTimeFragment.tv_week.text}至${this@SelectWeekTimeFragment.tv_date_end.text} ${this@SelectWeekTimeFragment.tv_date_end_hour.text}"
                    daysClick(nowNum, field)
                    return true
                }
            }
        }
    }
    private lateinit var explain: String

    private lateinit var calendar: Calendar
    /**
     * calendar 参数
     */
    private val field: Int by lazy {
        when (mTimeType) {
            WEEK_DAY -> {
                tv_week_start.text = "发布周数"
                explain = "周"
                tv1.text = "1周"
                tv2.text = "2周"
                tv3.text = "3周"
                tv4.visibility = View.VISIBLE
                space_last.visibility = View.VISIBLE
                iv_week_name.setImageResource(R.mipmap.week_size)
                tv4.text = "4周"
                Calendar.WEEK_OF_MONTH
            }
            else -> {
                tv_week_start.text = "发布月数"
                explain = "个月"
                tv1.text = "1个月"
                tv2.text = "2个月"
                tv3.text = "3个月"
                tv4.visibility = View.GONE
                iv_week_name.setImageResource(R.mipmap.month_size)
                space_last.visibility = View.GONE
                Calendar.MONTH
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mTimeType = it.getString(ARG_PARAM1)
        }
        mPresent = ElevatorDateTimePresent(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = Calendar.getInstance()
        tv_date_end_hour = view.findViewById(R.id.tv_date_end_hour)
        tv_date = view.findViewById(R.id.tv_date)
        tv_week = view.findViewById(R.id.tv_week)
        tv_week_start = view.findViewById(R.id.tv_week_start)
        //selectStarHour = calendar[Calendar.HOUR_OF_DAY]
        var s = "${TextUtils.format(selectStarHour)}:00"
        this@SelectWeekTimeFragment.tv_date_end_hour.text = s
        this@SelectWeekTimeFragment.tv_week.text = s
        nowDay1 = calendar
        tv_date.text = DateUtils.getSf(calendar.time)
        initGroupDays()//checked
        tv1.isChecked = true
        tv_date.setOnClickListener {
            dateDialog.show()
        }
        tv_week.setOnClickListener {
            if (TextUtils.isEmpty(selectDays)) {
                toast("请选择发布日期")
            } else {
                timeDialog.show()
            }
        }
    }

    /**
     * 更新天
     * @param data
     */
    override fun updateDateDays(data: String) {
        showWaiteStatue()
        var elevatorTime = ElevatorTime()
        elevatorTime.screentIds = onFragmentInteractionListener.getXspIds()
        elevatorTime.orderType = onFragmentInteractionListener.getAdType()
        elevatorTime.selectDate = data
        mPresent.loadElevatorTimeTask(elevatorTime)
    }

    /**
     * 展示等待状态
     */
    override fun showWaiteStatue() {
    }

    /**
     * 取消等待状态
     */
    override fun cancelWaiteStatue() {
    }

    /**
     * 展示信息
     * @param notice 消息
     */
    override fun showNotice(notice: String?) {
        toast(notice)
    }

    /**
     * 更新可用时间 并集
     * @param allUseTime
     */
    override fun updateTimes(allUseTime: MutableList<Int>) {
        listHour = ArrayList()
        (0..23).forEach {
            if (allUseTime.contains(it)) {
                (listHour as ArrayList<Int>).add(it)
            }
        }
        updatePrice()
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_select_week_month_time
    }

    private fun initGroupDays() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {//2131296559
                R.id.tv1 -> daysClick(1, field)
                R.id.tv2 -> daysClick(2, field)
                R.id.tv3 -> daysClick(3, field)
                R.id.tv4 -> daysClick(4, field)
            }
        }
    }

    private fun daysClick(num: Int, field: Int) {
        nowNum = num
        var s = "$num $explain"
        tv_week_num.text = s
        var calendar = this.calendar.clone() as Calendar
        calendar.add(field, num)
        //var dateEnd = calendar.get(Calendar.DATE)
        tv_date_end.text = DateUtils.getSf(calendar.time)
        createDayData(this.calendar, calendar)
    }

    private fun updatePrice() {
        mPresent?.let { it ->
            it.elevatorTimeBean?.let {
                //TODO 屏幕最终价格
                allXspPrice = it.getAllXspPrice(selectDays, listHour, onFragmentInteractionListener.getTimeDiscountBean())
                //Log.e("TAG", "money=$allXspPrice")
                allHours = it.hourAll
                XspManage.getInstance().playType = when (mTimeType) {
                    WEEK_DAY -> "1"
                    else -> "2"
                }
                XspManage.getInstance().releaseTime = tv_week_num.text.toString()
                XspManage.getInstance().releaseTime_ = tv_week_num.text.toString()[0].toString()
                XspManage.getInstance().startDate = "${tv_date.text} ${tv_week.text}至${tv_date_end.text} ${tv_date_end_hour.text}"
                Log.e("TAG", "startDate=" + XspManage.getInstance().startDate)
                onFragmentInteractionListener.updateAllXspPrices("${CalculateUtils.m1(allXspPrice)}", allHours)
            }
        }
    }

    override fun updateXspPriceAndTime() {
        daysClick(nowNum, field)
        //updatePrice()
    }

    private fun createDayData(calendarOld: Calendar, calendarNew: Calendar) {
        selectDays = DateUtils.addDays(calendarOld, calendarNew, selectStarHour > 0)
        Log.e("TAG", "日期=$selectDays")
        updateDateDays(selectDays)
    }

    override fun getStartHour(): Int {
        return selectStarHour
    }

    override fun getStartDate(): String {
        return tv_date.text.toString()
    }

    override fun getEndDate(): String {
        return tv_date_end.text.toString()
    }
}