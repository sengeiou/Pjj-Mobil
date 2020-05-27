package com.pjj.contract

import com.pjj.module.CityBean

/**
 * Create by xinheng on 2018/12/19 16:38。
 * describe：
 */
interface CitiesContract {
    interface View : BaseView {
        fun updateCities(list: MutableList<CityBean.CityListBean>?)
    }

    interface Present {
        fun loadCitiesTask()
    }
}