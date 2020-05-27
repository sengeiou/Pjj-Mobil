package com.pjj.contract

import com.pjj.module.OrderElevatorBean
import com.pjj.module.SpeedDataBean

/**
 * Create by xinheng on 2018/12/08 11:43。
 * describe：
 */
interface OrderInfContract {
    interface View : BaseView {
        fun updateOrderInf(type: String, path_content: String, title: String, showPhone: Boolean, showPerson: Boolean, time: String, no: String, timeLength: String, price: String, mediaType: String? = null, speedDataBean: SpeedDataBean? = null)
        fun updateOrderElevatorList(list: MutableList<OrderElevatorBean>)
        fun updateHours(hours: Int)
        fun updatePingInf(name: String?, colorTag: String)
    }

    interface Present {
        fun loadOrderInfTask(orderId: String, type: String)
    }
}