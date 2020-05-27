package com.pjj.contract

import com.pjj.module.ExchangeRecordBean

/**
 * Create by xinheng on 2019/04/11 10:27。
 * describe：
 */
interface ExchangeRecordContract {
    interface View : BaseView {
        fun updateExchangeRecord(list: MutableList<ExchangeRecordBean.ExchangeRecordData>?)
    }

    interface Present {
        fun loadExchangeRecordTask(start: Int, num: Int, status: String)
    }
}