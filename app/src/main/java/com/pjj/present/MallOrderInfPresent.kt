package com.pjj.present

import com.pjj.contract.MallOrderInfContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.CancelWhyBean
import com.pjj.module.MallOrderInfBean
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/05/20 13:54。
 * describe：P层
 */
class MallOrderInfPresent(view: MallOrderInfContract.View) : BasePresent<MallOrderInfContract.View>(view, MallOrderInfContract.View::class.java), MallOrderInfContract.Present {

    override fun loadMallOrderInfTask(goodOrderId: String, storeId: String) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.getGoodsOrderDetail(goodOrderId, storeId, object : PresentCallBack<MallOrderInfBean>(MallOrderInfBean::class.java) {
            override fun successResult(t: MallOrderInfBean) {
                mView.cancelWaiteStatue()
                mView.updateView(t.data)
            }
        })
    }

    override fun loadCancelOrder(goodOrderId: String, storeId: String, notes: String) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.cancelShoppingOrder(goodOrderId, storeId, notes, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.cancelSuccess()
            }
        })
    }

    override fun loadDeleteOrder(goodOrderId: String, storeId: String) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.deleteGoodsOrder(goodOrderId, storeId, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.deleteSuccess()
            }
        })
    }

    override fun loadSureOrder(goodOrderId: String, storeId: String) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.confirmReceipt(goodOrderId, storeId, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.sureOrderSuccess()
            }
        })
    }

    override fun loadCancelWhy() {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.getCancelOrderInfo(object : PresentCallBack<CancelWhyBean>(CancelWhyBean::class.java) {
            override fun successResult(t: CancelWhyBean) {
                mView.cancelWaiteStatue()
                mView.updateCancelData(t.data)
            }
        })
    }
}
