package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.MallOrderInfContract
import com.pjj.module.CancelWhyBean
import com.pjj.module.MallOrderInfBean
import com.pjj.module.MallOrderListBean
import com.pjj.present.MallOrderInfPresent
import com.pjj.utils.CalculateUtils
import com.pjj.utils.DateUtils
import com.pjj.utils.TextUtils
import com.pjj.view.dialog.CallPhoneDialog
import com.pjj.view.dialog.MallCancelOrderDialog
import kotlinx.android.synthetic.main.activity_exchange_record_inf.*
import kotlinx.android.synthetic.main.activity_mallorderinf.*
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_copy_order
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_copy_wuliu
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_no
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_order_time
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_person_address
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_person_name
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_person_phone
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_wuliu
import kotlinx.android.synthetic.main.activity_mallorderinf.tv_wuliu_inf

/**
 * Create by xinheng on 2019/05/20 13:54。
 * describe：订单详情
 */
class MallOrderInfActivity : BaseActivity<MallOrderInfPresent>(), MallOrderInfContract.View {
    private lateinit var goodOrderId: String
    private lateinit var storeId: String
    //类型 0待支付 1待收货 3已完成
    //private var type: String? = null
    private var notFaHuo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mallorderinf)
        setBlackTitle("订单详情")
        mPresent = MallOrderInfPresent(this)
        goodOrderId = intent.getStringExtra("goodOrderId")
        storeId = intent.getStringExtra("storeId")
        // type = intent.getStringExtra("type")
        mPresent?.loadMallOrderInfTask(goodOrderId, storeId)
        tv_call.setOnClickListener(onClick)
        tv_copy_order.setOnClickListener(onClick)
        tv_copy_wuliu.setOnClickListener(onClick)
        tv_cancel_order.setOnClickListener(onClick)
        tv_pay.setOnClickListener(onClick)
    }

    @SuppressLint("SetTextI18n")
    override fun updateView(bean: MallOrderInfBean.DataBean) {
        //地址
        tv_person_name.text = "收货人：${bean.name}"
        tv_person_phone.text = bean.phone
        tv_person_address.text = "收货地址：${bean.address}"
        var textHint = "温馨提示：若您支付未成功银行卡已扣款，钱款将于7个工作日内退回银行卡。"
        tv_goods_statue.text = when (bean.statusX) {
            "0" -> {
                tv_wuliu_inf.visibility = View.GONE
                tv_copy_wuliu.visibility = View.GONE
                tv_wuliu.text = "等待买家付款"
                "待支付"
            }
            "1" -> {
                notFaHuo = true
                ll_bottom.visibility = View.GONE
                tv_cancel_order.visibility = View.GONE
                tv_wuliu.text = "暂无物流信息"
                tv_pay.text = "确认收货"
                textHint = "温馨提示：您可以复制物流信息，百度搜索进行物流查询"
                "待发货"
            }
            "2" -> {
                tv_cancel_order.visibility = View.GONE
                tv_wuliu.text = "正在运输中"
                tv_pay.text = "确认收货"
                textHint = "温馨提示：您可以复制物流信息，百度搜索进行物流查询"
                "待收货"
            }
            "3" -> {
                tv_pay.visibility = View.GONE
                tv_cancel_order.text = "删除订单"
                ll_bottom.visibility = View.GONE
                tv_wuliu.text = "已完成"
                textHint = "温馨提示：您可以复制物流信息，百度搜索进行物流查询"
                "已完成"
            }
            else -> {//4
                tv_pay.visibility = View.GONE
                if (!TextUtils.isEmpty(bean.notes)) {
                    tv_wuliu_reason.visibility = View.VISIBLE
                    tv_wuliu_reason.text = bean.notes
                }
                textHint = ""
                tv_cancel_order.text = "删除订单"
                tv_wuliu.text = "交易关闭"
                "已取消"
            }
        }
        tv_hint.text = textHint
        addChildView(ll_parent, bean.orderDetail)
        tv_no.text = "订单编号：${bean.goodOrderId}"
        tv_order_time.text = "下单时间：${DateUtils.getSf1(bean.createTime)}"
        var wuliu_inf = bean.courierNumber
        if (TextUtils.isEmpty(wuliu_inf)) {
            tv_copy_wuliu.visibility = View.GONE
            wuliu_inf = "暂无"
        } else {
            wuliu_inf += (if (TextUtils.isEmpty(bean.express)) "" else "(${bean.express})")
        }
        tv_wuliu_inf.text = "物流信息：$wuliu_inf"

        tv_sum_price.text = "合计：¥${CalculateUtils.m1(bean.goodsPrice)}"
        tv_freight.text = "运费：${bean.postage} 元"
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_call -> callDialog.show()
            R.id.tv_copy_order -> {
                val clip = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clip.text = tv_no.text.substring(5)// 复制
                smillTag = true
                showNotice("复制成功")
                smillTag = false
            }
            R.id.tv_copy_wuliu -> {
                val clip = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                var text = tv_wuliu_inf.text
                var indexOf = text.indexOf("(")
                if (indexOf == -1) {
                    indexOf = text.length
                }
                clip.text = text.substring(5, indexOf)// 复制
                smillTag = true
                showNotice("复制成功")
                smillTag = false
            }
            R.id.tv_pay -> {
                val text = tv_pay.text
                when (text) {
                    "立即支付" -> {
                    }
                    "确认收货" -> {
                        if (notFaHuo) {
                            showNotice("请等待卖家发货")
                            return
                        }
                        mPresent?.loadSureOrder(goodOrderId, storeId)
                    }
                }
            }
            R.id.tv_cancel_order -> {
                val text = tv_cancel_order.text
                when (text) {
                    "删除订单" -> deleteDialog.show()
                    "取消订单" -> {
                        if (null == cancelDialog) {
                            mPresent?.loadCancelWhy()
                        } else {
                            cancelDialog!!.show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addChildView(parent: ViewGroup, goodsList: MutableList<MallOrderInfBean.DataBean.OrderDetailBean>?) {
        if (parent.childCount > 0) {
            parent.removeAllViews()
        }
        val oneTag = (goodsList?.size ?: 0) == 1

        goodsList?.forEach {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_mall_goods_list_child_item, parent, false)
            val imageView = view.findViewById<ImageView>(R.id.iv_goods)
            Glide.with(parent).load(PjjApplication.integralFilePath + it.goodsPicture).into(imageView)
            view.findViewById<TextView>(R.id.tv_goods_name).text = it.goodsName
            //view.findViewById<TextView>(R.id.tv_describe).text=it.goodsName
            view.findViewById<TextView>(R.id.tv_goods_num).text = "x" + it.goodsNumber
            if (oneTag) {
                view.findViewById<TextView>(R.id.tv_price).visibility = View.GONE
            } else {
                view.findViewById<TextView>(R.id.tv_price).text = "¥" + CalculateUtils.m1(it.goodsPrice)
            }
            //view.findViewById<TextView>(R.id.tv_price).text = "¥" + CalculateUtils.m1(it.goodsPrice)
            parent.addView(view)
        }
    }

    private val callDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(this).apply {
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:4001251818")))
                }
            }
        }
    }

    override fun cancelSuccess() {
        setResult(303)
        finish()
    }

    override fun deleteSuccess() {
        setResult(303)
        finish()
    }

    override fun sureOrderSuccess() {
        setResult(303)
        finish()
    }

    override fun updateCancelData(datas: MutableList<CancelWhyBean.DataBean>) {
        if (null == cancelDialog) {
            cancelDialog = MallCancelOrderDialog(this).apply {
                onMallCancelOrderListener = object : MallCancelOrderDialog.OnMallCancelOrderListener {
                    override fun cancel(msg: String) {
                        mPresent?.loadCancelOrder(goodOrderId, storeId, msg)
                    }

                    override fun notice(msg: String) {
                        showNotice(msg)
                    }

                }
            }
        }
        cancelDialog!!.data = datas
        cancelDialog!!.show()
    }

    private var cancelDialog: MallCancelOrderDialog? = null
    private val deleteDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(this).apply {
            phone = "确认删除订单？"
            setTowText("确认", "取消", false)
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    mPresent?.loadDeleteOrder(goodOrderId, storeId)
                }
            }
        }
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }
}
