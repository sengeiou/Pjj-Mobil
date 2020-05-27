package com.pjj.present

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.pjj.PjjApplication
import com.pjj.contract.SelectReleaseAreaAllContract
import com.pjj.contract.SelectReleaseAreaMediaContract
import com.pjj.intent.RetrofitService
import com.pjj.module.AllBuildingScreenBean
import com.pjj.module.CityBean
import com.pjj.utils.Log

/**
 * Create by xinheng on 2019/03/27 11:38。
 * describe：P层
 */
class SelectReleaseAreaAllPresent(view: SelectReleaseAreaAllContract.View) : BasePresent<SelectReleaseAreaAllContract.View>(view, SelectReleaseAreaAllContract.View::class.java), SelectReleaseAreaMediaContract.Present, AMapLocationListener {
    private var mBuildingBean: AllBuildingScreenBean? = null
    private lateinit var mLocationClient: AMapLocationClient
    override fun loadAreaBuildingTask(areaCode: String) {
        mView.showWaiteStatue()
        retrofitService.getCities(object : RetrofitService.CallbackClassResult<CityBean>(CityBean::class.java) {
            override fun successResult(t: CityBean) {
                //mView.cancelWaiteStatue()
                t.cityList?.forEach {
                    if (areaCode == it.areaName) {
                        loadGuessListTask(mView.createParmJson(it.areaCode, it.areaName))
                        return
                    }
                }
                mView.showNotice("找不到城市code值")
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })

        /*retrofitService.getCommunityNewMediaScreenNumByAreaBuilding(areaCode, object : RetrofitService.CallbackClassResult<NewMediaBuildingBean>(NewMediaBuildingBean::class.java) {
            override fun successResult(buildingBean: NewMediaBuildingBean) {
                mBuildingBean = buildingBean
                loadNationalPrice()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })*/

    }

    override fun startLocation() {
        //初始化定位
        mLocationClient = AMapLocationClient(PjjApplication.application)
        //初始化定位参数
        var mLocationOption = AMapLocationClientOption()
        //设置定位回调监听
        mLocationClient.setLocationListener(this)
        //设置为高精度定位模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        mLocationOption.interval = 1000
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mLocationClient!!.startLocation()//启动定位
    }

    override fun loadGuessListTask(areaCode: String) {
        mView.showWaiteStatue()
        retrofitService.getNewMediaCommNumByCityArea(areaCode, object : RetrofitService.CallbackClassResult<AllBuildingScreenBean>(AllBuildingScreenBean::class.java) {
            override fun successResult(buildingBean: AllBuildingScreenBean) {
                mBuildingBean = buildingBean
                mBuildingBean?.initData()
                val data = mBuildingBean?.communityList
                data?.forEach { communityListBean ->
                    setPrice(communityListBean)
                }
                mView.cancelWaiteStatue()
                mView.updateGuessBuildingList(data)
            }

            override fun fail(error: String) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    private fun setPrice(communityListBean: AllBuildingScreenBean.DataBean) {
        var v1: Float
        v1 = -1f
        communityListBean.screenList?.forEach {
            val price = if ("1" == it.cooperationMode) {//自营不打折
                it.mediaPrice
            } else {
                it.mediaPrice * it.finalMediaDiscount
            }
            it.finalXspPrice = price
            if (price < v1 || v1 < 0) {
                v1 = price
            }
        }
        communityListBean.elevatorList?.forEach { elevator ->
            elevator.screenList?.forEach { screen ->
                val price = if ("1" == screen.cooperationMode) {//自营不打折
                    screen.traditionPrice
                } else {
                    screen.traditionPrice * screen.finalTraditionDiscount
                }
                screen.finalXspPrice = price
                if (price < v1 || v1 < 0) {
                    v1 = price
                }
            }
        }
        if (v1 >= 0) {
            communityListBean.price = v1
        }
    }

    override fun loadNationalPrice() {}

    override fun onLocationChanged(amapLocation: AMapLocation?) {
        amapLocation?.let {
            mLocationClient.stopLocation()
            mLocationClient.onDestroy()
            Log.e("TAG", "amapLocation=$it")
            mView.updateAreaName(it.city)
            //loadGuessListTask(it.adCode)
            loadAreaBuildingTask(it.city)
        }
    }

}
