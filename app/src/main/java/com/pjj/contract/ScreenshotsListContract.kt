package com.pjj.contract

import com.pjj.module.ScreenshotsBean

/**
 * Create by xinheng on 2019/03/18 17:52。
 * describe：
 */
interface ScreenshotsListContract {
    interface View : BaseView {
        fun updateList(errInfo: String?, imgs: MutableList<ScreenshotsBean.ImgsBean>)
        fun sendScreenshotsCmdSuccess()
    }

    interface Present {
        fun loadGetScreenshotsListTask(orderId: String, screenId: String)
        fun screenshots(orderId: String, screenId: String)
    }
}