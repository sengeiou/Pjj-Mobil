package com.pjj.view.activity

import com.pjj.contract.PayContract
import com.pjj.module.xspad.PayManage
import com.pjj.present.PayPresent
import com.pjj.utils.CalculateUtils
import com.pjj.utils.Log
import com.pjj.view.dialog.PayDialogHelp
import com.pjj.view.dialog.PayOrderDialog

abstract class PayActivity<P : PayPresent<*>> : BaseActivity<P>(), PayContract.View {
    protected open var payType = -1
    protected open lateinit var payDialogHelp: PayDialogHelp
    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        payDialogHelp = PayDialogHelp(this, object : PayDialogHelp.OnPayListener {
            override fun showNotice(msg: String) {
                this@PayActivity.showNotice(msg)
            }

            override fun makeOrder(payType: Int) {
                this@PayActivity.payType = payType
                makeOrder()
            }

            override fun reLoadPay(payType: Int, orderId: String) {
                this@PayActivity.payType = payType
                PayManage.getInstance().payDialogHelp = payDialogHelp
                Log.e("TAG", "updateMakeOrderSuccess--$payType")
                when (payType) {
                    PayOrderDialog.PAY_ZHIFUBAO -> mPresent?.loadAliPayTask(orderId)
                    PayOrderDialog.PAY_WEIXIN -> mPresent?.loadWeiXinPayTask(orderId)
                    PayOrderDialog.PAY_YINLIAN -> {
                    }
                }
            }

            override fun startOrderView(index: Int) {
                payFinishStartOrderView(index)
            }

            override fun loadAliPayResult(orderId: String) {
                mPresent?.loadPayResult(orderId)
            }

            override fun loadWeiXinPayResult(orderId: String) {
                mPresent?.loadPayResult(orderId)
            }

            override fun loadYinHangPayResult(orderId: String) {
            }

            override fun paySuccess(payType: Int) {
                cancelWaiteStatue()
            }
        })
        PayManage.getInstance().payDialogHelp = payDialogHelp
    }

    protected open fun makeOrder() {
        mPresent?.loadPayOrderIdTask(getMakeOrderJson())
    }

    abstract fun getMakeOrderJson(): String
    abstract fun getFinalPayPrice(): Float
    abstract fun payFinishStartOrderView(index: Int)
    protected fun getZheKouTag(): Boolean {
        return false
    }

    override fun updateMakeOrderSuccess(orderId: String) {
        payDialogHelp.orderId_ = orderId
        cancelWaiteStatue()
        val price = getFinalPayPrice()
        if (price == 0f) {
            payDialogHelp.paySuccess()
        } else {
            if (orderId.isEmpty()) {
                showNotice("订单生成错误")
                return
            }
            payDialogHelp.showPayTypeDialog(CalculateUtils.m1(price), getZheKouTag())
        }
    }

    override fun payCipherSuccess(orderInfo: String, orderId: String) {
        payDialogHelp.payForOrderInfo(orderInfo, payType)
    }

    override fun payCipherFai(msg: String) {
        showNotice(msg)
    }

    override fun payResult(b: Boolean, s: String?) {
        payDialogHelp.setPayResult(b, s)
    }

}