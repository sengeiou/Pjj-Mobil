package com.pjj.contract

import com.pjj.module.MakeOrderScreenBean
import com.pjj.module.parameters.NewMediaMakeOrder

/**
 * Create by xinheng on 2019/03/27 16:33。
 * describe：
 */
interface MakeOrderContract {
    interface View : BaseView {
        fun updateResult(playLongTime: Int, screenSum: Int)

        fun updateMakeOrderSuccess(orderId: String)
        fun updateMakeOrderFail(bean: MakeOrderScreenBean.MakeOrderData?)

        fun updateMakeOrderFail(error: String?)
        /**
         * 获取成功，支付信息密文
         */
        fun payCipherSuccess(orderInfo: String, orderId: String)

        fun payResult(b: Boolean, s: String?)
    }

    interface Present {
        fun loadMakeOrder(newMediaMakeOrder: NewMediaMakeOrder)
        /**
         * 支付密文
         */
        fun loadAliPayTask(orderId: String)

        fun loadWeiXinPayTask(orderId: String)
        fun loadPayResult(orderId: String)
        fun loadUseTime(screenIds: String, selectDate: String, orderType: String)

    }
}