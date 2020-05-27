package com.pjj.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.ExchangeInfBean
import com.pjj.module.ExchangeRecordBean
import com.pjj.module.GoodsExplainBean
import com.pjj.module.ResultBean
import com.pjj.present.BasePresent
import com.pjj.utils.DateUtils
import com.pjj.utils.TextUtils
import com.pjj.view.dialog.CallPhoneDialog
import kotlinx.android.synthetic.main.activity_exchange_record_inf.*
import kotlinx.android.synthetic.main.layout_goods_list_item.*

class ExchangeRecordInfActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        @JvmStatic
        fun startActivity(activity: Activity, data: ExchangeRecordBean.ExchangeRecordData) {
            activity.startActivity(Intent(activity, ExchangeRecordInfActivity::class.java).putExtra("inf_data", data))
        }

        @JvmStatic
        fun startActivityForResult(activity: Activity, data: ExchangeRecordBean.ExchangeRecordData, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, ExchangeRecordInfActivity::class.java).putExtra("inf_data", data), requestCode)
        }
    }

    private var orderId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_record_inf)
        setBlackTitle("订单详情")
        val recordData = intent.getParcelableExtra<ExchangeRecordBean.ExchangeRecordData>("inf_data")
        loadPhone()
        if (null == recordData) {
            loadIntegralRule(intent.getStringExtra("orderId"))
        } else {
            setInf(recordData)
        }
        tv_sure_goods.setOnClickListener(onClick)
        tv_kefu.setOnClickListener(onClick)
        tv_copy_order.setOnClickListener(onClick)
        tv_copy_wuliu.setOnClickListener(onClick)
    }

    @SuppressLint("SetTextI18n")
    private fun setInf(it: ExchangeRecordBean.ExchangeRecordData) {
        orderId = it.orderId
        tv_person_name.text = "收货人：${it.name}"
        tv_person_address.text = "收货地址：${it.position}${it.address}"
        tv_person_phone.text = it.phone
        Glide.with(this).load(PjjApplication.integralFilePath + it.goodsPicture).into(iv_goods)
        tv_goods_statue.text = when (it.status) {
            "1" -> {
                tv_wuliu.text = "暂无物流信息"
                tv_copy_wuliu.visibility = View.GONE
                "待发货"
            }
            "2" -> {
                tv_sure_goods.visibility = View.VISIBLE
                tv_wuliu.text = "正在运输中"
                tv_wuliu_inf.text = "物流信息：${it.courierNumber}(${it.express})"
                "待收货"
            }
            else -> {
                tv_sure_goods.visibility = View.GONE
                tv_wuliu.text = "已完成"
                tv_wuliu_inf.text = "物流信息：${it.courierNumber}(${it.express})"
                "已完成"
            }
        }
        tv_goods_name.text = it.goodsName
        //tv_goods_num.text = "x1"
        tv_integral.text = "${it.goodsIntegral}金币"
        tv_freight.text = "运费：${it.postCost} 元"
        tv_no.text = "订单编号：${it.orderId}"
        tv_order_time.text = "下单时间：${DateUtils.getSf1(it.createTime)}"
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_kefu -> {
                if (TextUtils.isEmpty(phone)) {
                    showNotice("暂无客服电话")
                    return
                }
                callDialog.show()
            }
            R.id.tv_copy_order -> {
                val clip = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clip.text = tv_no.text.substring(5)// 复制
                noticeDialog.updateImage(R.mipmap.smile)
                noticeDialog.setNotice("复制成功")
            }
            R.id.tv_copy_wuliu -> {
                val clip = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                var text = tv_wuliu_inf.text
                var indexOf = text.indexOf("(")
                if (indexOf == -1) {
                    indexOf = text.length
                }
                clip.text = text.substring(5, indexOf)// 复制
                noticeDialog.updateImage(R.mipmap.smile)
                noticeDialog.setNotice("复制成功")
            }
            R.id.tv_sure_goods -> orderId?.let {
                sureGoods(it)
            }
        }
    }

    /**
     * 确认收货
     */
    private fun sureGoods(orderId: String) {
        showWaiteStatue()
        IntegralRetrofitService.instance.confirmReceivingGoods(orderId, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                cancelWaiteStatue()
                loadIntegralRule(orderId, TextUtils.isEmpty(phone))
            }

            override fun fail(error: String?) {
                super.fail(error)
                showNotice(error)
            }
        })
    }

    private var phone: String? = null
    private fun loadIntegralRule(orderId: String, phoneTag: Boolean = true) {
        this.orderId = orderId
        showWaiteStatue()
        IntegralRetrofitService.instance.getIntegralOrderByOrderId(orderId, object : RetrofitService.CallbackClassResult<ExchangeInfBean>(ExchangeInfBean::class.java) {
            override fun successResult(t: ExchangeInfBean) {
                cancelWaiteStatue()
                t.data?.let {
                    setInf(it)
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                cancelWaiteStatue()
                showNotice(error)
            }
        })
    }

    private fun loadPhone() {
        IntegralRetrofitService.instance.getIntegralRule("1", object : RetrofitService.CallbackClassResult<GoodsExplainBean>(GoodsExplainBean::class.java) {
            override fun successResult(t: GoodsExplainBean) {
                val phone = t.data.phone
                cancelWaiteStatue()
                phone?.let {
                    callDialog.phone = it
                    this@ExchangeRecordInfActivity.phone = it.replace("-", "")
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                cancelWaiteStatue()
                showNotice(error)
            }
        })
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }

    private val callDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(this).apply {
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phone")))
                }
            }
        }
    }

    override fun showNotice(error: String?) {
        noticeDialog.updateImage(R.mipmap.cry_white)
        super.showNotice(error)
    }
}
