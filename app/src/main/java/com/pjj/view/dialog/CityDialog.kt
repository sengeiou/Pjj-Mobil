package com.pjj.view.dialog

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.google.gson.JsonArray
import com.pjj.R
import com.pjj.view.adapter.AllCityAdapter
import kotlinx.android.synthetic.main.layout_city_dialog_item.*

/**
 * Created by XinHeng on 2019/04/02.
 * describe：
 */
class CityDialog(context: Context, array: JsonArray?) : FullWithNoTitleDialog(context, 0) {

    private var areaId: String? = null
    private lateinit var province: String
    private lateinit var city: String
    private lateinit var area: String
    private val cityAdapter1: AllCityAdapter by lazy {
        AllCityAdapter(1).apply {
            onAllCityItemClickListener = object : AllCityAdapter.OnAllCityItemClickListener {
                override fun itemClick(ranking: Int, areaId: String, text: String, array: JsonArray?) {
                    cityAdapter2.list = array
                    province = text
                    this@CityDialog.areaId = null
                }
            }
        }
    }
    private val cityAdapter2: AllCityAdapter by lazy {
        AllCityAdapter(2).apply {
            onAllCityItemClickListener = object : AllCityAdapter.OnAllCityItemClickListener {
                override fun itemClick(ranking: Int, areaId: String, text: String, array: JsonArray?) {
                    cityAdapter3.list = array
                    city = text
                    this@CityDialog.areaId = null
                }
            }
        }
    }
    private val cityAdapter3: AllCityAdapter by lazy {
        AllCityAdapter(3).apply {
            onAllCityItemClickListener = object : AllCityAdapter.OnAllCityItemClickListener {
                override fun itemClick(ranking: Int, areaId: String, text: String, array: JsonArray?) {
                    area = text
                    this@CityDialog.areaId = areaId
                }
            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> dismiss()
            R.id.iv_select -> {
                if (areaId == null) {
                    onCityDialogListener?.notice("请选择地区")
                } else {
                    onCityDialogListener?.selectAreaId(areaId!!, province, city, area)
                    dismiss()
                }
            }
        }
    }

    init {
        setContentView(R.layout.layout_city_dialog_item)
        rv1.layoutManager = LinearLayoutManager(context)
        rv2.layoutManager = LinearLayoutManager(context)
        rv3.layoutManager = LinearLayoutManager(context)
        rv1.adapter = cityAdapter1
        rv2.adapter = cityAdapter2
        rv3.adapter = cityAdapter3
        cityAdapter1.list = array
        iv_select.setOnClickListener(onClick)
        iv_close.setOnClickListener(onClick)
    }

    var onCityDialogListener: OnCityDialogListener? = null

    interface OnCityDialogListener {
        fun selectAreaId(areaId: String, province: String, city: String, area: String)
        fun notice(msg: String)
    }
}