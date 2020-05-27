package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.MakeElevatorOrderContract
import com.pjj.module.MakeOrderScreenBean
import com.pjj.module.MediaOrderInfBean
import com.pjj.module.parameters.NewMediaMakeOrder
import com.pjj.module.xspad.PayManage
import com.pjj.module.xspad.XspManage
import com.pjj.present.MakeElevatorOrderPresent
import com.pjj.utils.*
import com.pjj.view.adapter.OrderElevatorInfAdapter
import com.pjj.view.dialog.*
import kotlinx.android.synthetic.main.activity_make_elevator_order.*
import java.util.*

/**
 * Create by xinheng on 2019/03/27 16:33。
 * describe：电梯新传媒广告屏选择
 */
class MakeElevatorOrderActivity : BaseActivity<MakeElevatorOrderPresent>(), MakeElevatorOrderContract.View {
    private lateinit var payDialogHelp: PayDialogHelp
    private var payType: Int = -1
    private var price = 0f
    private var dates: MutableList<String>? = null
    private var playDate: String? = null//播放日期
    private var videoPlayTimes = 15
    private val dateDialog: DatePopuWindow by lazy {
        DatePopuWindow(this, false).apply {
            setHasSelectDate(Calendar.getInstance())
            onDateSelectListener = object : DatePopuWindow.OnDateSelectListener {
                override fun dismiss() {
                    this@MakeElevatorOrderActivity.tv_preview.visibility = View.VISIBLE
                    setTitle("订单确认")
                }

                @SuppressLint("SetTextI18n")
                override fun dateSelect(dates: MutableList<String>) {
                    if (dates.size == 0) {
                        return
                    }
                    this@MakeElevatorOrderActivity.dates = dates
                    this@MakeElevatorOrderActivity.tv_date_count.text = "共${dates.size}天"
                    var ss = ""
                    dates.forEach {
                        ss += ",$it"
                    }
                    XspManage.getInstance().newMediaData.screenIdList?.let {
                        var screenIds = ""
                        it.forEach { item ->
                            screenIds += ",${item.srceenId}"
                        }
                        mPresent?.loadUseTime(screenIds.substring(1), ss.substring(1), "9")
                    }
                }
            }
        }
    }
    private var sum_screen = 0
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_elevator_order)
        setTitle("订单确认")
        mPresent = MakeElevatorOrderPresent(this)
        val adapter = OrderElevatorInfAdapter()
        select(tv_5)
        rv_building.layoutManager = LinearLayoutManager(this)
        rv_building.adapter = adapter
        var communityList = XspManage.getInstance().newMediaData.elevatorCommunityList
        adapter.list = communityList
        var sum_elevator = 0
        communityList?.forEach {
            //price += it.price
            sum_screen += it.getScreenCount()
            sum_elevator += it.getElevatorCount()
        }
        ViewUtils.setHtmlText(tv_explain, "由于您选择的部分屏幕广告投放数量已饱和，自动为您选择可投放的屏幕    <font color=\"#00ABFE\">查看详情</font>")
        tv_sum_elevator.text = "电梯总数量：${sum_elevator}部"
        tv_sum_screen.text = "屏幕总数量：${sum_screen}面"
        //tv_sum_price.text = "${price}元"
        tv_date_start.setOnClickListener(onClick)
        tv_explain.setOnClickListener(onClick)
        tv_date_end.setOnClickListener(onClick)
        tv_date_count.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
        tv_explain.setOnClickListener(onClick)
        tv_5.setOnClickListener(onClick)
        tv_10.setOnClickListener(onClick)
        tv_15.setOnClickListener(onClick)
        tv_30.setOnClickListener(onClick)
        tv_defined.setOnClickListener(onClick)
        tv_60.setOnClickListener(onClick)
        tv_preview.setOnClickListener(onClick)
        tv_price_inf.setOnClickListener(onClick)
        tv_price_inf.visibility = View.GONE
        payDialogHelp = PayDialogHelp(this, object : PayDialogHelp.OnPayListener {
            override fun showNotice(msg: String) {
                this@MakeElevatorOrderActivity.showNotice(msg)
            }

            override fun makeOrder(payType: Int) {
                PayManage.getInstance().payDialogHelp = payDialogHelp
                this@MakeElevatorOrderActivity.payType = payType
                makeOrder()
            }

            override fun reLoadPay(payType: Int, orderId: String) {
                PayManage.getInstance().payDialogHelp = payDialogHelp
                this@MakeElevatorOrderActivity.payType = payType
                when (payType) {
                    PayOrderDialog.PAY_ZHIFUBAO -> mPresent?.loadAliPayTask(orderId)
                    PayOrderDialog.PAY_WEIXIN -> mPresent?.loadWeiXinPayTask(orderId)
                    PayOrderDialog.PAY_YINLIAN -> {
                    }
                }
            }

            override fun startOrderView(index: Int) {
                Log.e("TAG", "startOrderView  index=$index")
                //默认审核中页面
                startActivity(Intent(this@MakeElevatorOrderActivity, MainActivity::class.java).putExtra("index", index))
                finish()
            }

            override fun paySuccess(payType: Int) {
            }

            override fun loadAliPayResult(orderId: String) {
                mPresent?.loadPayResult(orderId)
            }

            override fun loadWeiXinPayResult(orderId: String) {
                mPresent?.loadPayResult(orderId)
            }

            override fun loadYinHangPayResult(orderId: String) {
            }

        })
    }

    private val color333 = ViewUtils.getColor(R.color.color_333333)
    @SuppressLint("SetTextI18n")
    private fun select(tv: TextView) {
        tv.background = ViewUtils.getDrawable(R.drawable.shape_theme_bg_3)
        tv.setTextColor(Color.WHITE)
        if (tv != tv_5) {
            tv_5.setTextColor(color333)
            tv_5.background = ViewUtils.getDrawable(R.drawable.shape_333_side_3)
        } else {
            videoPlayTimes = 15
        }
        if (tv != tv_30) {
            tv_30.setTextColor(color333)
            tv_30.background = ViewUtils.getDrawable(R.drawable.shape_333_side_3)
        } else {
            videoPlayTimes = 30
        }
        if (tv != tv_defined) {
            tv_defined.setTextColor(color333)
            tv_defined.background = ViewUtils.getDrawable(R.drawable.shape_333_side_3)
        } else {
            timeDefinedDialog.show()
        }
        if (tv != tv_60) {
            tv_60.setTextColor(color333)
            tv_60.background = ViewUtils.getDrawable(R.drawable.shape_333_side_3)
        } else {
            videoPlayTimes = 60
        }
        tv_sum_price.text = "${finalPrice()}元"
    }

    private fun finalPrice(): String {
        return CalculateUtils.m1(price * videoPlayTimes / 15)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_price_inf -> {
                startActivity(Intent(this, PriceInfActivity::class.java)
                        .putExtra("rate", videoPlayTimes / 15)
                        .putParcelableArrayListExtra("list_data", MediaOrderInfBean.chageScreenBeanList(XspManage.getInstance().newMediaData.screenIdList))
                        .putExtra("sum_price", XspManage.getInstance().newMediaData.price * videoPlayTimes / 15)
                )
            }
            R.id.tv_date_start, R.id.tv_date_end, R.id.tv_date_count -> {
                tv_preview.visibility = View.GONE
                setTitle("选择发布时间")
                dateDialog.showAsDropDown(titleView)
            }
            R.id.tv_explain -> {
                resultDialog.show()
            }
            R.id.tv_sure -> {
                if (!TextUtils.isNotEmptyList(dates)) {
                    showNotice("您还没有选择播放日期")
                    return
                }
                preMakeOrder()
            }
            R.id.tv_5 -> select(tv_5)
            R.id.tv_10 -> select(tv_10)
            R.id.tv_15 -> select(tv_15)
            R.id.tv_30 -> select(tv_30)
            R.id.tv_defined -> select(tv_defined)
            R.id.tv_60 -> select(tv_60)
            R.id.tv_preview -> {
                val template = XspManage.getInstance().newMediaData.selectedTemplate
                if (null != template) {
                    PreviewXspActivity.startActivity(this, template!!.fileUrl, template!!.type, true)
                } else {
                    if (null != XspManage.getInstance().newMediaData.preTowData)
                        PreviewXspActivity.startActivity(this, "", "", mbHidden = true, two = true)
                }
            }
        }
    }

    private fun preMakeOrder() {
        price0 = price <= 0f
        makeOrder()
    }

    private fun makeOrder() {
        val makeOrder = NewMediaMakeOrder().apply {
            showTime = videoPlayTimes
            templetIds = XspManage.getInstance().newMediaData.templetIds
            screen_time = XspManage.getInstance().newMediaData.dates
            authType = XspManage.getInstance().identityType.toString()
            orderType = XspManage.getInstance().adType.toString()
            userId = PjjApplication.application.userId
            playDate = this@MakeElevatorOrderActivity.playDate
            playTime = makeTimes()
            playType = "0"
        }
        if (null == makeOrder.screen_time) {
            showNotice("未选择屏幕")
            return
        }
        mPresent?.loadMakeOrder(makeOrder)
    }

    override fun updateMakeOrderFail(bean: MakeOrderScreenBean.MakeOrderData?) {
        if (null == bean) {
            showNotice("订单错误")
            return
        }
        resultDialog.setData(bean.useFullScreen, bean.offLineScreen)
    }

    private fun makeTimes(): String {
        var buff = StringBuffer()
        (0..23).forEach {
            buff.append(it)
            if (it != 23)
                buff.append(",")
        }
        return buff.toString()
    }


    private var price0 = false
    override fun updateMakeOrderFail(error: String?) {
        showNotice(error)
    }

    override fun payCipherSuccess(orderInfo: String, orderId: String) {
        payDialogHelp.payForOrderInfo(orderInfo, payType)
    }

    override fun updateMakeOrderSuccess(orderId: String) {
        payDialogHelp.orderId_ = orderId
        cancelWaiteStatue()
        if (price0) {
            XspManage.getInstance().clearNewMediaData()
            payDialogHelp.paySuccess()
        } else {
            payDialogHelp.showPayTypeDialog(finalPrice(), false)
        }
    }

    override fun payResult(result: Boolean, s: String?) {
        payDialogHelp.setPayResult(result, s)
    }

    @SuppressLint("SetTextI18n")
    override fun updateResult(dateCount: Int, screenSum: Int, elevatorSum: Int) {
        cancelWaiteStatue()
        Log.e("TAG", "$screenSum $dateCount ${dates?.size} $sum_screen")
        if (dateCount > 0) {
            dateDialog.dismiss()
            tv_price_inf.visibility = View.VISIBLE
            this.dates?.let { dates ->
                resultDialog.setDayNum(dateCount)
                if (sum_screen * dates.size != dateCount) {
                    tv_explain.visibility = View.VISIBLE
                    resultDialog.addFullScreenData(XspManage.getInstance().newMediaData.unUseScreenList)
                } else {
                    tv_explain.visibility = View.GONE
                }
                tv_sum_screen.text = "屏幕数量：${screenSum}面"
                //this@MakeElevatorOrderActivity.tv_date_count.text = "共${dateCount}天"
                val replace_s = dates[0].replace("-", ".")
                val replace_e = dates[dates.size - 1].replace("-", ".")
                var substring_s = replace_s.substring(5)
                this@MakeElevatorOrderActivity.tv_date_start.text = "$substring_s"
                var substring_e = replace_e.substring(5)
                this@MakeElevatorOrderActivity.tv_date_end.text = "$substring_e"
                price = XspManage.getInstance().newMediaData.price
                tv_sum_price.text = CalculateUtils.m1(price * videoPlayTimes / 15) + "元"
                playDate = if (dates.size == 1) replace_s else "$replace_s-$replace_e"
            }
        } else {
            showNotice("已选日期已排满\n请选择其他日期")
            tv_price_inf.visibility = View.GONE
        }
    }

    private val resultDialog: MakeOrderResultDialog by lazy {
        MakeOrderResultDialog(this).apply {
            onMakeOrderResultListener = object : MakeOrderResultDialog.OnMakeOrderResultListener {
                override fun reSelect() {
                }

                override fun nextMakeOrder() {
                    preMakeOrder()
                }
            }
        }
    }
    private val timeDefinedDialog: TimeDefinedDialog by lazy {
        TimeDefinedDialog(this).apply {
            onTimeDefinedListener = object : TimeDefinedDialog.OnTimeDefinedListener {
                override fun notice(msg: String) {
                    showNotice(msg)
                }

                @SuppressLint("SetTextI18n")
                override fun sure(num: Int) {
                    this@MakeElevatorOrderActivity.tv_defined.text = num.toString() + "秒"
                    videoPlayTimes = num
                }
            }
        }
    }

}
