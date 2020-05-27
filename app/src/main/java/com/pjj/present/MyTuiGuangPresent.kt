package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MyTuiGuangContract
import com.pjj.module.MyTuiGuangBean
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/07/26 15:20。
 * describe：P层
 */
class MyTuiGuangPresent(view: MyTuiGuangContract.View) : TuiGuangPresent<MyTuiGuangContract.View>(view, MyTuiGuangContract.View::class.java), MyTuiGuangContract.Present {

    override fun loadDataTask(pageNo: Int, pageNum: Int) {
        retrofitService.getTopOrderList(PjjApplication.application.userId, pageNo, pageNum, object : PresentCallBack<MyTuiGuangBean>(MyTuiGuangBean::class.java) {
            override fun successResult(t: MyTuiGuangBean) {
                mView.updateData(t.data)
            }
        })
    }

    override fun loadCancelTopTask(topOrderId: String) {
        mView.showWaiteStatue()
        retrofitService.cancelTopOrder(topOrderId, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelTopSuccess()
            }
        })
    }
}
