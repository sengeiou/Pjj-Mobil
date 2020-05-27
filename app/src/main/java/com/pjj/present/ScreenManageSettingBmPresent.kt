package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.ScreenManageSettingBmContract
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：P层
 */
class ScreenManageSettingBmPresent(view: ScreenManageSettingBmContract.View) : BasePresent<ScreenManageSettingBmContract.View>(view, ScreenManageSettingBmContract.View::class.java), ScreenManageSettingBmContract.Present {

    override fun setBm(propertyTitle: String, propertyInfo: String, screenId: String, isShow: String) {
        mView.showWaiteStatue()
        retrofitService.setPropertyInfo(PjjApplication.application.userId, propertyTitle, propertyInfo, screenId, isShow, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.setSuccess()
            }
        })
    }
}
