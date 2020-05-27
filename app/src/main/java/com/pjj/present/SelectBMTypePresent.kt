package com.pjj.present

import com.pjj.contract.SelectBMTypeContract
import com.pjj.intent.RetrofitService
import com.pjj.module.BmTypeBean

/**
 * Created by XinHeng on 2019/01/18.
 * describe：
 */
class SelectBMTypePresent(view: SelectBMTypeContract.View) : BasePresent<SelectBMTypeContract.View>(view, SelectBMTypeContract.View::class.java), SelectBMTypeContract.Present {
    override fun loadBmTypeTask() {
        mView.showWaiteStatue()
        retrofitService.loadInfTask("3", object : RetrofitService.CallbackClassResult<BmTypeBean>(BmTypeBean::class.java) {
            override fun successResult(t: BmTypeBean) {
                mView.updateList(t.data)
                mView.cancelWaiteStatue()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }


}