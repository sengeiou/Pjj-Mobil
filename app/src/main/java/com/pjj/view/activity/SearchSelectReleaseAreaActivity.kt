package com.pjj.view.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View

import com.pjj.R
import com.pjj.contract.SearchSelectReleaseAreaContract
import com.pjj.module.BuildingBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.SearchSelectReleaseAreaPresent
import com.pjj.utils.LettersComparator
import com.pjj.utils.TextUtils
import com.pjj.view.adapter.BuildingAdapter
import kotlinx.android.synthetic.main.activity_search_select_release_area.*
import java.util.*

/**
 * Create by xinheng on 2018/12/03 14:03。
 * describe：搜索选择发布地点
 */
class SearchSelectReleaseAreaActivity : BaseActivity<SearchSelectReleaseAreaPresent>(), SearchSelectReleaseAreaContract.View {
    private lateinit var adapter: BuildingAdapter
    private var selectTag: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_select_release_area)
        setTitle("选择发布地点", Color.WHITE, 0)
        //110102 西城区
        tv_location.text = "当前区域：${intent.getStringExtra("local_name")}"
        mPresent = SearchSelectReleaseAreaPresent(this)
        mPresent?.loadAreaBuildingTask(intent.getStringExtra("local_code"))
        rv_building.layoutManager = LinearLayoutManager(this)
        selectTag = XspManage.getInstance().adType > 2
        et_area.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var filter = s.toString().trim()
                adapter.filter(filter)
            }
        })
        if (selectTag) {
            tv_sure.setOnClickListener(onClick)
            tv_sure.visibility = View.VISIBLE
        }
    }

    override fun updateBuildingList(list: List<BuildingBean.CommunityListBean>) {
        var fillData = TextUtils.fillData(list)
        fillData?.let {
            Collections.sort(fillData, LettersComparator())
        }
        var fillData1 = TextUtils.addFirstLetter(fillData)

        adapter = BuildingAdapter(fillData1, selectTag).apply {
            setOnItemClickListener {
                var screenNum = 0
                try {
                    screenNum = it.screenNum.toInt()
                } catch (e: Exception) {
                }
                if (screenNum > 0) {
                    if (selectTag) {
                        // 控制
                    } else {
                        ElevatorActivity.start(this@SearchSelectReleaseAreaActivity, it.communityId, it.communityName)
                    }
                } else {
                    showNotice("该大厦没有广告屏")
                }
            }
        }
        rv_building.adapter = adapter
        bar_list.setOnLetterChangeListener { letter ->
            val position = adapter.getSortLettersFirstPosition(letter)
            if (position != -1) {
                if (rv_building.layoutManager is LinearLayoutManager) {
                    val manager = rv_building.layoutManager as LinearLayoutManager
                    manager.scrollToPositionWithOffset(position, 0)
                } else {
                    rv_building.layoutManager.scrollToPosition(position)
                }
            }
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_sure -> {
                var selectBuildingList = adapter.selectBuildings
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
