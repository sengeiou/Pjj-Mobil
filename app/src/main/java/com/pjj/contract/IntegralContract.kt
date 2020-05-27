package com.pjj.contract

import com.pjj.module.IntegralBean

/**
 * Create by xinheng on 2019/04/10 15:40。
 * describe：
 */
interface IntegralContract {
    interface View : BaseView {
        fun updateData(list: MutableList<IntegralBean.IntegralData>?)
    }

    interface Present {
        fun loadIntegral(pageNo: Int, pageNum: Int,type: String? = null)
    }
}