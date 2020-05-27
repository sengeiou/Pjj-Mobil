package com.pjj.contract

import com.pjj.module.BuildingBean

/**
 * Create by xinheng on 2018/12/19 15:22。
 * describe：
 */
interface BuildingContract {
    interface View : BaseView {
        fun updateBuildingList(communityList: MutableList<BuildingBean.CommunityListBean>)
    }

    interface Present {
        fun loadBuildingListTask(areaCode: String)
    }
}