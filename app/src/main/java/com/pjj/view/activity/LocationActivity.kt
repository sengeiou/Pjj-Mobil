package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.view.View
import android.widget.TextView
import com.pjj.R
import com.pjj.intent.RetrofitService
import com.pjj.module.AreaTypeBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.*
import com.pjj.view.dialog.AreaScopePopupWindow
import com.pjj.view.dialog.AreaTypePopupWindow
import com.pjj.view.dialog.CallPhoneDialog
import kotlinx.android.synthetic.main.activity_select_release_area_media.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.*
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.view.custom.ToastNoticeView

abstract class LocationActivity<P : BasePresent<*>> : BaseActivity<P>() {
    protected open var gpsAddTag = false
    protected open var typeId: String? = null
    protected open var range: String? = null
    protected open var areaCode: String? = null
    protected open var cityName: String? = null
    protected open var mapMarkerFullTag = false
    protected open var filterTag = false
    private var mapSelectPosition = -1
    private val listMarker = ArrayList<Marker>()
    private val gpsDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(this).apply {
            rightText = "去设置"
            phone = "请在设置中打开定位\n以展示您附近的屏幕"
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 3000)
                }
            }
        }
    }
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mlocationClient: AMapLocationClient? = null
    private var mLocationOption: AMapLocationClientOption? = null
    override fun fitSystemWindow(): Boolean {
        return false
    }

    fun preStartLocation() {
        loadAreaTypeTask()
        val cityName = XspManage.getInstance().cityName
        if (!TextUtils.isEmpty(cityName)) {
            gpsAddTag = true
            startLoadCityData(cityName)
            return
        }
        if (GpsUtils.isOPen(this)) {
            gpsAddTag = true
            startLocation()
        } else {
            gpsAddTag = false
            if (!gpsDialog.isShowing)
                gpsDialog.show()
        }
    }

    fun initViews(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)// 此方法必须重写s
        tv_change.setOnClickListener(onClick)
        tv_map_sure.setOnClickListener(onClick)
        initMap()
        aMap.setOnMarkerClickListener {
            Log.e("TAG", "setOnMarkerClickListener：")
            it.title?.let { intNum ->
                mapSelectPosition = intNum.toInt()
                updateMapBottomView(mapSelectPosition)
            }
            true
        }
    }

    protected fun setMapBottomContent(path: String, name: String, location: String) {
        rl_map_bottom.visibility = View.VISIBLE
        Glide.with(this).load(PjjApplication.filePath + path).into(iv_map_build)
        tv_map_build_name.text = name
        tv_map_location.text = location
    }

    protected fun getWhiteDrawableTopLR(): Drawable {
        return ViewUtils.getDrawable(R.drawable.shape_white_bg_4_lr)
    }

    private lateinit var aMap: AMap
    private fun initMap() {
        aMap = mapView.map
        aMap.isTrafficEnabled = false// 显示实时交通状况
        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.mapType = AMap.MAP_TYPE_NORMAL// 地图模式
        //设置缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(10f))
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //aMap.isMyLocationEnabled = true
        aMap.uiSettings.isRotateGesturesEnabled = false
        //方法自5.1.0版本后支持
        //aMap.myLocationStyle.showMyLocation(true)//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        //aMap.myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER)//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        // aMap.myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
//        var color1 = Color.parseColor("#8CC1E5F6")
//        aMap.myLocationStyle.strokeColor(color1)
//        aMap.myLocationStyle.radiusFillColor(color1)
        aMap.setOnCameraChangeListener(object : AMap.OnCameraChangeListener {
            override fun onCameraChangeFinish(p0: CameraPosition?) {
                if (!manualLocation) {//非手动定位
                    p0?.let {
                        val target = it.target
                        getAddressByLatlng(target)
                    }
                }
            }

            override fun onCameraChange(p0: CameraPosition?) {
            }

        })

        aMap.setLocationSource(object : LocationSource {
            override fun activate(listener: LocationSource.OnLocationChangedListener?) {
                mListener = listener
                if (mlocationClient == null) {
                    //初始化定位
                    mlocationClient = AMapLocationClient(this@LocationActivity)
                    //初始化定位参数
                    mLocationOption = AMapLocationClientOption()
                    //设置定位回调监听
                    mlocationClient!!.setLocationListener { amapLocation ->
                        if (mListener != null && amapLocation != null) {
                            if (amapLocation.errorCode == 0) {
                                //Log.e("TAG", "onLocationChanged: ${amapLocation.aoiName}")
                                Log.e("TAG", "code=" + amapLocation.adCode)
                                Log.e("TAG", "amapLocation=$amapLocation")
                                //110102
                                aMap.moveCamera(CameraUpdateFactory.zoomTo(10f))
                                mListener?.onLocationChanged(amapLocation)// 显示系统小蓝点
                                mlocationClient?.stopLocation()
                            } else {
                                val errText = "定位失败," + amapLocation.errorCode + ": " + amapLocation.errorInfo
                                Log.e("AmapErr", errText)
                            }
                        }
                        cancelWaiteStatue()
                    }
                    //设置为高精度定位模式
                    mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
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

            override fun deactivate() {
                if (mlocationClient != null) {
                    mlocationClient!!.stopLocation()
                    mlocationClient!!.onDestroy()
                }
                mlocationClient = null
            }

        })
    }

    private val dp8 = ViewUtils.getDp(R.dimen.dp_9)
    override fun onClick(viewId: Int) {
        super.onClick(viewId)
        when (viewId) {
            R.id.tv_area_qu -> {
                if (!GpsUtils.isOPen(this)) {
                    gpsAddTag = false
                    if (!gpsDialog.isShowing)
                        gpsDialog.show()
                    return
                }
                startActivityForResult(Intent(this, CitiesActivity::class.java), 200)
            }
            R.id.iv_back -> titleLeftClick()
            R.id.tv_map_sure -> mapSelectPosition(mapSelectPosition)
            R.id.fl_area_scope -> {
                fl_area_scope.background = getWhiteDrawableTopLR()
                setRightDrawable(tv_area_scope, R.mipmap.unxia_fill)
                scopeDialog.showAsDropDown(line, 0, -dp8)
            }
            R.id.fl_area_type -> {
                fl_area_type.background = getWhiteDrawableTopLR()
                setRightDrawable(tv_area_type, R.mipmap.unxia_fill)
                typeDialog.showAsDropDown(line, 0, -dp8)
            }
            R.id.tv_change -> {
                if (rl_map.visibility == View.VISIBLE) {
                    tv_change.text = "地图"
                    rl_map.visibility = View.GONE
                } else {
                    tv_change.text = "列表"
                    rl_map.visibility = View.VISIBLE
                    if (!mapMarkerFullTag) {
                        updateMarker(filterTag)
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    protected fun updateMarker(filterTag: Boolean = false) {
        this.filterTag = filterTag
        if (rl_map.visibility == View.VISIBLE) {
            clearMarker()
            if (XspManage.getInstance().cityName == XspManage.getInstance().aotuLocalCity) {
                manualLocation = true
                showWaiteStatue()
                if (null == mlocationClient) {
                    aMap.isMyLocationEnabled = true
                    aMap.myLocationStyle.showMyLocation(true)//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
                    aMap.myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
                    aMap.myLocationStyle.strokeColor(Color.TRANSPARENT)
                    aMap.myLocationStyle.radiusFillColor(Color.TRANSPARENT)
                } else {
                    mlocationClient?.startLocation()
                }
            } else {
                manualLocation = false
                getLatlon(XspManage.getInstance().cityName)
            }
            mapMarkerFullTag = true
            startAddMarker(filterTag)
        }
    }

    abstract fun startLocation()
    abstract fun startLoadCityData(cityName: String)
    abstract fun refreshBuilding(json: String)
    abstract fun mapSelectPosition(positionFirst: Int)
    abstract fun startAddMarker(filterTag: Boolean)
    abstract fun updateMapBottomView(position: Int)
    private fun setRightDrawable(tv: TextView, res: Int, whiteTag: Boolean = false) {
        val drawable = ViewUtils.getDrawable(res)
        if (whiteTag) {
            line_area.visibility = View.VISIBLE
            tv.setTextColor(ViewUtils.getColor(R.color.white))
        } else {
            line_area.visibility = View.GONE
            tv.setTextColor(ViewUtils.getColor(R.color.color_theme))
        }
        drawable.setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_7), ViewUtils.getDp(R.dimen.dp_4))
        tv.setCompoundDrawables(null, null, drawable, null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3000 && (resultCode == Activity.RESULT_CANCELED || resultCode == Activity.RESULT_OK)) {
            Log.e("TAG", "GPS RESULT  resultCode=$resultCode")
            preStartLocation()
        }
    }

    private var manualLocation = false

    protected var nowTextCity = ""
    protected fun updateCity(cityName: String) {
        nowTextCity = cityName
        TextUtils.setMaxEms(tv_area_qu, cityName, 4)
    }

    private val typeDialog: AreaTypePopupWindow by lazy {
        AreaTypePopupWindow(this).apply {
            onAreaScopeListener = object : AreaTypePopupWindow.OnAreaScopeListener {
                override fun notice(msg: String) {
                    showNotice(msg)
                }

                override fun reset() {
                    val tag = typeId != null
                    this@LocationActivity.typeId = null
                    this@LocationActivity.tv_area_type.text = "小区类型"
                    this@LocationActivity.tv_area_type.setTextColor(textColor)
                    if (tag) {
                        if (null != areaCode && null != cityName)
                            refreshBuilding(getBuildJson(areaCode!!, cityName!!))
                    }
                }

                override fun select(text: String, id: String?) {
                    if (text == "不限" && typeId != null) {
                        reset()
                        return
                    }
                    if (typeId != id) {
                        this@LocationActivity.typeId = id
                        this@LocationActivity.tv_area_type.text = text
                        this@LocationActivity.tv_area_type.setTextColor(textColorSelect)
                        if (null != areaCode && null != cityName)
                            refreshBuilding(getBuildJson(areaCode!!, cityName!!))
                    }
                }

                override fun dismiss() {
                    this@LocationActivity.fl_area_type.background = getThemeDrawable()
                    setRightDrawable(this@LocationActivity.tv_area_type, R.mipmap.xia_fill, true)
                }
            }
        }
    }
    private val scopeDialog: AreaScopePopupWindow by lazy {
        AreaScopePopupWindow(this).apply {
            onAreaScopeListener = object : AreaScopePopupWindow.OnAreaScopeListener {
                override fun notice(msg: String) {
                    showNotice(msg)
                }

                override fun reset() {
                    val tag = range != null
                    this@LocationActivity.range = null
                    this@LocationActivity.tv_area_scope.text = "投放区域"
                    this@LocationActivity.tv_area_scope.setTextColor(textColor)
                    if (tag) {
                        if (null != areaCode && null != cityName)
                            refreshBuilding(getBuildJson(areaCode!!, cityName!!))
                    }
                }

                override fun select(text: String, canshu: String?) {
                    if (text == "不限" && range != null) {
                        reset()
                        return
                    }
                    if (range != canshu) {
                        this@LocationActivity.range = canshu
                        this@LocationActivity.tv_area_scope.text = text
                        this@LocationActivity.tv_area_scope.setTextColor(textColorSelect)
                        if (null != areaCode && null != cityName)
                            refreshBuilding(getBuildJson(areaCode!!, cityName!!))
                    }
                }

                override fun dismiss() {
                    this@LocationActivity.fl_area_scope.background = getThemeDrawable()
                    setRightDrawable(this@LocationActivity.tv_area_scope, R.mipmap.xia_fill, true)
                }
            }
        }
    }
    private fun getThemeDrawable():Drawable?{
        return null
    }
    protected fun loadAreaTypeTask() {
        RetrofitService.getInstance().getCommunityRangeType(object : RetrofitService.CallbackClassResult<AreaTypeBean>(AreaTypeBean::class.java) {
            override fun successResult(t: AreaTypeBean) {
                val typeList = t.data.typeList
                if (TextUtils.isNotEmptyList(typeList)) {
                    updateAreaTypeAndScope(typeList)
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                showNotice(error)
            }
        })
    }

    private fun updateAreaTypeAndScope(typeList: MutableList<AreaTypeBean.DataBean.TypeListBean>) {
        typeDialog.setData(typeList)
    }

    protected fun getBuildJson(areaCode: String, cityName: String): String {
        if (cityName != XspManage.getInstance().aotuLocalCity) {
            scopeDialog.setIsUnClick(true)
        } else {
            scopeDialog.setIsUnClick(false)
        }
        this.areaCode = areaCode
        this.cityName = cityName
        val map = HashMap<String, String?>()
        map["areaCode"] = areaCode
        map["typeId"] = typeId
        if (XspManage.getInstance().aotuLocalCity == cityName && !TextUtils.isEmpty(range)) {
            map["lng"] = XspManage.getInstance().lng
            map["lat"] = XspManage.getInstance().lat
            map["range"] = range
        }
        return JsonUtils.toJsonString(map)
    }

    protected fun addMarker(position: Int, text: String, lat: Double, lng: Double) {
        val markerOption = MarkerOptions()
        markerOption.position(LatLng(lat, lng))
        markerOption.title(position.toString()).snippet("")
        markerOption.draggable(false)//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromView(createMarkerView(text)))
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.isFlat = true//设置marker平贴地图效果
        var marker = aMap.addMarker(markerOption)
        listMarker.add(marker)
    }

    private fun createMarkerView(text: String): View {
        val markerView = ToastNoticeView(this)
        //markerView.layoutParams = ViewGroup.LayoutParams(ViewUtils.getDp(R.dimen.dp_92), ViewGroup.LayoutParams.WRAP_CONTENT)
        markerView.text = text
        markerView.bgColor = ViewUtils.getColor(R.color.color_theme)
        return markerView
    }

    private fun clearMarker() {
        rl_map_bottom.visibility = View.GONE
        listMarker.forEach {
            it.remove()
        }
        listMarker.clear()
    }

    private val geocodeSearch: GeocodeSearch by lazy {
        GeocodeSearch(this).apply {
            setOnGeocodeSearchListener(object : GeocodeSearch.OnGeocodeSearchListener {
                override fun onRegeocodeSearched(regeocodeResult: RegeocodeResult?, p1: Int) {
                    /*if (p1 == 1000)
                        regeocodeResult?.let {
                            var regeocodeAddress = it.regeocodeAddress
                            var latLonPoint = it.regeocodeQuery.point
                            Log.e("TAG", "code=" + regeocodeAddress.adCode + " ,,asdf")
                        }*/
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
                                Log.e("TAG", "onGeocodeSearched: = latLonPoint=${latLonPoint!!.latitude}, ${latLonPoint!!.longitude}")
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