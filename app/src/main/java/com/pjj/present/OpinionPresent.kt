package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.OpinionContract
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/01/19 17:16。
 * describe：P层
 */
class OpinionPresent(view: OpinionContract.View) : BasePresent<OpinionContract.View>(view, OpinionContract.View::class.java), OpinionContract.Present {

    override fun loadOpinionTask(msg: String) {
        mView.showWaiteStatue()
        retrofitService.addUserMessage(PjjApplication.application.userId,msg,object :RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java){
            override fun successResult(t: ResultBean) {
                mView.cancelWaiteStatue()
                mView.loadSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }
}
