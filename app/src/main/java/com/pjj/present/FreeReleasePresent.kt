package com.pjj.present

import com.google.gson.JsonParser
import com.pjj.contract.FreeReleaseContract
import com.pjj.module.CipherPayZhifubaoBean
import com.pjj.module.ResultBean
import com.pjj.module.TopPriceBean
import com.pjj.module.parameters.ReleaseFreeOrder

class FreeReleasePresent(view: FreeReleaseContract.View) : PayPresent<FreeReleaseContract.View>(view, FreeReleaseContract.View::class.java), FreeReleaseContract.Present {
    override fun dealWithGoodsOrderResult(result: String) {
        val jsonElement = JsonParser().parse(result)
        if (jsonElement != null && jsonElement.isJsonObject) {
            val `object` = jsonElement.asJsonObject
            val flag = `object`.get("flag").asString
            if (ResultBean.SUCCESS_CODE1 == flag) {
                val element = `object`.get("topOrderId")
                val topOrderId = element?.asString ?: ""
                mView.updateMakeOrderSuccess(topOrderId)
            } else {
                val msg = `object`.get("msg").asString
                mView.showNotice(msg)
            }
        }
    }

    override fun loadPayOrderIdTask(json: String) {
    }

    override fun loadAliPayTask(orderId: String) {
        retrofitService.goTopOrderAlipay(orderId, object : PresentCallBack<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(cipherPayZhifubaoBean: CipherPayZhifubaoBean) {
                mView.payCipherSuccess(cipherPayZhifubaoBean.data, orderId)
            }

            override fun fail(error: String) {
                //super.fail(error);
                mView.payCipherFai(error)
            }
        })
    }

    override fun loadWeiXinPayTask(orderId: String) {
        retrofitService.goTopOrderWxpay(orderId, object : PresentCallBack<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(cipherPayZhifubaoBean: CipherPayZhifubaoBean) {
                mView.payCipherSuccess(cipherPayZhifubaoBean.data, orderId)
            }

            override fun fail(error: String) {
                //super.fail(error);
                mView.payCipherFai(error)
            }
        })
    }

    override fun loadTopPriceTask() {
        retrofitService.getTopPriceList(object : PresentCallBack<TopPriceBean>(TopPriceBean::class.java) {
            override fun successResult(topPriceBean: TopPriceBean) {
                mView.updatePrice(topPriceBean.data)
            }
        })
    }

    override fun loadReleaseFreeOrder(bean: ReleaseFreeOrder) {
        mView.showWaiteStatue()
        retrofitService.releaseFreeOrder(bean, payOrderIdCallBack)
    }

}