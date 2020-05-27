package com.pjj.contract

import com.pjj.module.NewMediaBuildingBean

/**
 * Create by xinheng on 2019/03/27 11:38。
 * describe：
 */
interface SelectReleaseAreaMediaContract {
    interface View : BaseView {
        fun updateAreaName(areaName: String)
        fun updateGuessBuildingList(list: MutableList<NewMediaBuildingBean.CommunityListBean>?, hotCommunityList: MutableList<NewMediaBuildingBean.CommunityListBean>?)
        //fun updateAreaTypeAndScope(typeList: MutableList<AreaTypeBean.DataBean.TypeListBean>)
        fun createParmJson(areaCode: String, cityName: String): String
    }

    interface Present {
        fun loadGuessListTask(areaCode: String)
        /**
         * 全国价格
         */
        fun loadNationalPrice()

        fun loadAreaBuildingTask(areaCode: String)
        fun startLocation()
        //fun loadAreaTypeAndScope()
    }
}