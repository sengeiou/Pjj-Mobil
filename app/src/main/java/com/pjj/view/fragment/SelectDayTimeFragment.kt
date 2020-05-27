package com.pjj.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.pjj.R
import com.pjj.contract.ElevatorDateTimeContract
import com.pjj.module.ElevatorTimeBean
import com.pjj.module.parameters.ElevatorTime
import com.pjj.module.xspad.XspManage
import com.pjj.present.ElevatorDateTimePresent
import com.pjj.utils.CalculateUtils
import com.pjj.utils.TextUtils
import com.pjj.view.adapter.HourSelectAdapter
import com.pjj.view.custom.DateViewGroup
import com.pjj.view.dialog.DateDialog
import com.pjj.view.dialog.HourTimeDialog
import kotlinx.android.synthetic.main.fragment_select_day_time.*
import java.util.*


/**
 * Created by XinHeng on 2018/11/22.
 * describe：选择日期
 * 使用 [SelectDayTimeFragment.newInstance] 方法创建
 */

class SelectDayTimeFragment : ABTimeFragment<ElevatorDateTimePresent>(), ElevatorDateTimeContract.View {
    private var mTimeType: String? = null
    private lateinit var nowDay1: Calendar

    companion object {
        private const val ARG_PARAM1 = "mTimeType"
        /**
         *
         * 单天
         */
        const val ONE_DAY = "one_day"
        /**
         * 多天
         */
        const val MORE_DAY = "more_day"

        @JvmStatic
        fun newInstance(type: String) = SelectDayTimeFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, type)
            }
        }
    }

    private var adapter: HourSelectAdapter? = null
    /**
     * 日期弹窗
     */
    private val dateDialog: DateDialog by lazy {
        DateDialog(activity as Context, 0, when (mTimeType == ONE_DAY) {
            true -> DateViewGroup.ONLY_ONE
            else -> DateViewGroup.MORE
        }).apply {
            setSelectDay(nowDay1)
            onDateSelectListener = object : DateDialog.OnDateSelectListener {
                override fun dateSelect(calendar: Calendar) {
                    updateDateDays(calendar.let {
                        "${it[Calendar.YEAR]}-${TextUtils.format(it[Calendar.MONTH] + 1)}-${TextUtils.format(it[Calendar.DATE])}"
                    })
                }

                override fun dateSelect(dates: String) {
                    updateDateDays(dates)
                }
            }
        }
    }
    private var tv_time: TextView? = null
    /**
     * 小时弹窗
     */
    private val timeDialog: HourTimeDialog by lazy {
        HourTimeDialog(activity as Context).apply {
            onClickListener = object : HourTimeDialog.OnClickListener {
                override fun onSure(leftIndex: Int, rightIndex: Int): Boolean {
                    this@SelectDayTimeFragment.tv_time?.text = "${TextUtils.format(leftIndex)}:00-${TextUtils.format(rightIndex)}:00"
                    adapter?.selectAllTime(leftIndex, rightIndex)
                    return true
                }
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
        //updateDateDays("2018-11-23")
        tv_time = view.findViewById(R.id.tv_time)
        var nowDay = com.pjj.utils.DateUtils.getNowDay()
        nowDay1 = Calendar.getInstance().apply {
            time = Date()
        }
        tv_date.text = nowDay
        updateDateDays(nowDay)
        tv_date.setOnClickListener {
            dateDialog.show()
        }
        tv_time?.setOnClickListener {
            if (TextUtils.isEmpty(tv_date.text.toString())) {
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
        tv_date.text = data
        updateTimes(data)
    }

    private fun updateTimes(data: String) {
        showWaiteStatue()
        var elevatorTime = ElevatorTime()
        elevatorTime.screentIds = onFragmentInteractionListener.getXspIds()
        elevatorTime.orderType = onFragmentInteractionListener.getAdType()
        elevatorTime.selectDate = data
        mPresent.loadElevatorTimeTask(elevatorTime)
    }

    /**
     * 更新可用时间 并集
     * @param allUseTime
     */
    override fun updateTimes(allUseTime: MutableList<Int>) {
        if (null == adapter) {
            adapter = HourSelectAdapter().apply {
                onSelectListener = object : HourSelectAdapter.OnSelectListener {
                    override fun onSelect(list: String) {
                        //tv_time.text = list
                        tv_time?.text = ""
                    }

                    override fun onSelectList(list: List<Int>) {
                        listHour = list
                        updatePrice()
                    }
                }
                grid.adapter = this
                grid.onItemClickListener = itemClickListener
            }
        }
        adapter?.run {
            setList(allUseTime)
        }
        listHour?.run {
            updatePrice()
        }
    }

    private fun updatePrice() {
        mPresent?.let { it ->
            it.elevatorTimeBean?.let {
                // 屏幕最终价格
                allXspPrice = it.getAllXspPrice(tv_date.text.toString(), listHour, onFragmentInteractionListener.getTimeDiscountBean())
                //Log.e("TAG", "money=$allXspPrice")
                allHours = it.hourAll
                XspManage.getInstance().startDate = tv_date.text.toString()
                XspManage.getInstance().playType = "0"
                XspManage.getInstance().releaseTime = listHour?.run {
                    XspManage.getInstance().releaseTime_ = ElevatorTimeBean.appendHours(listHour)
                    //Log.e("TAG", "releaseTime_= ${XspManage.getInstance().releaseTime_}")
                    ElevatorTimeBean.continuityHours(this)
                }
                onFragmentInteractionListener.updateAllXspPrices(CalculateUtils.m1(allXspPrice), allHours)
            }
        }
    }

    override fun updateXspPriceAndTime() {
        updateTimes(tv_date.text.toString())
        //updatePrice()
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_select_day_time
    }

    override fun getEndDate(): String {
        return ""
    }

    override fun getStartDate(): String {
        return ""
    }

    override fun getStartHour(): Int {
        return -1
    }

}