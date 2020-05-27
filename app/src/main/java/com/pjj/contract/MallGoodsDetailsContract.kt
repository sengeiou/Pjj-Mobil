package com.pjj.contract

import com.pjj.module.GoodsDetailBean

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：
 */
interface MallGoodsDetailsContract {
    interface View : BaseView{
        fun updateGoods(data: GoodsDetailBean)
        fun addShopCarSuccess()
        fun refresh()
    }

    interface Present {
        fun loadGoodsDetail(goodsId: String)
        fun loadAddShopCar(goodsId: String, specificId: String?, goodsNum: Int)
    }
}