package com.pjj.present

import com.pjj.contract.SpeedScreenContract
import com.pjj.intent.RetrofitService
import com.pjj.module.SpeedBean

/**
 * Created by XinHeng on 2019/03/15.
 * describe：
 */
class SpeedScreenPresent(view: SpeedScreenContract.View) : BasePresent<SpeedScreenContract.View>(view, SpeedScreenContract.View::class.java), SpeedScreenContract.Present {
    override fun loadSpeedDataTask() {
        retrofitService.getSpellIdentification(object : RetrofitService.CallbackClassResult<SpeedBean>(SpeedBean::class.java) {
            override fun successResult(t: SpeedBean) {
                if (t.data != null)
                    mView.updateSpeedData(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
            }
        })
    }
}