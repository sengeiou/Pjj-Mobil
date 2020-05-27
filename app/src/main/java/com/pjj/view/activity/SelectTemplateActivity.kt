package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.pjj.utils.SpaceItemDecoration
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.BianMianAdapter
import com.pjj.view.adapter.TemplateAdapter
import com.pjj.view.adapter.TemplateElevatorExpandableListAdapter
import com.pjj.view.dialog.DayHoursDialog
import com.pjj.view.dialog.PayDialogHelp
import com.pjj.view.dialog.PayOrderDialog
import kotlinx.android.synthetic.main.activity_select_template.*
import kotlinx.android.synthetic.main.layout_xsp_count_price.*

/**
 * Create by xinheng on 2018/11/29 13:44。
 * describe：选择模板
 */
class SelectTemplateActivity : BaseActivity<SelectTemplatePresent>(), SelectTemplateContract.View {
    private lateinit var rv_template: RecyclerView
    private var templateAdapter: TemplateAdapter? = null
    private var listView: ListView? = null
    private var showPhone = "1"
    private var showName = "1"
    /**
     * 0元标识
     */
    private var price0 = false
    private lateinit var payDialogHelp: PayDialogHelp
    var linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    private val daysHoursDialog by lazy {
        DayHoursDialog(this@SelectTemplateActivity)
    }
    private var paySuccess: Boolean = false

    override fun titleLeftClick() {
        if (paySuccess) {
            startActivity(Intent(this@SelectTemplateActivity, MainActivity::class.java).putExtra("index", 1))
            finish()
            return
        }
        super.titleLeftClick()
    }

    override fun onBackPressed() {
        if (paySuccess) {
            startActivity(Intent(this@SelectTemplateActivity, MainActivity::class.java).putExtra("index", 1))
            finish()
            return
        }
        super.onBackPressed()
    }

    /**
     * 前往订单页面
     */
    private fun startOrderView(index: Int) {
        cancelWaiteStatue()
        //默认审核中页面
        startActivity(Intent(this@SelectTemplateActivity, MainActivity::class.java).putExtra("index", index))
        finish()
    }

    private val allPrice: String?
        get() {
            return intent.getStringExtra("xspAllPrice")
        }
    private var payType: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_template)
        setTitle("选择发布模板")
        XspManage.getInstance().orderContent = null
        if (XspManage.getInstance().adType == 2 || XspManage.getInstance().adType == 4) {//1 DIY类型 2便民信息 3随机diy传媒 4 随机便民
            listView = findViewById<ViewStub>(R.id.stub_bian_min).inflate() as ListView
        } else {
            var view = findViewById<ViewStub>(R.id.stub_diy_).inflate()
            rv_template = view.findViewById(R.id.rv_template)
            var iv_left = view.findViewById<View>(R.id.iv_left)
            var iv_right = view.findViewById<View>(R.id.iv_right)
            templateAdapter = TemplateAdapter(this).apply {
                onPreviewListener = object : TemplateAdapter.OnPreviewListener {
                    override fun preview(path: String, type: String) {
                        startPreview(path, type)
                    }
                }
            }
            rv_template.layoutManager = linearLayoutManager
            rv_template.adapter = templateAdapter
            rv_template.addItemDecoration(SpaceItemDecoration(this, RecyclerView.HORIZONTAL,
                    ViewUtils.getDp(R.dimen.dp_10), Color.WHITE))
            iv_left.setOnClickListener(onClick)
            iv_right.setOnClickListener(onClick)
        }
        mPresent = SelectTemplatePresent(this).apply {
            loadUserTemplateListTask()
        }
        expandableListTime.setAdapter(TemplateElevatorExpandableListAdapter().apply {
            //setDeleteTag(false)
            setOnDeleteListener(object : TemplateElevatorExpandableListAdapter.OnDeleteListener {
                override fun expanded(groupPosition: Int) {
                    if (expandableListTime.isGroupExpanded(groupPosition)) {
                        expandableListTime.collapseGroup(groupPosition)
                    } else {
                        expandableListTime.expandGroup(groupPosition)
                    }
                }

                override fun showHoursDialog(xspName: String, screenId: String) {
                    var list = XspManage.getInstance().listHashMap[screenId]
                    if (list != null) {
                        daysHoursDialog.updateContent(xspName, list)
                    }
                }

                override fun onDelete() {
                }
            })
        })
        tv_date.text = XspManage.getInstance().startDate
        tv_time.text = XspManage.getInstance().releaseTime
        tv_sure.setOnClickListener(onClick)
        iniLeftDrawable()
        var allPrice = allPrice
        ViewUtils.setXspInf(findViewById(R.id.rl_bottom), intent.getStringExtra("xspAllHours"), allPrice)
        payDialogHelp = PayDialogHelp(this, object : PayDialogHelp.OnPayListener {
            override fun showNotice(msg: String) {
                this@SelectTemplateActivity.showNotice(msg)
            }

            override fun makeOrder(payType: Int) {
                PayManage.getInstance().payDialogHelp = payDialogHelp
                this@SelectTemplateActivity.payType = payType
                mPresent?.loadMakeOrderTask()
            }

            override fun reLoadPay(payType: Int, orderId: String) {
                updateMakeOrderSuccess(orderId)
            }

            override fun startOrderView(index: Int) {
                this@SelectTemplateActivity.startOrderView(index)
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
            R.id.iv_left -> rv_template.smoothScrollBy(-ViewUtils.getDp(R.dimen.dp_80), 0)
            R.id.iv_right -> rv_template.smoothScrollBy(ViewUtils.getDp(R.dimen.dp_80), 0)
            R.id.tv_sure -> {
                if (TextUtils.isEmpty(XspManage.getInstance().orderContent)) {
                    showNotice("请选择模板")
                    return
                }
                allPrice?.let {
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
     * 启动预览页
     */
    private fun startPreview(path: String, type: String) {
        PreviewXspActivity.startActivity(this, path, type)
    }

    /**
     * 更新模板列表
     */
    override fun updateTemplateListView(dataList: List<UserTempletBean.DataBean>) {
        templateAdapter?.setList(dataList)
    }

    override fun updateBMTemplateListView(dataList: List<BianMinBean.DataBean>) {
        listView?.adapter = BianMianAdapter(dataList).apply {
            onItemSelectListener = object : BianMianAdapter.OnItemSelectListener {
                override fun preview(content: String) {
                    PreviewXspActivity.startActivity(this@SelectTemplateActivity, content, "0",
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

    override fun updateMakeOrderFail(error: String?) {
        showNotice(error)
    }

    override fun getPlayDate(): String {
        return tv_date.text.toString()
    }

    override fun getPlayTime(): String {
        return tv_time.text.toString()
    }

    override fun isShowName(): String {
        return showName
    }

    override fun isShowPhone(): String {
        return showPhone
    }

    override fun getCommunityIds(): String {
        return ""
    }

    override fun payCipherSuccess(orderInfo: String, orderId: String) {
        payDialogHelp.payForOrderInfo(orderInfo, payType)
    }

    override fun payResult(result: Boolean, msg: String?) {
        payDialogHelp.setPayResult(result, msg)
    }

    override fun onDestroy() {
        PayManage.getInstance().clear()
        super.onDestroy()
    }
}
