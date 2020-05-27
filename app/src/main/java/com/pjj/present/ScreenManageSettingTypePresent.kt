package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.ScreenManageSettingTypeContract
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：P层
 */
class ScreenManageSettingTypePresent(view: ScreenManageSettingTypeContract.View) : BasePresent<ScreenManageSettingTypeContract.View>(view, ScreenManageSettingTypeContract.View::class.java), ScreenManageSettingTypeContract.Present {
    override fun loadSetType(screenId: String, mediaPrice: String?, cooperationMode: String?, setVoice: String?) {
        mView.showWaiteStatue()
        retrofitService.setOwnScreen(PjjApplication.application.userId, screenId, mediaPrice, cooperationMode, setVoice, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.setSuccess()
            }
        })
    }
}
