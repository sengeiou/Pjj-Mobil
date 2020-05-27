package com.pjj.present

import android.app.Activity
import com.pjj.PjjApplication
import com.pjj.contract.PayParentView
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.CipherPayZhifubaoBean
import com.pjj.module.MakeOrderBean
import com.pjj.module.MyPayResultBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.TextUtils

class MallPayPresent(activity: Activity, view: PayParentView) : PayParentPresent<PayParentView>(activity, view, PayParentView::class.java) {
    var storeId: String? = null
    var goodOrderId: String? = null
    fun showDialog(price: String?, zheKou0Tag: Boolean) {
        payDialogHelp?.showPayTypeDialog(price, zheKou0Tag)
    }

    override fun makeOrder(payType: Int) {
        if (!TextUtils.isEmpty(goodOrderId)) {
            updateMakeOrderSuccess(payType, goodOrderId!!)
            return
        }
        if (!mView.preMakeOrder(payType))
            startMakeOrder(payType)
    }

    fun startMakeOrder(payType: Int) {
        val goods = XspManage.getInstance().integralGoods
        val goodsList = goods.integraGoodsId!!
        IntegralRetrofitService.instance.generateShoppingOrder(PjjApplication.application.userId, goodsList, goods.position!!, goods.address!!, goods.name!!, goods.phone!!, object : RetrofitService.CallbackClassResult<MakeOrderBean>(MakeOrderBean::class.java) {
            override fun successResult(t: MakeOrderBean) {
                mView.cancelWaiteStatue()
                updateMakeOrderSuccess(payType, t.orderId)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadAliPayTask(orderId: String) {
        IntegralRetrofitService.instance.goShoppingPay(orderId, storeId, "1", callBack)
    }

    private val callBack = object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
        override fun successResult(t: CipherPayZhifubaoBean) {
            payDialogHelp?.payForOrderInfo(t.data, payType)
        }

        override fun fail(error: String?) {
            super.fail(error)
            mView.showNotice(error)
        }
    }

    override fun loadWeiXinPayTask(orderId: String) {
        IntegralRetrofitService.instance.goShoppingPay(orderId, storeId, "2", callBack)
    }

    override fun loadAliPayResult(orderId: String) {
        retrofitService.loadPayResultTask(orderId, object : RetrofitService.CallbackClassResult<MyPayResultBean>(MyPayResultBean::class.java) {
            override fun successResult(t: MyPayResultBean) {
                payDialogHelp?.setPayResult(true, "")
            }

            override fun fail(error: String?) {
                super.fail(error)
                payDialogHelp?.setPayResult(false, error)
            }
        })
    }

    override fun loadWeiXinPayResult(orderId: String) {
        loadAliPayResult(orderId)
    }
}