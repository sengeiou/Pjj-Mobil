package com.pjj.module.xspad

import com.pjj.module.*
import com.pjj.view.adapter.OrderElevatorInfAdapter
import com.pjj.view.adapter.SelectReleaseAreaAllAdapter

/**
 * Created by XinHeng on 2019/03/29.
 * describe：传媒模面池
 */
class NewMediaData {
    //var hashMap: HashMap<String, String>? = null
    var screenId: String? = null
    var buildingName: String? = null
    var price = 0f
    var templetIds: String? = null
    var screenIdList: MutableList<ScreenBean>? = null
    var communityList: MutableList<MediaOrderInfBean.OrderScreenListBean>? = null
    var communityAllList: MutableList<SelectReleaseAreaAllAdapter.OrderAllInfParent>? = null
    var elevatorCommunityList: MutableList<OrderElevatorInfAdapter.OrderElevatorInfParent>? = null
    var templetName: String? = null
    var selectedTemplate: UserTempletBean.DataBean.FileListBean? = null
    var releaseTag = false
    var dates: String? = null
    var preTowData: SpeedDataBean? = null
    var unUseScreenList: MutableList<MakeOrderScreenBean.ScreenBean>? = null

    var selectAllBuildBean: AllBuildingScreenBean.DataBean? = null

    class ScreenBean {
        var srceenId: String? = null
        var price: Float = 0f
        var screenName: String? = null
        var priceDay: Float = 0f
        var days: MutableList<String>? = null
        var maxAdCount = 100
    }
}