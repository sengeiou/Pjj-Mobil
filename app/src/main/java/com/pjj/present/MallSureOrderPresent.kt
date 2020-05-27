package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MallSureOrderContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.CipherPayZhifubaoBean
import com.pjj.module.GoodsAddressBean
import com.pjj.module.MakeOrderBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.TextUtils

/**
 * Create by xinheng on 2019/05/16 17:21。
 * describe：P层
 */
class MallSureOrderPresent(view: MallSureOrderContract.View) : BasePresent<MallSureOrderContract.View>(view, MallSureOrderContract.View::class.java), MallSureOrderContract.Present {

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

    override fun makeMallGoodsOrder(goodsList1: String) {
        mView.showWaiteStatue()
        val goods = XspManage.getInstance().integralGoods
        val goodsList = goods.integraGoodsId!!
        IntegralRetrofitService.instance.generateShoppingOrder(PjjApplication.application.userId, goodsList, goods.position!!, goods.address!!, goods.name!!, goods.phone!!, object : RetrofitService.CallbackClassResult<MakeOrderBean>(MakeOrderBean::class.java) {
            override fun successResult(t: MakeOrderBean) {
                mView.cancelWaiteStatue()
                mView.makeSuccess(t.orderId)
            }

            override fun fail(error: String?) {
                if (null != error && error.contains("库存不足")) {
                    mView.noGoods()
                } else {
                    super.fail(error)
                    mView.showNotice(error)
                }
            }
        }, goodsList1)
    }

    override fun loadAliPayTask(orderId: String) {
        //1支付宝   2微信支付
        IntegralRetrofitService.instance.goShoppingPay(orderId, XspManage.getInstance().integralGoods.storeId, "1", object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
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
        IntegralRetrofitService.instance.goShoppingPay(orderId, XspManage.getInstance().integralGoods.storeId, "2", object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
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
