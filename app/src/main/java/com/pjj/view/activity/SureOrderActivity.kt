package com.pjj.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.SureOrderContract
import com.pjj.module.GoodsAddressBean
import com.pjj.module.xspad.PayManage
import com.pjj.module.xspad.XspManage
import com.pjj.present.SureOrderPresent
import com.pjj.view.dialog.PayDialogHelp
import com.pjj.view.dialog.PayOrderDialog
import kotlinx.android.synthetic.main.activity_sureorder.*
import kotlinx.android.synthetic.main.layout_address_item_include.*
import kotlinx.android.synthetic.main.layout_goods_list_item.*

/**
 * Create by xinheng on 2019/04/04 15:26。
 * describe：确定订单
 */
class SureOrderActivity : BaseActivity<SureOrderPresent>(), SureOrderContract.View {
    private var addressView: View? = null
    private var yunFei: String = ""
    private var price0 = false
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sureorder)
        setBlackTitle("确认订单")

        val goods = XspManage.getInstance().integralGoods
        tv_heji.text = "${goods.integral}金币"
        tv_yunfei.text = "运费：${goods.postCost}元"
        yunFei = goods.postCost.toString()
        tv_record.setOnClickListener(onClick)
        tv_record.isEnabled = false
        tv_goods_statue.visibility = View.GONE
        tv_freight.visibility = View.GONE
        Glide.with(this).load(PjjApplication.integralFilePath + goods.goodsPicture).into(iv_goods)
        tv_goods_name.text = goods.describe
        tv_integral.text = "${goods.integral}金币"
        tv_add_address.setOnClickListener(onClick)
        mPresent = SureOrderPresent(this)
        try {
            price0 = goods.postCost?.toInt() == 0
            mPresent?.loadGoodsAddress()
        } catch (e: Exception) {
            e.printStackTrace()
            tv_add_address.isEnabled = false
            showNotice("运费信息错误")
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_add_address -> CreateAddressActivity.startActivityForResult(this, 303, CreateAddressActivity.TYPE_CREATE)
            R.id.tv_record -> if (null == XspManage.getInstance().integralGoods.address) showNotice("您未添加收货地址") else {
                if (price0) {
                    mPresent?.makeIntegralGoodsOrder()
                } else {
                    payDialogHelp.showPayTypeDialog(yunFei, false)
                }
            }
        }
    }

    override fun updateGoodsAddress(address: GoodsAddressBean.GoodsAddressData?) {
        tv_record.isEnabled = true
        if (null == address) {
            tv_add_address.visibility = View.VISIBLE
        } else {
            updateAddress(address.name!!, address.phone!!, address.position!!, address.describe!!)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateAddress(name: String, phone: String, position: String, address: String) {
        showAddressView()
        var goods = XspManage.getInstance().integralGoods
        goods.name = name
        goods.position = position
        goods.address = address
        goods.phone = phone
        tv_add_address.visibility = View.GONE
        tv_person_name.text = "收货人：$name"
        tv_person_phone.text = phone
        tv_person_address.text = "收货地址：$position$address"
    }

    private fun showAddressView() {
        if (null == addressView) {
            findViewById<ViewStub>(R.id.vs).inflate().apply {
                addressView = this
                setOnClickListener {
                    startActivityForResult(Intent(this@SureOrderActivity, SelectAddressActivity::class.java), 301)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            (requestCode == 301 || requestCode == 303) && resultCode == RESULT_OK -> {
                data?.let {
                    updateAddress(it.getStringExtra("name"), it.getStringExtra("phone"), it.getStringExtra("position"), it.getStringExtra("address"))
                }
            }
        }
    }

    override fun makeSuccess(orderId: String) {
        updateMakeOrderSuccess(orderId)
    }

    private fun makePaySuccess() {
        var goods = XspManage.getInstance().integralGoods
        startActivity(Intent(this, ExchangeSuccessActivity::class.java)
                .putExtra("name", goods.name)
                .putExtra("phone", goods.phone)
                .putExtra("orderId", payDialogHelp.orderId_)
                .putExtra("address", goods.position + goods.address)
        )
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }

    override fun payCipherSuccess(data: String, orderId: String) {
        payDialogHelp.payForOrderInfo(data, payType)
    }

    private var payType = -1
    private val payDialogHelp: PayDialogHelp by lazy {
        PayDialogHelp(this, object : PayDialogHelp.OnPayListener {
            override fun showNotice(msg: String) {
                this@SureOrderActivity.showNotice(msg)
            }

            override fun makeOrder(payType: Int) {
                this@SureOrderActivity.payType = payType
                mPresent?.makeIntegralGoodsOrder()
            }

            override fun reLoadPay(payType: Int, orderId: String) {
                updateMakeOrderSuccess(orderId)
            }

            override fun startOrderView(index: Int) {
                if (index > 0) //0 等待支付
                    makePaySuccess()
            }

            override fun paySuccess(payType: Int) {
            }

            override fun loadAliPayResult(orderId: String) {
                //mPresent?.loadPayResult(orderId)
                //payDialogHelp.setPayResult(true, "")
                startOrderView(1)
            }

            override fun loadWeiXinPayResult(orderId: String) {
                //mPresent?.loadPayResult(orderId)
                //payDialogHelp.setPayResult(true, "")
                startOrderView(1)
            }

            override fun loadYinHangPayResult(orderId: String) {
            }

        })
    }

    private fun updateMakeOrderSuccess(orderId: String) {
        payDialogHelp.orderId_ = orderId
        PayManage.getInstance().payDialogHelp = payDialogHelp
        if (price0) {
            cancelWaiteStatue()
            makePaySuccess()
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
}
