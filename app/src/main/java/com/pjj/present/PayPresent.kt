package com.pjj.present

import com.pjj.contract.PayContract
import com.pjj.intent.RetrofitService
import com.pjj.module.MyPayResultBean

abstract class PayPresent<V : PayContract.View>(view: V, vClass: Class<V>?) : BasePresent<V>(view, vClass), PayContract.Present {
    protected val payOrderIdCallBack = object : RetrofitService.MyCallback() {
        override fun success(s: String) {
            dealWithGoodsOrderResult(s)
        }

        override fun fail(error: String?) {
            super.fail(error)
            mView.showNotice(error)
        }
    }

    override fun loadPayResult(orderId: String) {
        mView.payResult(true, "")
        retrofitService.loadPayResultTask(orderId, object : RetrofitService.CallbackClassResult<MyPayResultBean>(MyPayResultBean::class.java) {
            override fun successResult(t: MyPayResultBean) {
                mView.payResult(true, "")
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.payResult(false, error)
            }
        })
    }

    abstract fun dealWithGoodsOrderResult(result: String)
}