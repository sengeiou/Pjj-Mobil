package com.pjj.contract

import com.pjj.module.BuildingElevatorBean

/**
 * Create by xinheng on 2019/03/27 11:38。
 * describe：
 */
interface SelectReleaseAreaElevatorMediaContract {
    interface View : BaseView {
        fun updateAreaName(areaName: String)
        fun updateGuessBuildingList(list: MutableList<BuildingElevatorBean.DataBean>?, hotData: MutableList<BuildingElevatorBean.DataBean>?)
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
    }
}