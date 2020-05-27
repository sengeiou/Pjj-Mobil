package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.ScreenManageContract
import com.pjj.module.ScreenBean

/**
 * Create by xinheng on 2019/05/20 17:55。
 * describe：P层
 */
class ScreenManagePresent(view: ScreenManageContract.View) : BasePresent<ScreenManageContract.View>(view, ScreenManageContract.View::class.java), ScreenManageContract.Present {

    override fun loadScreenDataTask(queryType: String) {
        mView.showWaiteStatue()
        retrofitService.getOwnScreen(PjjApplication.application.userId, queryType, object : PresentCallBack<ScreenBean>(ScreenBean::class.java) {
            override fun successResult(t: ScreenBean) {
                mView.cancelWaiteStatue()
                mView.updateData(t.data)
            }
        })
    }
}
