package com.pjj.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewStub
import android.widget.ListView
import android.widget.TextView

import com.pjj.R
import com.pjj.contract.SelectTemplateContract
import com.pjj.module.BianMinBean
import com.pjj.module.UserTempletBean
import com.pjj.module.xspad.PayManage
import com.pjj.module.xspad.XspManage
import com.pjj.present.SelectTemplatePresent
import com.pjj.utils.*
import com.pjj.view.adapter.BianMianAdapter
import com.pjj.view.adapter.RandomAreaAdapter
import com.pjj.view.adapter.TemplateAdapter
import com.pjj.view.dialog.*
import kotlinx.android.synthetic.main.activity_select_bm_ping_template.*
import java.util.*

/**
 * Create by xinheng on 2018/11/29 13:44。
 * describe：选择便民拼屏模板
 */
class SelectBMPingTemplateActivity : BaseActivity<SelectTemplatePresent>(), SelectTemplateContract.View {
    private lateinit var rv_template: RecyclerView
    private var templateAdapter: TemplateAdapter? = null
    private var listView: ListView? = null
    private var showPhone = "1"
    private var showName = "1"
    private lateinit var mianHandler: Handler
    private lateinit var randomAdapter: RandomAreaAdapter
    private var price: String = "0"
    private var communityIds: String = ""
    /**
     * 0元标识
     */
    private var price0 = false
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private var paySuccess: Boolean = false
    private lateinit var payDialogHelp: PayDialogHelp

    override fun titleLeftClick() {
        if (paySuccess) {
            startActivity(Intent(this@SelectBMPingTemplateActivity, MainActivity::class.java).putExtra("index", 1))
            finish()
            return
        }
        if (deleteTag) {
            if (TextUtils.isNotEmptyList(XspManage.getInstance().buildList)) {
                XspManage.getInstance().buildList.clear()
            }
        }
        super.titleLeftClick()
    }

    override fun onBackPressed() {
        if (paySuccess) {
            startActivity(Intent(this@SelectBMPingTemplateActivity, MainActivity::class.java).putExtra("index", 1))
            finish()
            return
        }
        if (deleteTag) {
            if (TextUtils.isNotEmptyList(XspManage.getInstance().buildList)) {
                XspManage.getInstance().buildList.clear()
            }
        }
        super.onBackPressed()
    }

    /**
     * 前往订单页面
     */
    private fun startOrderView(index: Int) {
        //默认审核中页面
        startActivity(Intent(this@SelectBMPingTemplateActivity, MainActivity::class.java).putExtra("index", index))
        finish()
    }

    private lateinit var nowCalendar: Calendar
    private var selectStarHour = 0
    private var payType: Int = -1
    private var sizeXsp: Int = 0
    private val dateDialog: DateDialog by lazy {
        DateDialog(this).apply {
            setSelectDay(nowCalendar)
            onDateSelectListener = object : DateDialog.OnDateSelectListener {
                override fun dateSelect(dates: String) {}

                override fun dateSelect(calendar: Calendar) {
                    nowCalendar = calendar
                    tvDate.text = DateUtils.getSf(calendar.time)
                }

            }
        }
    }
    private val timeDialog: SelectStartHourDialog by lazy {
        SelectStartHourDialog(this).apply {
            onClickListener = object : SelectStartHourDialog.OnClickListener {
                override fun onSure(leftIndex: Int): Boolean {
                    selectStarHour = leftIndex
                    var s = "${TextUtils.format(leftIndex)}:00"
                    tvTime.text = s
                    return true
                }
            }
        }
    }

    companion object {
        val SCOPE_AREA = "scope_area"
        fun start(context: Context, scope: String) {
            context.startActivity(Intent(context, SelectBMPingTemplateActivity::class.java).putExtra(SCOPE_AREA, scope))
        }
    }

