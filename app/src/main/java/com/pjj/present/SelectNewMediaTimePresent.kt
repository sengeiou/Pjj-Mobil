package com.pjj.present

import com.pjj.contract.SelectNewMediaTimeContract
import com.pjj.intent.RetrofitService
import com.pjj.module.TimeObject
import com.pjj.utils.Log

/**
 * Create by xinheng on 2019/03/29 17:17。
 * describe：P层
 */
class SelectNewMediaTimePresent(view: SelectNewMediaTimeContract.View) : BasePresent<SelectNewMediaTimeContract.View>(view, SelectNewMediaTimeContract.View::class.java), SelectNewMediaTimeContract.Present {
    override fun loadUseTime(screenIds: String, selectDate: String, orderType: String) {
        retrofitService.getElevatorTime(screenIds, selectDate, orderType, object : RetrofitService.MyCallback() {
            override fun success(s: String?) {
                Log.e("TAG", "success: $s")
                var timeOJ = TimeObject(s)
                if (timeOJ.isSuccess) {
                    var canUse = timeOJ.canUse(screenIds, selectDate)
                    mView.updateResult(canUse, selectDate)
                } else {
                    fail(timeOJ.msg)
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }
}
