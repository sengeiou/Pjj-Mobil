package com.pjj.contract

import com.pjj.module.GoodsAddressBean

/**
 * Create by xinheng on 2019/05/16 17:21。
 * describe：
 */
interface MallSureOrderContract {
    interface View : BaseView{
        fun updateGoodsAddress(address: GoodsAddressBean.GoodsAddressData?)

        fun makeSuccess(orderId: String)
        fun payCipherSuccess(data: String, orderId: String)
        fun noGoods()
    }

    interface Present{
        fun loadGoodsAddress()
        fun loadAliPayTask(orderId: String)
        fun loadWeiXinPayTask(orderId: String)
        fun makeMallGoodsOrder(goodsList: String)
    }
}