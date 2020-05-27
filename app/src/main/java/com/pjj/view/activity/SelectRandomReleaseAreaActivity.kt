package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.PopupWindow
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient

import com.pjj.R
import com.pjj.contract.SelectReleaseAreaContract
import com.pjj.present.SelectReleaseAreaPresent
import com.amap.api.maps.AMap
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.*
import com.pjj.module.BuildingBean
import com.pjj.module.parameters.Area
import com.pjj.module.xspad.XspManage
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.BuildingSelectInfAdapter
import com.pjj.view.custom.SpinnerLinearView
import kotlinx.android.synthetic.main.activity_select_random_release_area.*


/**
 * Create by xinheng on 2018/12/01 14:11。
 * describe：选择发布地点
 */
class SelectRandomReleaseAreaActivity : BaseActivity<SelectReleaseAreaPresent>(), SelectReleaseAreaContract.View, LocationSource, AMapLocationListener {
    private lateinit var mListener: LocationSource.OnLocationChangedListener
    private var mlocationClient: AMapLocationClient? = null
    private lateinit var mLocationOption: AMapLocationClientOption
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
            mLocationOption.locationMode = AMapLocationMode.Hight_Accuracy
            //设置定位参数
            mlocationClient!!.setLocationOption(mLocationOption)
            mLocationOption.interval = 1000
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient!!.startLocation()//启动定位
        }
    }

    private var area: Area? = null
    private lateinit var aMap: AMap
    private lateinit var buildingAdapter: BuildingSelectInfAdapter
    private var dp17 = ViewUtils.getDp(R.dimen.dp_17)
    private var drawableSelect = ViewUtils.getDrawable(R.mipmap.select).apply {
        setBounds(0, 0, dp17, dp17)
    }
    private var drawableUnSelect = ViewUtils.getDrawable(R.mipmap.unselect).apply {
        setBounds(0, 0, dp17, dp17)
    }
    /**
     * 全部选中标记
     */
    private var selectAll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_random_release_area)
        mPresent = SelectReleaseAreaPresent(this)
        mapView.onCreate(savedInstanceState)// 此方法必须重写s
        setTitle("选择发布地点", Color.WHITE, 0).background = bgColorDrawable
        buildingAdapter = BuildingSelectInfAdapter().apply {
            onItemSelectListener = object : BuildingSelectInfAdapter.OnItemSelectListener {
                override fun select(isAll: Boolean) {
                    if (isAll) {
                        selectAll = true
                        tv_all_select.setCompoundDrawables(null, null, drawableSelect, null)
                    } else {
                        selectAll = false
                        tv_all_select.setCompoundDrawables(null, null, drawableUnSelect, null)
                    }
                }
            }
        }
        initMap()
        rv_building.layoutManager = LinearLayoutManager(this)
        //rv_building.addItemDecoration(SpaceItemDecoration(this,LinearLayoutManager.VERTICAL,ViewUtils.getDp(R.dimen.dp_1),ViewUtils.getColor(R.color.color_f1f1f1)))
        rv_building.adapter = buildingAdapter
        tv_repositioning.setOnClickListener(onClick)
        tv_click_select.setOnClickListener(onClick)
        tv_search.setOnClickListener(onClick)
        tv_scope.setOnClickListener(onClick)
        tv_all_select.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
        tv_city.setOnClickListener(onClick)

        tv_all_select.setCompoundDrawables(null, null, drawableUnSelect, null)
        XspManage.getInstance().clearXspData()
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_city -> startActivityForResult(Intent(this, CitiesActivity::class.java).putExtra("now_city", tv_city.text.toString()), 200)
            R.id.tv_click_select -> AreaActivity.toInstance(this, tv_city.text.toString())
            R.id.tv_repositioning -> {
                showWaiteStatue()
                mlocationClient?.startLocation()
            }
            R.id.tv_search -> {
                areaCode?.let {
                    startActivity(Intent(this@SelectRandomReleaseAreaActivity, SearchSelectReleaseAreaActivity::class.java)
                            .putExtra("local_name", areaName)
                            .putExtra("local_code", it))
                }
            }
            R.id.tv_scope -> startScopeDialog()
            R.id.tv_all_select -> {
                when (selectAll) {
                    true -> setAllUnSelect()
                    else -> setAllSelect()
                }
            }
            R.id.tv_sure -> {
                var selectBuildingList = buildingAdapter.getSelectBuildingList()
                if (null == selectBuildingList) {
                    showNotice("请选择小区")
                } else {
                    XspManage.getInstance().buildList = selectBuildingList
                    when {
                        XspManage.getInstance().bianMinPing == 1 -> SelectBMPingTemplateActivity.start(this@SelectRandomReleaseAreaActivity, tv_scope.text.substring(4))
                        XspManage.getInstance().identityType==3 -> SelectRandomSpeedTemplateActivity.start(this@SelectRandomReleaseAreaActivity, tv_scope.text.substring(4))
                        else -> SelectRandomTemplateActivity.start(this@SelectRandomReleaseAreaActivity, tv_scope.text.substring(4))
                    }
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
                p0?.let {
                    var target = it.target
                    getAddressByLatlng(target)
                }
            }

            override fun onCameraChange(p0: CameraPosition?) {
            }

        })
        aMap.myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_location))
