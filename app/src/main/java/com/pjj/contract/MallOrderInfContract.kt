package com.pjj.contract

import com.pjj.module.CancelWhyBean
import com.pjj.module.MallOrderInfBean

/**
 * Create by xinheng on 2019/05/20 13:54。
 * describe：
 */
interface MallOrderInfContract {
    interface View : BaseView {
        fun updateView(bean: MallOrderInfBean.DataBean)
        fun cancelSuccess()
        fun deleteSuccess()
        fun sureOrderSuccess()
        fun updateCancelData(datas: MutableList<CancelWhyBean.DataBean>)
    }

    interface Present {
        /**
         * 查询订单详情
         *
         * @param goodOrderId
         * @param storeId
         */
        fun loadMallOrderInfTask(goodOrderId: String, storeId: String)

        fun loadDeleteOrder(goodOrderId: String, storeId: String)

        fun loadSureOrder(goodOrderId: String, storeId: String)
        fun loadCancelOrder(goodOrderId: String, storeId: String, notes: String)
        fun loadCancelWhy()
    }
}