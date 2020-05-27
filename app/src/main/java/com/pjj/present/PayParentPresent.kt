package com.pjj.present

import android.app.Activity
import com.pjj.PjjApplication
import com.pjj.contract.PayParentView
import com.pjj.view.dialog.PayDialogHelp
import com.pjj.view.dialog.PayOrderDialog

abstract class PayParentPresent<V : PayParentView>(activity: Activity, view: V, vClass: Class<V>) : BasePresent<V>(view, vClass) {
    protected var payDialogHelp: PayDialogHelp? = null
    protected var payType = -1
    private var listener = object : PayDialogHelp.OnPayListener {
        override fun showNotice(msg: String) {
            mView.showNotice(msg)
        }

        override fun makeOrder(payType: Int) {
            this@PayParentPresent.payType = payType
            this@PayParentPresent.makeOrder(payType)
        }

        override fun reLoadPay(payType: Int, orderId: String) {
            updateMakeOrderSuccess(payType, orderId)
        }

        override fun startOrderView(index: Int) {
            mView.startOrderView(index)
        }

        override fun loadAliPayResult(orderId: String) {
        }

        override fun loadWeiXinPayResult(orderId: String) {
        }

        override fun loadYinHangPayResult(orderId: String) {
        }

        override fun paySuccess(payType: Int) {
            mView.paySuccess()
        }

    }

    init {
        payDialogHelp = PayDialogHelp(activity, listener)
    }

    protected fun updateMakeOrderSuccess(payType: Int, orderId: String) {
        payDialogHelp?.orderId_ = orderId
        when (payType) {
            PayOrderDialog.PAY_ZHIFUBAO -> loadAliPayTask(orderId)
            PayOrderDialog.PAY_WEIXIN -> loadWeiXinPayTask(orderId)
            PayOrderDialog.PAY_YINLIAN -> {
                mView.showNotice(PjjApplication.Un_Show)
            }
        }
    }

    abstract fun makeOrder(payType: Int)
    abstract fun loadAliPayTask(orderId: String)
    abstract fun loadWeiXinPayTask(orderId: String)

    abstract fun loadAliPayResult(orderId: String)
    abstract fun loadWeiXinPayResult(orderId: String)
}