//        aMap.setMyLocationRotateAngle(0f)
        //aMap.myLocationStyle.
    }
    private fun setAllSelect() {
        selectAll = true
        tv_all_select.setCompoundDrawables(null, null, drawableSelect, null)
        buildingAdapter.setAllSelectStatue(selectAll)
    }

    private fun setAllUnSelect() {
        selectAll = false
        tv_all_select.setCompoundDrawables(null, null, drawableUnSelect, null)
        buildingAdapter.setAllSelectStatue(selectAll)
    }

    override fun updateGuessBuildingList(list1: MutableList<BuildingBean.CommunityListBean>?) {
        buildingAdapter.setList(list1)
        tv_all_select.isEnabled = TextUtils.isNotEmptyList(list1)
        selectAll = false
        tv_all_select.setCompoundDrawables(null, null, drawableUnSelect, null)
        ViewUtils.setHtmlText(tv_local, tv_scope.text.substring(4) + "内随机  " + "<font color=\"#40BBF7\">" + (list1?.size
                ?: 0) + "个小区</font>")
    }

    private var areaName: String? = null
    private var areaCode: String? = null
    override fun onLocationChanged(amapLocation: AMapLocation) {
        if (amapLocation.errorCode == 0) {
            //tv_local.text = amapLocation.street
            Log.e("TAG", "code=" + amapLocation.adCode)
            //110102
            areaCode = amapLocation.adCode
            areaName = amapLocation.district
            area = Area(amapLocation.longitude.toString(), amapLocation.latitude.toString(), 1000.toString())
            mPresent?.loadGuessListTask(area)
            tv_city.text = amapLocation.city
            mListener.onLocationChanged(amapLocation)// 显示系统小蓝点
            mlocationClient?.stopLocation()
        } else {
            val errText = "定位失败," + amapLocation.errorCode + ": " + amapLocation.errorInfo
            Log.e("AmapErr", errText)
        }
        cancelWaiteStatue()
    }

    private var dp141 = ViewUtils.getDp(R.dimen.dp_141)
    /**
     * 区域范围选择标志
     */
    private var scopePosition: Int = 1

    private fun startScopeDialog() {
        var view = SpinnerLinearView(this).apply {
            background = ColorDrawable(Color.WHITE)
        }
        var list = view.creatIntList()
        view.setTextList(list, scopePosition)
        var popupWindow = PopupWindow(view, tv_scope.measuredWidth, dp141, true)
        view.onSelectListener = object : SpinnerLinearView.OnSelectListener {
            override fun select(position: Int, content: String) {
                popupWindow.dismiss()
                if (null == area) {
                    showNotice("等待定位")
                } else {
                    area?.let {
                        scopePosition = position
                        it.range = list[position].toString()
                        tv_scope.text = "选择范围$content"
                        mPresent?.loadGuessListTask(it)
                    }
                }
            }

            override fun dismiss() {
                popupWindow.dismiss()
            }
        }
        popupWindow.showAsDropDown(tv_scope)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        if (!TextUtils.isNotEmptyList(XspManage.getInstance().buildList)) {
            buildingAdapter?.setAllSelectStatue(false)
            tv_all_select.setCompoundDrawables(null, null, drawableUnSelect, null)
        }
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            areaCode = data?.getStringExtra("areaCode")
            areaName = data?.getStringExtra("areaName")
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
                            //tv_local.text = regeocodeAddress.township
                            Log.e("TAG", "code=" + regeocodeAddress.adCode)
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
