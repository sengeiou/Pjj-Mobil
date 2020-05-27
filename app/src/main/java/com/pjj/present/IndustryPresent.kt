package com.pjj.present

import com.pjj.contract.IndustryContract
import com.pjj.intent.RetrofitService
import com.pjj.module.IndustryBean

/**
 * Create by xinheng on 2018/12/11 16:03。
 * describe：P层
 */
class IndustryPresent(view: IndustryContract.View) : BasePresent<IndustryContract.View>(view, IndustryContract.View::class.java), IndustryContract.Present {

    override fun loadIndustryTask() {
        retrofitService.loadInfTask("1", object : RetrofitService.CallbackClassResult<IndustryBean>(IndustryBean::class.java) {
            override fun successResult(t: IndustryBean) {
                mView.updateList(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }
}
