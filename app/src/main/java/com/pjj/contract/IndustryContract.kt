package com.pjj.contract

import com.pjj.module.IndustryBean

/**
 * Create by xinheng on 2018/12/11 16:03。
 * describe：
 */
interface IndustryContract {
    interface View : BaseView {
        fun updateList(list: MutableList<IndustryBean.DataBean>)
    }

    interface Present {
        fun loadIndustryTask()
    }
}