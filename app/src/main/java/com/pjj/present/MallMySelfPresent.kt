package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MallMySelfContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.MyIntegralBean

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：P层
 */
class MallMySelfPresent(view: MallMySelfContract.View) : BasePresent<MallMySelfContract.View>(view, MallMySelfContract.View::class.java), MallMySelfContract.Present {

    override fun loadMyIntegral() {
        if (null == PjjApplication.application.userId) {
            mView.updateMyIntegral("")
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
