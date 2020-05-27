package com.pjj.view.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.ScreenMediaContract
import com.pjj.module.NewMediaScreenBean
import com.pjj.present.ScreenMediaPresent
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.ScreenMediaAdapter
import kotlinx.android.synthetic.main.activity_screenmedia.*

/**
 * Create by xinheng on 2019/03/27 14:00。
 * describe：新传媒广告屏选择
 */
class ScreenMediaActivity : BaseActivity<ScreenMediaPresent>(), ScreenMediaContract.View {
    private var communityId: String? = null
    private var adapterScreen: ScreenMediaAdapter = ScreenMediaAdapter().apply {
        onScreenMediaAdapterListener = object : ScreenMediaAdapter.OnScreenMediaAdapterListener {
            override fun hasSelect() {
                setSubmit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenmedia)
        setTitle("选择屏幕")
        mPresent = ScreenMediaPresent(this)
        communityId = intent.getStringExtra("communityId")
        rv_screen.layoutManager = LinearLayoutManager(this)
        rv_screen.adapter = adapterScreen
        tv_submit.setOnClickListener(onClick)
        communityId?.let {
            mPresent?.loadScreenDataList(it)
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_submit -> {
                startActivity(Intent(this, SelectNewMediaTimeActivity::class.java))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateView(bean: NewMediaScreenBean.DataBean) {
        Glide.with(this).load(PjjApplication.filePath + bean.imgName).into(iv_building)
        tv_building_name.text = bean.communityName
        tv_building_location.text = bean.position
        tv_elevator_num.text = "${bean.screenList.size} 面屏幕"
        adapterScreen.list = bean.screenList
        if (!TextUtils.isNotEmptyList(bean.screenList)) {
            setUnSubmit()
        }
    }

    private fun setUnSubmit() {
        tv_submit.background = ColorDrawable(ViewUtils.getColor(R.color.color_d2d2d2))
        tv_submit.isEnabled = false
    }

    private fun setSubmit() {
        if (tv_submit.isEnabled) {
            return
        }
        tv_submit.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
        tv_submit.isEnabled = true
    }
}
