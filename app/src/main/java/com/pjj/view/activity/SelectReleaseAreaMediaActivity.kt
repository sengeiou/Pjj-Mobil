package com.pjj.view.activity


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.pjj.R
import com.pjj.contract.SelectReleaseAreaMediaContract
import com.pjj.module.NewMediaBuildingBean
import com.pjj.present.SelectReleaseAreaMediaPresent
import com.pjj.utils.SpaceItemDecoration
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_select_release_area_media.*
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import com.pjj.module.MediaOrderInfBean
import com.pjj.module.xspad.NewMediaData
import com.pjj.module.xspad.XspManage
import com.pjj.utils.TextUtils
import com.pjj.view.adapter.BuildingInfNewMediaMoreAdapter


/**
 * Create by xinheng on 2019/03/27 11:38。
 * describe：新传媒选择发布地点
 */
class SelectReleaseAreaMediaActivity : LocationActivity<SelectReleaseAreaMediaPresent>(), SelectReleaseAreaMediaContract.View {
    private var localCity: String = ""
    private var adapter = BuildingInfNewMediaMoreAdapter().apply {
        onItemSelectListener = object : BuildingInfNewMediaMoreAdapter.OnItemSelectListener {
            override fun selectAllStatue(isAll: Boolean, selectAllStatue: Boolean) {
                if (selectAllStatue) {
                    this@SelectReleaseAreaMediaActivity.iv_all_select.setImageResource(R.mipmap.unclickselect)
                    return
                }
                this@SelectReleaseAreaMediaActivity.iv_all_select.setImageResource(if (isAll) R.mipmap.select_red else R.mipmap.unselect)
            }
        }
    }

    override fun createParmJson(areaCode: String, cityName: String): String {
        return getBuildJson(areaCode, cityName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_release_area_media)
        //setTitle("选择发布地点")
        rv_guess.run {
            layoutManager = LinearLayoutManager(this@SelectReleaseAreaMediaActivity)
            adapter = this@SelectReleaseAreaMediaActivity.adapter
            addItemDecoration(SpaceItemDecoration(this@SelectReleaseAreaMediaActivity, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_1), ViewUtils.getColor(R.color.color_f1f1f1)))
            //(itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            itemAnimator = null
        }
        initViews(savedInstanceState)
        mPresent = SelectReleaseAreaMediaPresent(this)
        tv_area_qu.setOnClickListener(onClick)
        et_area.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterData()
            }

        })
        iv_all_select.setOnClickListener(onClick)
        tv_all_select.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
        fl_area_scope.setOnClickListener(onClick)
        fl_area_type.setOnClickListener(onClick)
        iv_back.setOnClickListener(onClick)
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
            //R.id.tv_area_qu -> startActivityForResult(Intent(this, CitiesActivity::class.java).putExtra("now_city", localCity), 200)
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
                startActivity(Intent(this, MakeOrderActivity::class.java))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            var areaCode = data?.getStringExtra("areaCode")
            var areaName = data?.getStringExtra("areaName")!!
            updateCity(areaName)
            XspManage.getInstance().cityName = areaName
            XspManage.getInstance().cityNameWeather = data?.getStringExtra("cityName")
            areaCode?.let {
                et_area.setText("")
                mPresent?.loadGuessListTask(getBuildJson(it, areaName))
            }
        }
    }

    override fun updateGuessBuildingList(list: MutableList<NewMediaBuildingBean.CommunityListBean>?, hotCommunityList: MutableList<NewMediaBuildingBean.CommunityListBean>?) {
        adapter.listOld = list
        adapter.hotData = hotCommunityList
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
        val communityListBean = list!![positionFirst]
        val selectList = ArrayList<NewMediaData.ScreenBean>()
        val communityList = ArrayList<MediaOrderInfBean.OrderScreenListBean>()
        var childSize = 0
        var min = 2000000000f
        communityListBean.screenList?.forEach { child ->
            if (child.screenStatus != "2") {
                ++childSize
                if (min > child.finalXspPrice) {
                    min = child.finalXspPrice
                }
                selectList.add(NewMediaData.ScreenBean().apply {
                    srceenId = child.screenId
                    price = child.finalXspPrice
                    screenName = child.screenName
                })
            }
        }
        if (childSize > 0) {
            communityList.add(communityListBean.cloneInf(childSize, min))
        }
        if (communityList.size > 0) {
            XspManage.getInstance().newMediaData.communityList = communityList
            XspManage.getInstance().newMediaData.screenIdList = selectList
            startActivity(Intent(this, MakeOrderActivity::class.java))
        } else {
            showNotice("当前大厦暂无可用屏幕")
        }
    }

    override fun updateAreaName(areaName: String) {
        updateCity(areaName)
        localCity = areaName
    }

}
