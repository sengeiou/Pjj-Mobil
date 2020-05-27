package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewStub
import com.bumptech.glide.Glide
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.MallSureOrderContract
import com.pjj.module.GoodsAddressBean
import com.pjj.module.xspad.PayManage
import com.pjj.module.xspad.XspManage
import com.pjj.present.MallSureOrderPresent
import com.pjj.utils.CalculateUtils
import com.pjj.view.adapter.ShopCarAdapter
import com.pjj.view.dialog.LineMessageDialog
import com.pjj.view.dialog.PayDialogHelp
import com.pjj.view.dialog.PayOrderDialog
import kotlinx.android.synthetic.main.activity_mallsureorder.*
import kotlinx.android.synthetic.main.layout_address_item_include.*

/**
 * Create by xinheng on 2019/05/16 17:21。
 * describe：确认订单
 */
class MallSureOrderActivity : BaseActivity<MallSureOrderPresent>(), MallSureOrderContract.View {
    private var addressView: View? = null
    private var yunFei: String = ""
    private var price0 = false
    private var sumPrice = "0"
    private var adapterMall = ShopCarAdapter(true)
    private var shoppingCar = false
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mallsureorder)
        setBlackTitle("确认订单")

        val goods = XspManage.getInstance().integralGoods
        tv_yunfei.text = "运费：${goods.postCost}元"
        yunFei = goods.postCost.toString()
        tv_record.setOnClickListener(onClick)
        tv_record.isEnabled = false
        rv_goods.run {
            layoutManager = LinearLayoutManager(this@MallSureOrderActivity)
            adapter = adapterMall
        }
        shoppingCar = intent.getBooleanExtra("shoppingCar", false)
        adapterMall.list = goods.goods
        tv_add_address.setOnClickListener(onClick)
        mPresent = MallSureOrderPresent(this)
        try {
            val sum = goods.integral!!.toFloat()
            sumPrice = goods.integral!!
            tv_heji.text = "¥$sumPrice"
            price0 = sum == 0f
            mPresent?.loadGoodsAddress()
        } catch (e: Exception) {
            e.printStackTrace()
            tv_add_address.isEnabled = false
            showNotice("价格信息错误")
        }

    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_add_address -> CreateAddressActivity.startActivityForResult(this, 303, CreateAddressActivity.TYPE_CREATE)
            R.id.tv_record -> if (null == XspManage.getInstance().integralGoods.address) showNotice("您未添加收货地址") else {
                mPresent?.makeMallGoodsOrder(if (shoppingCar) "1" else "0")
                //payDialogHelp.showPayTypeDialog(sumPrice, false)
            }
        }
    }

    override fun noGoods() {
        lineMessageDialog.show()
    }

    private val lineMessageDialog: LineMessageDialog by lazy {
        LineMessageDialog(this).apply {
            onMessageDialogListener = object : LineMessageDialog.OnMessageDialogListener {
                override fun callClick() {
                    XspManage.getInstance().refreshTag.mallGoodsRefreshTag = true
                    finish()
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
        val goods = XspManage.getInstance().integralGoods
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
                    startActivityForResult(Intent(this@MallSureOrderActivity, SelectAddressActivity::class.java), 301)
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
        payDialogHelp.orderId_ = orderId
        if (price0) {
            makePaySuccess()
        } else {
            PayManage.getInstance().payDialogHelp = payDialogHelp
            payDialogHelp.showPayTypeDialog(sumPrice, false)
            //updateMakeOrderSuccess(orderId)
        }
    }

    private fun makePaySuccess() {
        var goods = XspManage.getInstance().integralGoods
        startActivity(Intent(this, ExchangeSuccessActivity::class.java)
                .putExtra("mallTag", true)
                .putExtra("name", goods.name)
                .putExtra("phone", goods.phone)
                .putExtra("orderId", payDialogHelp.orderId_)
                .putExtra("storeId", XspManage.getInstance().integralGoods.storeId)
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
                this@MallSureOrderActivity.showNotice(msg)
            }

            override fun makeOrder(payType: Int) {
                this@MallSureOrderActivity.payType = payType
                mPresent?.makeMallGoodsOrder(if (shoppingCar) "1" else "0")
            }

            override fun reLoadPay(payType: Int, orderId: String) {
                updateMakeOrderSuccess(orderId)
            }

            override fun startOrderView(index: Int) {
                if (index > 0) //0 等待支付
                    makePaySuccess()
                else {
                    startActivity(Intent(this@MallSureOrderActivity, MallHomepageActivity::class.java).putExtra("index", 2))
                }

            }

            override fun paySuccess(payType: Int) {
            }

            override fun loadAliPayResult(orderId: String) {
                //mPresent?.loadPayResult(orderId)
                //payDialogHelp.setPayResult(true, "")
                //startOrderView(1)
            }

            override fun loadWeiXinPayResult(orderId: String) {
                //mPresent?.loadPayResult(orderId)
                //payDialogHelp.setPayResult(true, "")
                //startOrderView(1)
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
