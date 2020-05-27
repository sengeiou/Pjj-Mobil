package com.pjj.contract

import com.pjj.module.CancelWhyBean
import com.pjj.module.MallOrderListBean

/**
 * Create by xinheng on 2019/05/18 17:13。
 * describe：
 */
interface MallOrderContract {
    interface View : PayParentView {
        fun updateData(list: MutableList<MallOrderListBean.DataBean>?)
        fun cancelSuccess()
        fun deleteSuccess()
        fun updateCancelWhy(data: MutableList<CancelWhyBean.DataBean>)
        fun sureOrderSuccess()
    }

    interface Present {
        fun loadData(statue: Int, pageNo: Int, pageNum: Int)
        fun loadDeleteOrder(goodOrderId: String, storeId: String)
        fun loadCancelTask(goodOrderId: String, storeId: String, notes: String)
        fun loadCancelWhy()
        fun loadMakeOrderTask(goodOrderId: String, storeId: String, payType: String)
        fun loadSureOrder(goodOrderId: String, storeId: String)
    }
}