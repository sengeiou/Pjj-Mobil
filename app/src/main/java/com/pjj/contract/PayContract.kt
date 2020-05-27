package com.pjj.contract

interface PayContract {
    interface View : BaseView {
        fun updateMakeOrderSuccess(orderId: String)
        /**
         * 获取成功，支付信息密文
         */
        fun payCipherSuccess(orderInfo: String, orderId: String)

        fun payCipherFai(msg: String)

        fun payResult(b: Boolean, s: String?)

    }

    interface Present {
        fun loadPayOrderIdTask(json: String)

        /**
         * 获取支付密文
         */
        fun loadAliPayTask(orderId: String)

        fun loadWeiXinPayTask(orderId: String)
        fun loadPayResult(orderId: String)

    }
}