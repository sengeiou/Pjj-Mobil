package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle

import com.pjj.R
import com.pjj.contract.AreaContract
import com.pjj.module.AreaBean
import com.pjj.present.AreaPresent
import com.pjj.view.adapter.AreaAdapter
import kotlinx.android.synthetic.main.activity_area.*

/**
 * Create by xinheng on 2018/12/01 15:53。
 * describe：地区，市下级区
 */
class AreaActivity : BaseActivity<AreaPresent>(), AreaContract.View {
    private lateinit var areaAdapter: AreaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area)
        val cityName = intent.getStringExtra(CITY_NAME)
        setTitle(cityName)
        mPresent = AreaPresent(this)
        cityName?.let {
            mPresent?.loadAreaListTask(cityName)
        }
        rv_area.adapter = AreaAdapter().apply {
            areaAdapter = this
            onItemClickListener = object : AreaAdapter.OnItemClickListener {
                override fun itemClick(areaCode: String,areaName:String) {
                    startActivity(Intent(this@AreaActivity,BuildingActivity::class.java)
                            .putExtra("area_name",areaName)
                            .putExtra("areaCode",areaCode))
                }
            }
        }
    }


    override fun updateListView(list: MutableList<AreaBean.CountyListBean>) {
        areaAdapter.list = list
    }

    companion object {
        val CITY_NAME = "city_name"
        fun toInstance(activity: Activity, cityName: String) {
            activity.startActivity(Intent(activity, AreaActivity::class.java).putExtra(CITY_NAME, cityName))
        }
    }
}
