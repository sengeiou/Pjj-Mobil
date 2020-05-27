package com.pjj.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.pjj.R
import com.pjj.contract.BuildingContract
import com.pjj.module.BuildingBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.BuildingPresent
import com.pjj.view.adapter.BuildingInfAdapter
import com.pjj.view.adapter.BuildingSelectInfAdapter
import kotlinx.android.synthetic.main.activity_building.*

/**
 * Create by xinheng on 2018/12/19 15:22。
 * describe：大厦列表
 */
class BuildingActivity : BaseActivity<BuildingPresent>(), BuildingContract.View {
    private var buildAdapter: BuildingInfAdapter? = null
    private var buildRandomAdapter: BuildingSelectInfAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building)
        setTitle(intent.getStringExtra("area_name"))
        var areaCode = intent.getStringExtra("areaCode")
        var random = when (XspManage.getInstance().adType) {
            3, 4 -> true
            else -> false
        }
        rv_building.run {
            layoutManager = LinearLayoutManager(this@BuildingActivity, LinearLayoutManager.VERTICAL, false)
            adapter = when (random) {
                false -> {
                    tv_next.visibility = View.GONE
                    BuildingInfAdapter(true).apply {
                        buildAdapter = this
                        onItemSelectListener = object : BuildingInfAdapter.OnItemSelectListener {
                            override fun itemClick(communityId: String, communityName: String) {
                                ElevatorActivity.start(this@BuildingActivity, communityId, communityName)
                            }

                            override fun noXsp() {
                                showNotice("该大厦没有广告屏")
                            }
                        }
                    }
                }
                else -> {
                    tv_next.visibility = View.VISIBLE
                    tv_next.setOnClickListener(onClick)
                    BuildingSelectInfAdapter(true).apply {
                        buildRandomAdapter = this
                    }
                }
            }
        }
        mPresent = BuildingPresent(this).apply {
            loadBuildingListTask(areaCode)
        }
    }

    override fun updateBuildingList(communityList: MutableList<BuildingBean.CommunityListBean>) {
        buildAdapter?.list = communityList
        buildRandomAdapter?.setList(communityList)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_next -> {
                var selectBuildingList = buildRandomAdapter?.getSelectBuildingList()
                if (null == selectBuildingList) {
                    showNotice("请选择小区")
                } else {
                    XspManage.getInstance().buildList = selectBuildingList
                    if (XspManage.getInstance().bianMinPing == 1) {
                        SelectBMPingTemplateActivity.start(this, "")
                    } else {
                        SelectRandomTemplateActivity.start(this, "")
                    }
                }
            }
        }
    }
}
