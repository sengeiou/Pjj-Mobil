package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.ExchangeRecordContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.ExchangeRecordBean

/**
 * Create by xinheng on 2019/04/11 10:27。
 * describe：P层
 */
class ExchangeRecordPresent(view: ExchangeRecordContract.View) : BasePresent<ExchangeRecordContract.View>(view, ExchangeRecordContract.View::class.java), ExchangeRecordContract.Present {

    override fun loadExchangeRecordTask(start: Int, num: Int, status: String) {
        IntegralRetrofitService.instance.getIntegralOrderList(PjjApplication.application.userId, start, num, object : RetrofitService.CallbackClassResult<ExchangeRecordBean>(ExchangeRecordBean::class.java) {
            override fun successResult(t: ExchangeRecordBean) {
                mView.updateExchangeRecord(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        }, status)
    }
}
