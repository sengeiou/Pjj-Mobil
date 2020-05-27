package com.pjj.contract

/**
 * Create by xinheng on 2019/04/04 14:35。
 * describe：
 */
interface GoodsDetailsContract {
    interface View : BaseView {
        fun passCheckIntegral()
        fun updateIntegralRule(text: String?)
    }

    interface Present {
        fun loadCheckIntegral(goodsIntegral: String)
        fun loadIntegralRule()
    }
}