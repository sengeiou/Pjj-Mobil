package com.pjj.contract

import com.pjj.module.MallClassificationBean
import com.pjj.module.MallHomepageGoodsBean

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：
 */
interface MallHomepageContract {
    interface View : BaseView {
        fun updateTitleList(list: MutableList<MallClassificationBean.DataBean>)
        fun updateBanner(list: MutableList<MallHomepageGoodsBean.DataBean.AdverBannerBean>?)
    }

    interface Present {
        fun loadClassificationTask()
    }
}