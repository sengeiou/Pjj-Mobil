package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.ScreenManageOrderListContract
import com.pjj.module.ResultBean
import com.pjj.module.ScreenManageOrderListBean
import com.pjj.utils.JsonUtils

/**
 * Create by xinheng on 2019/06/05 13:50。
 * describe：P层
 */
class ScreenManageOrderListPresent(view: ScreenManageOrderListContract.View) : BasePresent<ScreenManageOrderListContract.View>(view, ScreenManageOrderListContract.View::class.java), ScreenManageOrderListContract.Present {

    override fun loadDeleteOrderTask(ownOrderId: String?) {
        val json = JsonUtils.toJsonString(HashMap<String, String?>(2).apply {
            put("userId", PjjApplication.application.userId)
            this["ownOrderId"] = ownOrderId
        })
        mView.showWaiteStatue()
        retrofitService.revokeOwnOrder(json, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.deleteSuccess()
            }
        })
    }

    override fun loadOrderListTask() {
        mView.showWaiteStatue()
        retrofitService.getAppOwnOrderList(PjjApplication.application.userId, object : PresentCallBack<ScreenManageOrderListBean>(ScreenManageOrderListBean::class.java) {
            override fun successResult(t: ScreenManageOrderListBean) {
                mView.cancelWaiteStatue()
                mView.updateList(t.data)
            }
        })
    }
}
