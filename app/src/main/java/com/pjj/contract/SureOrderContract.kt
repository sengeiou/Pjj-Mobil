package com.pjj.contract

import com.pjj.module.GoodsAddressBean

/**
 * Create by xinheng on 2019/04/04 15:26。
 * describe：
 */
interface SureOrderContract {
    interface View : BaseView {
        fun updateGoodsAddress(address: GoodsAddressBean.GoodsAddressData?)

        fun makeSuccess(orderId: String)
        fun payCipherSuccess(data: String, orderId: String)
    }

    interface Present {
        fun loadGoodsAddress()
        fun makeIntegralGoodsOrder()
        fun loadAliPayTask(orderId: String)
        fun loadWeiXinPayTask(orderId: String)
    }
}