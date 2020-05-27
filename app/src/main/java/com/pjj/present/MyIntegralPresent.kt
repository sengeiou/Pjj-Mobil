package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MyIntegralContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.MyIntegralBean

/**
 * Create by xinheng on 2019/04/04 15:32。
 * describe：P层
 */
class MyIntegralPresent(view: MyIntegralContract.View) : BasePresent<MyIntegralContract.View>(view, MyIntegralContract.View::class.java), MyIntegralContract.Present {

    override fun loadMyIntegralTask() {
        if (null == PjjApplication.application.userId) {
            mView.showNotice("用户验证不通过")
            return
        }

        IntegralRetrofitService.instance.getUserIntegralByUserId(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<MyIntegralBean>(MyIntegralBean::class.java) {
            override fun successResult(t: MyIntegralBean) {
                mView.updateMyIntegral(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }
}
