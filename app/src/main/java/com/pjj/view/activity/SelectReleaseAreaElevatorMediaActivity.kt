package com.pjj.view.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.pjj.R
import kotlinx.android.synthetic.main.activity_select_release_area_media.*
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.pjj.contract.SelectReleaseAreaElevatorMediaContract
import com.pjj.module.BuildingElevatorBean
import com.pjj.module.xspad.NewMediaData
import com.pjj.module.xspad.XspManage
import com.pjj.present.SelectReleaseAreaElevatorMediaPresent
import com.pjj.utils.*
import com.pjj.view.adapter.BuildingElevatorInfMoreAdapter
import com.pjj.view.adapter.OrderElevatorInfAdapter


/**
 * Create by xinheng on 2019/03/27 11:38。
 * describe：新传媒选择发布地点
 */
class SelectReleaseAreaElevatorMediaActivity : LocationActivity<SelectReleaseAreaElevatorMediaPresent>(), SelectReleaseAreaElevatorMediaContract.View {
    private var localCity: String = ""
    private var adapter = BuildingElevatorInfMoreAdapter().apply {
        onItemSelectListener = object : BuildingElevatorInfMoreAdapter.OnItemSelectListener {
            override fun selectAllStatue(isAll: Boolean, unClickTag: Boolean) {
                Log.e("TAG", "selectAllStatue: unClickTag=$unClickTag")
                if (unClickTag) {
                    this@SelectReleaseAreaElevatorMediaActivity.iv_all_select.setImageResource(R.mipmap.unclickselect)
                    return
                }
                this@SelectReleaseAreaElevatorMediaActivity.iv_all_select.setImageResource(if (isAll) R.mipmap.select else R.mipmap.unselect)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_release_area_media)
        //setTitle("选择发布地点")
        rv_guess.run {
            layoutManager = LinearLayoutManager(this@SelectReleaseAreaElevatorMediaActivity)
            adapter = this@SelectReleaseAreaElevatorMediaActivity.adapter
            addItemDecoration(SpaceItemDecoration(this@SelectReleaseAreaElevatorMediaActivity, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_1), ViewUtils.getColor(R.color.color_f1f1f1)))
            //(itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            itemAnimator = null
        }
        initViews(savedInstanceState)
        mPresent = SelectReleaseAreaElevatorMediaPresent(this)
        tv_area_qu.setOnClickListener(onClick)
        iv_all_select.setOnClickListener(onClick)
        tv_all_select.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
        fl_area_scope.setOnClickListener(onClick)
        fl_area_type.setOnClickListener(onClick)
        iv_back.setOnClickListener(onClick)
        et_area.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData()
            }

        })
        preStartLocation()
    }

    override fun startLocation() {
        mPresent?.startLocation()
    }

    override fun startLoadCityData(cityName: String) {
        mPresent?.loadAreaBuildingTask(cityName)
        updateAreaName(cityName)
    }

    override fun refreshBuilding(json: String) {
        mPresent?.loadGuessListTask(json)
    }

    private fun filterData() {
        /*var systemService = getSystemService(Context.INPUT_METHOD_SERVICE)
        if (systemService is InputMethodManager) {
            val imm = systemService
            try {
                imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
        var string = et_area.text.toString()
        if (string.isEmpty()) {
            //tv_guess.visibility = View.VISIBLE
            adapter.restore()
            mapMarkerFullTag = false
            updateMarker()
            return
        }
        tv_guess.visibility = View.GONE
        adapter.filter(string)
        mapMarkerFullTag = false
        updateMarker(true)
    }

    override fun onClick(viewId: Int) {
        super.onClick(viewId)
        when (viewId) {
            R.id.tv_all_select, R.id.iv_all_select -> adapter.changeAllSelect()
            R.id.tv_sure -> {
                if (!gpsAddTag) {
                    preStartLocation()
                    return
                }
                if (!adapter.getSelectScreenIds()) {
                    showNotice("您还没有选择任何地点")
                    return
                }
                startActivity(Intent(this, MakeElevatorOrderActivity::class.java))
            }
        }
    }

    override fun createParmJson(areaCode: String, cityName: String): String {
        return getBuildJson(areaCode, cityName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            var areaCode = data?.getStringExtra("areaCode")
            var areaName = data?.getStringExtra("areaName")!!
//            tv_area_qu.text = areaName
            updateCity(areaName)
            XspManage.getInstance().cityNameWeather = data?.getStringExtra("cityName")
            XspManage.getInstance().cityName = areaName
            areaCode?.let {
                et_area.setText("")
                mPresent?.loadGuessListTask(getBuildJson(it, areaName))
            }
        }
    }

    override fun updateGuessBuildingList(list: MutableList<BuildingElevatorBean.DataBean>?, hotData: MutableList<BuildingElevatorBean.DataBean>?) {
        adapter.listOld = list
        adapter.hotData = hotData
        if (adapter.itemCount > 0) {
            ll_no_data.visibility = View.GONE
            //tv_guess.visibility = View.VISIBLE
            ll_bottom.visibility = View.VISIBLE
        } else {
            ll_no_data.visibility = View.VISIBLE
            tv_guess.visibility = View.GONE
            ll_bottom.visibility = View.GONE
        }
        et_area.setText("")
        //iv_all_select.setImageResource(R.mipmap.unselect)
        mapMarkerFullTag = false
        updateMarker()
    }

    override fun updateMapBottomView(position: Int) {
        val list = if (filterTag) adapter.filterList else adapter.listOld
        val bean = list!![position]
        val split = bean.position.split(" ")
        setMapBottomContent(bean.imgName, bean.communityName, split[split.size - 1])
    }

    override fun startAddMarker(filterTag: Boolean) {
        val list = if (filterTag) adapter.filterList else adapter.listOld
        if (TextUtils.isNotEmptyList(list)) {
            list!!.forEachIndexed { index, communityListBean ->
                addMarker(index, communityListBean.communityName + "\n" + "屏幕数${communityListBean.screenNum}面",
                        communityListBean.lat.toDouble(), communityListBean.lng.toDouble())
            }
        }
    }

    override fun mapSelectPosition(positionFirst: Int) {
        val list = if (filterTag) adapter.filterList else adapter.listOld
        val it = list!![positionFirst]
        val selectList = ArrayList<NewMediaData.ScreenBean>()
        val communityList = ArrayList<OrderElevatorInfAdapter.OrderElevatorInfParent>()
        var screenSize = 0
        var elevatorSize = 0
        var min = 200000000000f
        it.elevatorList?.forEach { elevator ->
            var hasChildSelect = false
            elevator.screenList?.forEach { child ->
                if (child.screenStatus != "2") {
                    ++screenSize
                    if (min > child.finalXspPrice) {
                        min = child.finalXspPrice
                    }
                    hasChildSelect = true
                    selectList.add(NewMediaData.ScreenBean().apply {
                        srceenId = child.screenId
                        price = child.finalXspPrice
                        screenName = child.screenName
                    })
                }
            }
            if (hasChildSelect)
                ++elevatorSize
        }
        if (screenSize > 0) {
            communityList.add(it.cloneInf(elevatorSize, screenSize,min))
        }

        if (communityList.size > 0) {
            XspManage.getInstance().newMediaData.elevatorCommunityList = communityList
            XspManage.getInstance().newMediaData.screenIdList = selectList
            startActivity(Intent(this, MakeElevatorOrderActivity::class.java))
        } else {
            showNotice("当前大厦暂无可用屏幕")
        }
    }

    override fun updateAreaName(areaName: String) {
//        tv_area_qu.text = areaName
        updateCity(areaName)
        localCity = areaName
    }
}