    private var scope_area: String? = ""
    private var deleteTag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_bm_ping_template)
        setTitle("拼屏信息发布")
        XspManage.getInstance().orderContent = null
        XspManage.getInstance().nameTypePing = null
        XspManage.getInstance().tagColorPing = -1
        randomAdapter = RandomAreaAdapter().apply {
            onDeleteItemListener = object : RandomAreaAdapter.OnDeleteItemListener {
                override fun deleteItem() {
                    deleteTag = true
                    updateAreaList()
                }
            }
        }
        expandableListTime.adapter = randomAdapter
        scope_area = intent.getStringExtra(SCOPE_AREA)
        updateAreaList()
        mianHandler = Handler()
        listView = findViewById<ViewStub>(R.id.stub_bian_min).inflate() as ListView
        mPresent = SelectTemplatePresent(this).apply {
            loadUserTemplateListTask()
        }
        tvDate = findViewById(R.id.tv_date)
        tvTime = findViewById(R.id.tv_time)
        nowCalendar = Calendar.getInstance(Locale.CHINA)
        tvDate.text = DateUtils.getSf(nowCalendar.time)
        tv_sure.setOnClickListener(onClick)
        tvDate.setOnClickListener(onClick)
        tvTime.setOnClickListener(onClick)
        tv_type.setOnClickListener(onClick)
        tv_color.setOnClickListener(onClick)
        iniLeftDrawable()
        et_time_length.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                price = CalculateUtils.m1((TextUtils.calculateElevatovrXspCount(XspManage.getInstance().buildList, s.toString())[2] as Double).toFloat())
                ViewUtils.setHtmlText(tv_xsp_price, "预计订单金额：<font color=\"#40BBF7\">$price</font> 元")
            }
        })
        payDialogHelp = PayDialogHelp(this, object : PayDialogHelp.OnPayListener {
            override fun showNotice(msg: String) {
                this@SelectBMPingTemplateActivity.showNotice(msg)
            }

            override fun makeOrder(payType: Int) {
                PayManage.getInstance().payDialogHelp = payDialogHelp
                this@SelectBMPingTemplateActivity.payType = payType
                mPresent?.loadMakeOrderTask()
            }

            override fun reLoadPay(payType: Int, orderId: String) {
                updateMakeOrderSuccess(orderId)
            }

            override fun startOrderView(index: Int) {
                this@SelectBMPingTemplateActivity.startOrderView(index)
            }

            override fun paySuccess(payType: Int) {
                paySuccess = true
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

    private var dp16 = ViewUtils.getDp(R.dimen.dp_16)
    private fun iniLeftDrawable() {
        var dp19 = ViewUtils.getDp(R.dimen.dp_19)
        var dp18 = ViewUtils.getDp(R.dimen.dp_18)
        setLeftDrawable(tv_ad_name, ViewUtils.getDrawable(R.mipmap.ad).apply {
            setBounds(0, 0, dp18, dp19)
        })
        setLeftDrawable(tv_show_person, ViewUtils.getDrawable(R.mipmap.unselect).apply {
            setBounds(0, 0, dp16, dp16)
        })
        setLeftDrawable(tv_show_phone, ViewUtils.getDrawable(R.mipmap.unselect).apply {
            setBounds(0, 0, dp16, dp16)
        })
        tv_show_person.setOnClickListener(onClick)
        tv_show_phone.setOnClickListener(onClick)
    }

    private fun setLeftDrawable(tv: TextView, drawable: Drawable) {
        tv.setCompoundDrawables(drawable, null, null, null)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_date -> dateDialog.show()
            R.id.tv_time -> timeDialog.show()
            R.id.tv_type -> SelectBMTypeActivity.instanceForResult(this, 101, XspManage.getInstance().nameTypePing)
            R.id.tv_color -> SelectBgColorActivity.instanceForResult(this, 102)
            R.id.tv_sure -> {
                if (TextUtils.isEmpty(XspManage.getInstance().nameTypePing)) {
                    showNotice("请选择分类")
                    return
                }
                if (XspManage.getInstance().tagColorPing == -1) {
                    showNotice("请选择背景颜色")
                    return
                }
                if (sizeXsp <= 0) {
                    showNotice("请选择广告屏")
                    return
                }
                if (TextUtils.isEmpty(tvDate.text.toString())) {
                    showNotice("请选择起始发布日期")
                    return
                }
                var startTime = tvTime.text.toString()
                if (TextUtils.isEmpty(startTime) || "请选择" == startTime) {
                    showNotice("请选择起始发布时间")
                    return
                }
                var time_length = et_time_length.text.toString()
                if (TextUtils.isEmpty(time_length)) {
                    showNotice("请输入发布时长")
                    return
                }
                var timeLength = 0
                try {
                    timeLength = time_length.toInt()
                } catch (e: Exception) {
                }
                if (timeLength <= 0) {
                    showNotice("请输入有效的发布时长")
                    return
                }
                if (TextUtils.isEmpty(XspManage.getInstance().orderContent)) {
                    showNotice("请选择模板")
                    return
                }
                XspManage.getInstance().releaseTime_ = time_length
                XspManage.getInstance().releaseTime = time_length

                price?.let {
                    var priceF = 0.0f
                    try {
                        priceF = it.toFloat()
                    } catch (e: Exception) {
                    }
                    when {
                        priceF == 0f -> {
                            price0 = true
                            mPresent?.loadMakeOrderTask()
                        }//0 元直接生成订单，支付成功
                        priceF < 0f -> showNotice("价格错误")
                        else -> payDialogHelp.showPayTypeDialog(it)
                    }

                }
            }
            R.id.tv_show_person -> {
                if (showName == "0") {
                    showName = "1"
                    setLeftDrawable(tv_show_person, ViewUtils.getDrawable(R.mipmap.unselect).apply {
                        setBounds(0, 0, dp16, dp16)
                    })
                } else {
                    showName = "0"
                    setLeftDrawable(tv_show_person, ViewUtils.getDrawable(R.mipmap.select).apply {
                        setBounds(0, 0, dp16, dp16)
                    })
                }
            }
            R.id.tv_show_phone -> {
                if (showPhone == "0") {
                    showPhone = "1"
                    setLeftDrawable(tv_show_phone, ViewUtils.getDrawable(R.mipmap.unselect).apply {
                        setBounds(0, 0, dp16, dp16)
                    })
                } else {
                    showPhone = "0"
                    setLeftDrawable(tv_show_phone, ViewUtils.getDrawable(R.mipmap.select).apply {
                        setBounds(0, 0, dp16, dp16)
                    })
                }
            }
        }
    }

    /**
     * 更新模板列表
     */
    override fun updateTemplateListView(dataList: List<UserTempletBean.DataBean>) {
        templateAdapter?.let {
            it.setList(dataList)
            if (it.selectPosition > 0) {
                rv_template.scrollToPosition(it.selectPosition)
            }
        }
    }

    override fun updateBMTemplateListView(dataList: List<BianMinBean.DataBean>) {
        listView?.adapter = BianMianAdapter(dataList).apply {
            //selectPosition = position
            onItemSelectListener = object : BianMianAdapter.OnItemSelectListener {
                override fun preview(content: String) {
                    if (TextUtils.isEmpty(XspManage.getInstance().nameTypePing)) {
                        showNotice("请选择分类")
                        return
                    }
                    if (XspManage.getInstance().tagColorPing == -1) {
                        showNotice("请选择背景颜色")
                        return
                    }
                    PreviewXspBmPingActivity.startActivity(this@SelectBMPingTemplateActivity, content, XspManage.getInstance().tagColorPing, XspManage.getInstance().nameTypePing,
                            when (isShowName()) {
                                "0" -> null
                                else -> "屏加加科技有限公司"
                            },
                            when (isShowPhone()) {
                                "0" -> null
                                else -> "4001251818"
                            })
                }
            }
        }
    }

    override fun updateMakeOrderSuccess(orderId: String) {
        payDialogHelp.orderId_ = orderId
        if (price0) {
            cancelWaiteStatue()
            payDialogHelp.paySuccess()
        } else {
            when (payType) {
                PayOrderDialog.PAY_ZHIFUBAO -> mPresent?.loadAliPayTask(orderId)
                PayOrderDialog.PAY_WEIXIN -> {
                    mPresent?.loadWeiXinPayTask(orderId)
                }
                PayOrderDialog.PAY_YINLIAN -> {
                }
            }
        }
    }

    private fun updateAreaList() {
        var buildList = XspManage.getInstance().buildList
        randomAdapter.list = buildList
        if (TextUtils.isEmpty(scope_area)) {
            tv_scope.visibility = View.GONE
            tv_area_size.visibility = View.GONE
        } else {
            //ViewUtils.setHtmlText(tv_scope, "发布范围：$scope_area   <font color=\"#40BBF7\">  ${buildList.size}个小区</font>")
            tv_scope.text = "发布范围：$scope_area"
            tv_area_size.text = "${buildList.size}个小区"
        }
        var count = TextUtils.calculateElevatovrXspCount(buildList, et_time_length.text.toString())
        var size = count[0] as Int
        sizeXsp = count[1] as Int
        price = CalculateUtils.m1((count[2] as Double).toFloat())
        ViewUtils.setHtmlText(tv_xsp_count, "随机屏幕数量：<font color=\"#40BBF7\">$sizeXsp</font> 面")
        ViewUtils.setHtmlText(tv_xsp_price, "预计订单金额：<font color=\"#40BBF7\">$price</font> 元")
        tv_elevator_num.text = "$size 部电梯"
        tv_xsp_num.text = "$sizeXsp 面屏幕"
        communityIds = count[3] as String
    }

    override fun updateMakeOrderFail(error: String?) {
        showNotice(error)
    }

    override fun getPlayDate(): String {
        return tv_date.text.toString()
    }

    override fun getPlayTime(): String {
        return tvTime.text.toString().replace(":00", "")
    }

    override fun isShowName(): String {
        return showName
    }

    override fun isShowPhone(): String {
        return showPhone
    }

    override fun getCommunityIds(): String {
        return communityIds
    }

    override fun payCipherSuccess(orderInfo: String, orderId: String) {
        payDialogHelp.payForOrderInfo(orderInfo, payType)
    }

    override fun payResult(result: Boolean, msg: String?) {
        payDialogHelp.setPayResult(result, msg)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == 101 && resultCode == Activity.RESULT_OK -> {
                data?.let {
                    var name = it.getStringExtra(SelectBMTypeActivity.BM_TYPE_TAG)
                    XspManage.getInstance().nameTypePing = name
                    tv_type.text = name
                }
            }
            requestCode == 102 && resultCode == Activity.RESULT_OK -> {
                data?.let {
                    var colorTag = it.getIntExtra(SelectBgColorActivity.BG_COLOR_TAG, -1)
                    if (colorTag != -1) {
                        Log.e("TAG", "onActivityResult:colorTag=$colorTag ")
                        var bgColor = XspManage.getBgColor(colorTag)
                        XspManage.getInstance().tagColorPing = colorTag
                        tv_color.text = ""
                        view_color.visibility = View.VISIBLE
                        view_color.background = ColorDrawable(bgColor)
                    }
                }
            }
            else -> {

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        PayManage.getInstance().clear()
        super.onDestroy()
    }
}
