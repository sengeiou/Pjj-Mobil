package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MallOrderContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.*

/**
 * Create by xinheng on 2019/05/18 17:13。
 * describe：P层
 */
class MallOrderPresent(view: MallOrderContract.View) : BasePresent<MallOrderContract.View>(view, MallOrderContract.View::class.java), MallOrderContract.Present {
    override fun loadDeleteOrder(goodOrderId: String, storeId: String) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.deleteGoodsOrder(goodOrderId, storeId, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.deleteSuccess()
            }
        })
    }

    override fun loadData(statue: Int, pageNo: Int, pageNum: Int) {
        IntegralRetrofitService.instance.getGoodsOrderList(statue.toString(), PjjApplication.application.userId, pageNo, pageNum, object : RetrofitService.CallbackClassResult<MallOrderListBean>(MallOrderListBean::class.java) {
            override fun successResult(t: MallOrderListBean) {
                mView.updateData(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadCancelTask(goodOrderId: String, storeId: String, notes: String) {
        IntegralRetrofitService.instance.cancelShoppingOrder(goodOrderId, storeId, notes, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                mView.cancelSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadCancelWhy() {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.getCancelOrderInfo(object : RetrofitService.CallbackClassResult<CancelWhyBean>(CancelWhyBean::class.java) {
            override fun successResult(t: CancelWhyBean) {
                mView.cancelWaiteStatue()
                mView.updateCancelWhy(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadMakeOrderTask(goodOrderId: String, storeId: String, payType: String) {
        IntegralRetrofitService.instance.goShoppingPay(goodOrderId, storeId, payType, object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(t: CipherPayZhifubaoBean) {
                //mView.cancelSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
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

}
