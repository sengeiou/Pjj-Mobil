package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.provider.SyncStateContract
import android.support.v7.widget.LinearLayoutManager
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient

import com.pjj.R
import com.pjj.contract.SelectReleaseAreaContract
import com.pjj.present.SelectReleaseAreaPresent
import kotlinx.android.synthetic.main.activity_select_release_area.*
import com.amap.api.maps.AMap
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.*
import com.pjj.module.BuildingBean
import com.pjj.module.parameters.Area
import com.pjj.module.xspad.XspManage
import com.pjj.utils.*
import com.pjj.view.adapter.BuildingInfAdapter


/**
 * Create by xinheng on 2018/12/01 14:11。
 * describe：选择发布地点
 */
class SelectReleaseAreaActivity : BaseActivity<SelectReleaseAreaPresent>(), SelectReleaseAreaContract.View, LocationSource, AMapLocationListener {
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    override fun deactivate() {
        //mListener =
        if (mlocationClient != null) {
            mlocationClient!!.stopLocation()
            mlocationClient!!.onDestroy()
        }
        mlocationClient = null
    }

    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        mListener = listener
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = AMapLocationClient(this)
            //初始化定位参数
            mLocationOption = AMapLocationClientOption()
            //设置定位回调监听
            mlocationClient!!.setLocationListener(this)
            //设置为高精度定位模式
            mLocationOption?.locationMode = AMapLocationMode.Hight_Accuracy
            //设置定位参数
            mlocationClient!!.setLocationOption(mLocationOption)
            mLocationOption?.interval = 1000
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient!!.startLocation()//启动定位
        }
    }

    private var manualLocation = true
    private lateinit var aMap: AMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_release_area)
        mPresent = SelectReleaseAreaPresent(this)
        mapView.onCreate(savedInstanceState)// 此方法必须重写s
        setTitle("选择发布地点", Color.WHITE, 0).background = bgColorDrawable
        if (GpsUtils.isOPen(this)) {
        } else {
            showNotice("请打开GPS")
            //跳转到设置页面让用户自己手动开启
//            var locationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//            startActivityForResult(locationIntent, 200)
        }
        initMap()
        rv_building.layoutManager = LinearLayoutManager(this)
        rv_building.addItemDecoration(SpaceItemDecoration(this, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_1), ViewUtils.getColor(R.color.color_f1f1f1)))
        tv_repositioning.setOnClickListener(onClick)
        tv_click_select.setOnClickListener(onClick)
        tv_search.setOnClickListener(onClick)
        tv_city.setOnClickListener(onClick)
        XspManage.getInstance().clearXspData()
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_city -> {
                //showNotice("暂未开放")
                startActivityForResult(Intent(this, CitiesActivity::class.java).putExtra("now_city", tv_city.text.toString()), 200)
            }
            R.id.tv_click_select -> {
                AreaActivity.toInstance(this, tv_city.text.toString())
            }
            R.id.tv_repositioning -> {
                showWaiteStatue()
                manualLocation = true
                mlocationClient?.startLocation()
            }
            R.id.tv_search -> {
                areaCode?.let {
                    startActivity(Intent(this@SelectReleaseAreaActivity, SearchSelectReleaseAreaActivity::class.java)
                            .putExtra("local_name", areaName)
                            .putExtra("local_code", it))
                }
            }
        }
    }

    private fun initMap() {
        aMap = mapView.map
        aMap.isTrafficEnabled = false// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.mapType = AMap.MAP_TYPE_NORMAL// 地图模式
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17.5f))
        // 设置定位监听
        aMap.setLocationSource(this)
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.isMyLocationEnabled = true
        //方法自5.1.0版本后支持
        aMap.myLocationStyle.showMyLocation(true)//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        aMap.myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        var color1 = Color.parseColor("#8CC1E5F6")
        aMap.myLocationStyle.strokeColor(color1)
        aMap.myLocationStyle.radiusFillColor(color1)
        aMap.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(p0: CameraPosition?) {
                if (!manualLocation) {//非手动定位
                    p0?.let {
                        var target = it.target
                        getAddressByLatlng(target)
                    }
                }
            }

            override fun onCameraChange(p0: CameraPosition?) {
            }

        })
        //var dp23=ViewUtils.getDp(R.dimen.dp_23)
        //var dp29=ViewUtils.getDp(R.dimen.dp_29)
        aMap.myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_location))
