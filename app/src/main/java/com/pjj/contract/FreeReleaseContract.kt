package com.pjj.contract

import com.pjj.module.TopPriceBean
import com.pjj.module.parameters.ReleaseFreeOrder

interface FreeReleaseContract {
    interface View : PayContract.View {
        fun updatePrice(datas: MutableList<TopPriceBean.DataBean>?)
    }

    interface Present {
        fun loadTopPriceTask()
        fun loadReleaseFreeOrder(bean: ReleaseFreeOrder)
    }
}