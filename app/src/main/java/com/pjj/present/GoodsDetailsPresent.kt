package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.GoodsDetailsContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.GoodsExplainBean
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/04/04 14:35。
 * describe：P层
 */
class GoodsDetailsPresent(view: GoodsDetailsContract.View) : BasePresent<GoodsDetailsContract.View>(view, GoodsDetailsContract.View::class.java), GoodsDetailsContract.Present {

    override fun loadCheckIntegral(goodsIntegral: String) {
        IntegralRetrofitService.instance.isUserIntegralBalance(goodsIntegral, PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.passCheckIntegral()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadIntegralRule() {
        IntegralRetrofitService.instance.getIntegralRule("1", object : RetrofitService.CallbackClassResult<GoodsExplainBean>(GoodsExplainBean::class.java) {
            override fun successResult(t: GoodsExplainBean) {
                mView.updateIntegralRule(t.data.content)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }
}
