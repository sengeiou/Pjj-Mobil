package com.pjj.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher

import com.pjj.R
import com.pjj.contract.CitiesContract
import com.pjj.module.CityBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.CitiesPresent
import com.pjj.utils.GpsUtils
import com.pjj.utils.LettersComparator
import com.pjj.utils.TextUtils
import com.pjj.view.adapter.CitiesAdapter
import com.pjj.view.dialog.CallPhoneDialog
import kotlinx.android.synthetic.main.activity_cities.*
import java.util.*

/**
 * Create by xinheng on 2018/12/19 16:38。
 * describe：选择城市
 */
class CitiesActivity : BaseActivity<CitiesPresent>(), CitiesContract.View {
    private var adapterCity: CitiesAdapter? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities)
        setTitle("选择城市")
        val localCity = TextUtils.clean(XspManage.getInstance().aotuLocalCity)
        tv_local_city.text = "当前定位城市：$localCity"
        tv_local_city.setOnClickListener {
            val data = adapterCity?.getSelectCity(XspManage.getInstance().aotuLocalCity)
            if (null == data) {
                showNotice("暂时查不到此城市")
                return@setOnClickListener
            }
            var areaCode = data.areaCode
            var areaName = data.areaName
            var cityName = data.cityName
            setResult(Activity.RESULT_OK, Intent()
                    .putExtra("cityName", cityName)
                    .putExtra("areaCode", areaCode)
                    .putExtra("areaName", areaName))
            finish()
        }
        /*et_area.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var filter = s.toString().trim()
                adapterCity?.filter(filter)
            }
        })*/
        tv_search.setOnClickListener {
            adapterCity?.filter(et_area.text.toString())
        }
        rv_building.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mPresent = CitiesPresent(this).apply {
            loadCitiesTask()
        }
    }

    private val gpsDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(this).apply {
            rightText = "去设置"
            phone = "请在设置中打开定位\n以确定您的位置"
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 3000)
                }
            }
        }
    }

    override fun updateCities(list: MutableList<CityBean.CityListBean>?) {
        adapterCity = CitiesAdapter(TextUtils.fillCityData(list)?.let {
            Collections.sort(it, LettersComparator())
            TextUtils.addFirstLetter(it)
        }).apply {
            setOnItemClickListener { data ->
                var areaCode = data.areaCode
                var areaName = data.areaName
                var cityName = data.cityName
                setResult(Activity.RESULT_OK, Intent()
                        .putExtra("cityName", cityName)
                        .putExtra("areaCode", areaCode)
                        .putExtra("areaName", areaName))
                finish()
            }
        }
        rv_building.adapter = adapterCity
        bar_list.setOnLetterChangeListener { letter ->
            adapterCity?.let {
                val position = it.getSortLettersFirstPosition(letter)
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
    }
}
