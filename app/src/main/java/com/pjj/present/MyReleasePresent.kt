package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MyReleaseContract
import com.pjj.module.MyReleaseBean
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/08/09 14:49。
 * describe：P层
 */
class MyReleasePresent(view: MyReleaseContract.View) : BasePresent<MyReleaseContract.View>(view, MyReleaseContract.View::class.java), MyReleaseContract.Present {

    override fun loadMyReleaseTask(pageNo: Int, pageNum: Int) {
        retrofitService.getFreeOrderList(PjjApplication.application.userId, pageNo, pageNum, object : PresentCallBack<MyReleaseBean>(MyReleaseBean::class.java) {
            override fun successResult(t: MyReleaseBean) {
                mView.updateData(t.data)
            }
        })
    }

    override fun loadDeleteOrRecoverTask(freeOrderId: String, status: String) {
        mView.showWaiteStatue()
        retrofitService.updateFreeOrderStatus(freeOrderId, status, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.deleteOrRecoverSuccess(status == "3")
            }
        })
    }

    override fun loadCancelFreeTopOrderTask(freeOrderId: String) {
        mView.showWaiteStatue()
        retrofitService.canacelFreeTopOrder(freeOrderId, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.canacelFreeTopOrderSuccess()
            }
        })
    }
}