//        aMap.setMyLocationRotateAngle(0f)
        //aMap.myLocationStyle.

    }

    override fun updateGuessBuildingList(list1: MutableList<BuildingBean.CommunityListBean>?) {
        rv_building.adapter = BuildingInfAdapter().apply {
            list = list1
            onItemSelectListener = object : BuildingInfAdapter.OnItemSelectListener {
                override fun itemClick(communityId: String, communityName: String) {
                    guessClick(communityId, communityName)
                }

                override fun noXsp() {
                    showNotice("该大厦没有广告屏")
                }
            }
        }
    }

    private var areaName: String? = null
    private var areaCode: String? = null
    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.errorCode == 0) {
                //Log.e("TAG", "onLocationChanged: ${amapLocation.aoiName}")
                tv_local.text = amapLocation.aoiName
                Log.e("TAG", "code=" + amapLocation.adCode)
                Log.e("TAG", "amapLocation=" + amapLocation.toString())
                areaCode = amapLocation.adCode
                areaName = amapLocation.district
                //110102
                mPresent?.loadGuessListTask(Area(amapLocation.longitude.toString(), amapLocation.latitude.toString(), 1000.toString()))
                tv_city.text = amapLocation.city
                mListener?.onLocationChanged(amapLocation)// 显示系统小蓝点
                mlocationClient?.stopLocation()
            } else {
                val errText = "定位失败," + amapLocation.errorCode + ": " + amapLocation.errorInfo
                Log.e("AmapErr", errText)
            }
        }
        cancelWaiteStatue()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        mlocationClient?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    /**
     * 猜你所想列表点击
     */
    private fun guessClick(communityId: String, communityName: String) {
        ElevatorActivity.start(this, communityId, communityName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            areaCode = data?.getStringExtra("areaCode")
            var areaName = data?.getStringExtra("areaName")
            tv_city.text = areaName
            getLatlon(areaName!!)
        }
    }

    private val geocodeSearch: GeocodeSearch by lazy {
        GeocodeSearch(this).apply {
            setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult?, p1: Int) {
                    if (p1 == 1000)
                        regeocodeResult?.let {
                            var regeocodeAddress = it.regeocodeAddress
                            var latLonPoint = it.regeocodeQuery.point
                            tv_local.text = regeocodeAddress.township
                            Log.e("TAG", "code=" + regeocodeAddress.adCode + " ,,asdf")
                            areaCode = regeocodeAddress.adCode
                            areaName = regeocodeAddress.district
                            mPresent?.loadGuessListTask(Area(latLonPoint.longitude.toString(), latLonPoint.latitude.toString(), 1000.toString()))
                            //tv_city.text = amapLocation.city
//                        mListener?.onLocationChanged(AMapLocation("").apply {
//                            longitude = latLonPoint.longitude
//                            latitude = latLonPoint.latitude
//                        })// 显示系统小蓝点
                        }
                }

                override fun onGeocodeSearched(geocodeResult: GeocodeResult?, i: Int) {
                    if (i == 1000) {
                        geocodeResult?.let {
                            if (TextUtils.isNotEmptyList(it.geocodeAddressList)) {
                                var latLonPoint: LatLonPoint? = null
                                it.geocodeAddressList.forEach { index ->
                                    if (areaCode == index.adcode) {
                                        latLonPoint = index.latLonPoint
                                        return@forEach
                                    }
                                }
                                if (null == latLonPoint) {
                                    latLonPoint = it.geocodeAddressList[0].latLonPoint
                                }
                                Log.e("TAG", "onGeocodeSearched: $areaName = latLonPoint=${latLonPoint!!.latitude}, ${latLonPoint!!.longitude}")
                                manualLocation = false//自动定位
                                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latLonPoint!!.latitude, latLonPoint!!.longitude), 11f))
                            }
                        }
                    } else {

                    }
                }
            })
        }
    }

    private fun getLatlon(cityName: String) {
        var geocodeQuery = GeocodeQuery(cityName.trim(), "29")
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery)
    }

    private fun getAddressByLatlng(latLng: LatLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        var latLonPoint = LatLonPoint(latLng.latitude, latLng.longitude)
        var query = RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP)
        //异步查询
        geocodeSearch.getFromLocationAsyn(query)
    }
}
