package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MallGoodsDetailsContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.GoodsDetailBean
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：P层
 */
class MallGoodsDetailsPresent(view: MallGoodsDetailsContract.View) : BasePresent<MallGoodsDetailsContract.View>(view, MallGoodsDetailsContract.View::class.java), MallGoodsDetailsContract.Present {

    override fun loadGoodsDetail(goodsId: String) {
        IntegralRetrofitService.instance.getGoodsDetails(goodsId, object : RetrofitService.CallbackClassResult<GoodsDetailBean>(GoodsDetailBean::class.java) {
            override fun successResult(t: GoodsDetailBean) {
                mView.updateGoods(t)
            }
        })
    }

    override fun loadAddShopCar(goodsId: String, specificId: String?, goodsNum: Int) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.addShoppingCart(PjjApplication.application.userId, goodsId, specificId, goodsNum, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                mView.cancelWaiteStatue()
                mView.addShopCarSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                if (null != error && error.contains("库存不足")) {
                    mView.refresh()
                }
                mView.showNotice(error)
            }
        })
    }
}
