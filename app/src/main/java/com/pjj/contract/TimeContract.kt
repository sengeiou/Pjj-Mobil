package com.pjj.contract

import com.pjj.module.TimeDiscountBean

/**
 * Create by xinheng on 2018/11/21 17:33。
 * describe：
 */
interface TimeContract {
    interface View : BaseView {
        fun saveTimeDiscountBean(timeDiscountBean: TimeDiscountBean)
    }

    interface Present {
        fun loadTimeDiscountTask()
    }
}