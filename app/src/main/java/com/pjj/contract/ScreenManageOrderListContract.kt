package com.pjj.contract

import com.pjj.module.ScreenManageOrderListBean

/**
 * Create by xinheng on 2019/06/05 13:50。
 * describe：
 */
interface ScreenManageOrderListContract {
    interface View : BaseView {
        fun deleteSuccess()
        fun updateList(list: MutableList<ScreenManageOrderListBean.DataBean>?)
    }

    interface Present {
        fun loadDeleteOrderTask(ownOrderId: String?)
        fun loadOrderListTask()
    }
}