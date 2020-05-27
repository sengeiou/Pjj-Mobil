package com.pjj.contract

import com.pjj.module.NewMediaScreenBean

/**
 * Create by xinheng on 2019/03/27 14:00。
 * describe：
 */
interface ScreenMediaContract {
    interface View : BaseView{
        fun updateView(data: NewMediaScreenBean.DataBean)
    }

    interface Present{
        /**
         * 加载屏幕信息
         * @param communityId  小区id
         */
        fun loadScreenDataList(communityId:String)
    }
}