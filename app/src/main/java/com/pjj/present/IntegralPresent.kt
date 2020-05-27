package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.IntegralContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.IntegralBean

/**
 * Create by xinheng on 2019/04/10 15:40。
 * describe：P层
 */
class IntegralPresent(view: IntegralContract.View) : BasePresent<IntegralContract.View>(view, IntegralContract.View::class.java), IntegralContract.Present {

    override fun loadIntegral(pageNo: Int, pageNum: Int, type: String?) {
        IntegralRetrofitService.instance.getIntegralRecordList(PjjApplication.application.userId, pageNo, pageNum, object : RetrofitService.CallbackClassResult<IntegralBean>(IntegralBean::class.java) {
            override fun successResult(t: IntegralBean) {
                mView.updateData(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        }, type)
    }
}
