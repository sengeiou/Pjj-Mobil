package com.pjj.present

import com.pjj.contract.ScreenshotsListContract
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean
import com.pjj.module.ScreenshotsBean

/**
 * Create by xinheng on 2019/03/18 17:52。
 * describe：P层
 */
class ScreenshotsListPresent(view: ScreenshotsListContract.View) : BasePresent<ScreenshotsListContract.View>(view, ScreenshotsListContract.View::class.java), ScreenshotsListContract.Present {

    override fun loadGetScreenshotsListTask(orderId: String, screenId: String) {
        retrofitService.getScreenImgList(orderId, screenId, object : RetrofitService.CallbackClassResult<ScreenshotsBean>(ScreenshotsBean::class.java) {
            override fun successResult(t: ScreenshotsBean) {
                mView.updateList(t.errInfo,t.imgs)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun screenshots(orderId: String, screenId: String) {
        mView.showWaiteStatue()
        retrofitService.screenshots(orderId, screenId, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.sendScreenshotsCmdSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }

        })
    }
}
