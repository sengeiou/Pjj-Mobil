package com.pjj.contract

import com.pjj.module.DiyDataBean

/**
 * Create by xinheng on 2019/05/28 17:58。
 * describe：
 */
interface BirthdayWishesContract {
    interface View : BaseView {
        fun update(bean: DiyDataBean)
    }

    interface Present {
        fun loadTemplateInfTask(id: String)
    }
}