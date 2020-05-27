package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MallShoppingCartContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean
import com.pjj.module.ShopCarBean

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：P层
 */
class MallShoppingCartPresent(view: MallShoppingCartContract.View) : BasePresent<MallShoppingCartContract.View>(view, MallShoppingCartContract.View::class.java), MallShoppingCartContract.Present {

    override fun loadShopCarList() {
        IntegralRetrofitService.instance.getShoppingCart(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<ShopCarBean>(ShopCarBean::class.java) {
            override fun successResult(t: ShopCarBean) {
                mView.updateShopCar(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadAddShopCar(goodsId: String, tag: Boolean, goodsNum: Int) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.changeShoppingCar(PjjApplication.application.userId, goodsId, goodsNum, tag, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                mView.cancelWaiteStatue()
                mView.addSuccess()
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

    override fun loadDeleteShopCarGoods(shoppingCartId: String) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.delShoppingCart(shoppingCartId, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.deleteSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadCheckGoodsStockTask(json: String) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.confirmGoodsStock(json, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                mView.cancelWaiteStatue()
                mView.checkPass(json)
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
