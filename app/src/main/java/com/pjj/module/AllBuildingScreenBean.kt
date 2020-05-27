package com.pjj.module

import com.pjj.utils.TextUtils
import com.pjj.view.adapter.OrderElevatorInfAdapter
import com.pjj.view.adapter.SelectReleaseAreaAllAdapter

/**
 * Created by XinHeng on 2019/03/27.
 * describe：
 */
class AllBuildingScreenBean : ResultBean() {

    /**
     * data : {"mediaDiscount":1,"lng":"116.347534","assembleDiscount":1,"screenNum":"5","imgName":"communityBig.png","screenList":[{"assemblePrice":1,"screenName":"朗琴国际B座测试新媒体屏幕","finaNewlDiscount":1,"screenCode":"11010200001","mediaPrice":1,"screenId":"11111111111","price":1,"screenType":"3","isSplicing":"1","playTime":"1","finalMediaDiscount":1,"screenUrlName":"screenSmall.png","finalAssembleDiscount":1}],"areaCode":"110102","newMediaDiscount":1,"communityName":"朗琴国际B座","position":" 北京 北京市 西城区","communityId":"95","lat":"39.894571"}
     */

    var communityList: MutableList<DataBean>? = null

    fun initData() {
        communityList?.forEach {
            it.initData()
        }
    }

    class DataBean : NewMediaBuildingBean.CommunityListBean() {
        var sumPrice: Float = 0f
        var inListTag = true
        var titleImg: String? = null
        var elevatorList: MutableList<BuildingElevatorBean.DataBean.ElevatorListBean>? = null
        var selectCount: Int = 0
        var canUseCount: Int = 0
        fun cloneInf(elevatorSize: Int, screenSize: Int, price: Float): SelectReleaseAreaAllAdapter.OrderAllInfParent {
            return object : SelectReleaseAreaAllAdapter.OrderAllInfParent {
                override fun getBuildingImageName(): String? {
                    return TextUtils.clean(imgName)
                }

                override fun getScreenNum(): Int {
                    return selectCount
                }

                override fun getBuildingName(): String? {
                    return TextUtils.clean(communityName)
                }

                override fun getElevatorCount(): Int {
                    return elevatorSize
                }

                override fun getScreenCount(): Int {
                    return screenSize
                }

                override fun getPrice(): Float {
                    return price
                }
            }
        }

        fun initData() {
            canUseCount = 0
            var elevatorCanUse: Int
            elevatorList?.forEach { elevator ->
                elevatorCanUse = 0
                elevator.screenList?.forEach { screen ->
                    if (screen.screenStatus != "2") {
                        ++canUseCount
                        ++elevatorCanUse
                    }
                }
                elevator.canUseCount = elevatorCanUse
            }
            screenList?.forEach { screen ->
                if (screen.screenStatus != "2") {
                    ++canUseCount
                }
            }
            inListTag = TextUtils.isNotEmptyList(elevatorList) || TextUtils.isNotEmptyList(screenList)
            isCanClick = canUseCount > 0
        }
    }
}
