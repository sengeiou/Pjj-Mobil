package com.pjj.contract

import com.pjj.module.TopPriceBean

/**
 * Create by xinheng on 2019/07/15 11:14。
 * describe：
 */
interface TuiGuangContract {
    interface View : PayContract.View {
        fun updatePrice(datas: MutableList<TopPriceBean.DataBean>?)
    }

    interface Present {
        fun loadTopPriceTask()
    }
}