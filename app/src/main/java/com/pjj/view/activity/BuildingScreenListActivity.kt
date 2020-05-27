package com.pjj.view.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.AllBuildingScreenBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.CalculateUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.ScreenListAdapter
import kotlinx.android.synthetic.main.activity_building_screen_list.*

class BuildingScreenListActivity : BaseActivity<BasePresent<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building_screen_list)
        setTitle("详细信息")
        titleView.background = ColorDrawable(Color.TRANSPARENT)
        val selectAllBuildBean = XspManage.getInstance().newMediaData.selectAllBuildBean
        if (statue > 0) {
            fl_title.setPadding(0, statue, 0, 0)
        }
        selectAllBuildBean?.let {
            initData(it)
        }
    }

    override fun getStatusBarColor(): Int {
        return Color.TRANSPARENT
    }

    override fun fitSystemWindow(): Boolean {
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun initData(bean: AllBuildingScreenBean.DataBean) {
        Glide.with(this).load(PjjApplication.filePath + bean.titleImg).into(iv_top)
        tv_building.text = bean.communityName
        tv_building_address.text = bean.street
        tv_elevator_count.text = "${bean.elevatorList?.size ?: 0}部电梯"
        tv_indoor_count.text = "${bean.screenList?.size ?: 0}面室内屏"
        var colorSelect = "#999999"
        var colorSum = "#999999"
        var colorPrice = "#999999"
        if (bean.canUseCount > 0) {
            colorSelect = "#FF4C4C"
            colorSum = "#333333"
            colorPrice = "#FF8C19"
        }
        ViewUtils.setHtmlText(tv_select, "已选屏幕：<font color=\"$colorSelect\">${bean.selectCount}</font><font color=\"$colorSum\">/${bean.screenNum}</font>")
        ViewUtils.setHtmlText(tv_price, "<font color=\"$colorPrice\">¥${CalculateUtils.m1(bean.price)} 起</font>")
        ViewUtils.setHtmlText(tv_sum_price_name, "<font color=\"$colorSelect\">总价：</font>")
        ViewUtils.setHtmlText(tv_sum_price, "<font color=\"$colorSelect\">¥${CalculateUtils.m1(bean.sumPrice)}</font>")
        val topView = top
        val viewGroup = top.parent as ViewGroup
        viewGroup.removeView(topView)
        rv_screen.layoutManager = LinearLayoutManager(this)
        val screenListAdapter = ScreenListAdapter(this)
        screenListAdapter.top = topView
        screenListAdapter.data = bean
        rv_screen.adapter = screenListAdapter
        rv_screen.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var sumDy = 0
            val dp = ViewUtils.getDp(R.dimen.dp_198)
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                sumDy += dy
                if (sumDy >= dp - fl_title.measuredHeight) {
                    fl_title.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
                } else {
                    fl_title.background = ColorDrawable(getStatusBarColor())
                }
            }
        })
    }

    override fun titleLeftClick() {
        finishSelf()
    }

    override fun onBackPressed() {
        finishSelf()
    }

    private fun finishSelf() {
        setResult(303)
        finish()
    }
}
