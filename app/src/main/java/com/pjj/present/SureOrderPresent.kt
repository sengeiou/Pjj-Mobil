package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.SureOrderContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.CipherPayZhifubaoBean
import com.pjj.module.GoodsAddressBean
import com.pjj.module.MakeOrderBean
import com.pjj.module.ResultBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.TextUtils

/**
 * Create by xinheng on 2019/04/04 15:26。
 * describe：P层
 */
class SureOrderPresent(view: SureOrderContract.View) : BasePresent<SureOrderContract.View>(view, SureOrderContract.View::class.java), SureOrderContract.Present {

    override fun loadGoodsAddress() {
        IntegralRetrofitService.instance.getReceivingAddressList(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<GoodsAddressBean>(GoodsAddressBean::class.java) {
            override fun successResult(t: GoodsAddressBean) {
                if (TextUtils.isNotEmptyList(t.data)) {
                    mView.updateGoodsAddress(t.data!![0])
                } else {
                    mView.updateGoodsAddress(null)
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun makeIntegralGoodsOrder() {
        mView.showWaiteStatue()
        var goods = XspManage.getInstance().integralGoods
        IntegralRetrofitService.instance.generateIntegralOrder(PjjApplication.application.userId, goods.integral!!, goods.describe!!, goods.integraGoodsId!!, goods.position!!, goods.address!!, goods.postCost!!, goods.phone!!, goods.name!!, goods.goodsPicture!!, object : RetrofitService.CallbackClassResult<MakeOrderBean>(MakeOrderBean::class.java) {
            override fun successResult(t: MakeOrderBean) {
                mView.cancelWaiteStatue()
                mView.makeSuccess(t.orderId)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadAliPayTask(orderId: String) {
        IntegralRetrofitService.instance.goIntegralAlipay(orderId, object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(t: CipherPayZhifubaoBean) {
                var data = t.data
                if (TextUtils.isEmpty(data)) {
                    fail("支付信息错误")
                    return
                }
                mView.payCipherSuccess(data, orderId)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadWeiXinPayTask(orderId: String) {
        IntegralRetrofitService.instance.goIntegralWxpay(orderId, object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(t: CipherPayZhifubaoBean) {
                var data = t.data
                if (TextUtils.isEmpty(data)) {
                    fail("支付信息错误")
                    return
                }
                mView.payCipherSuccess(data, orderId)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }
}
