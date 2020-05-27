package com.pjj.contract

import com.pjj.module.MyReleaseBean

/**
 * Create by xinheng on 2019/08/09 14:49。
 * describe：
 */
interface MyReleaseContract {
    interface View : BaseView {
        fun updateData(list: MutableList<MyReleaseBean.DataBean>?)
        fun deleteOrRecoverSuccess(tagDelete: Boolean)
        fun canacelFreeTopOrderSuccess()
    }

    interface Present {
        fun loadMyReleaseTask(pageNo: Int, pageNum: Int)
        fun loadDeleteOrRecoverTask(freeOrderId: String, status: String)
        fun loadCancelFreeTopOrderTask(freeOrderId: String)
    }
}