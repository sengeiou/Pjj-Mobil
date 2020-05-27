package com.pjj.view.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import com.pjj.R
import com.pjj.contract.SelectNewMediaTimeContract
import com.pjj.module.xspad.XspManage
import com.pjj.present.SelectNewMediaTimePresent
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.DateViewGroup
import com.pjj.view.dialog.DateDialog
import kotlinx.android.synthetic.main.activity_selectnewmediatime.*
import java.util.*

/**
 * Create by xinheng on 2019/03/29 17:17。
 * describe：选择时间
 */
class SelectNewMediaTimeActivity : BaseActivity<SelectNewMediaTimePresent>(), SelectNewMediaTimeContract.View {
    /**
     * 日期弹窗
     */
    private val dateDialog: DateDialog by lazy {
        DateDialog(this, 0, DateViewGroup.ONLY_ONE, false).apply {
            setSelectDay(Calendar.getInstance())
            //setNumForMonth(5)
            onDateSelectListener = object : DateDialog.OnDateSelectListener {
                override fun dateSelect(calendar: Calendar) {
                    var dates = (calendar.let {
                        "${it[Calendar.YEAR]}-${TextUtils.format(it[Calendar.MONTH] + 1)}-${TextUtils.format(it[Calendar.DATE])}"
                    })
                    showWaiteStatue()
                    mPresent?.loadUseTime(XspManage.getInstance().newMediaData.screenId!!, dates, "7")
                }

                override fun dateSelect(dates: String) {
                    //var split = dates.split(",")
                    //var size = split.size
                    //showWaiteStatue()
                    //mPresent?.loadUseTime(XspManage.getInstance().newMediaData.screenId!!, dates, "7")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selectnewmediatime)
        setTitle("选择投放时间")
        mPresent = SelectNewMediaTimePresent(this)
        tv_date.setOnClickListener(onClick)
        iv_date_ori.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_date_ori, R.id.tv_date -> dateDialog.show()
            R.id.tv_sure -> {
                startActivity(Intent(this, MakeOrderActivity::class.java))
            }
        }
    }

    override fun updateResult(isUse: Boolean, dates: String?) {
        cancelWaiteStatue()
        dateDialog.dismiss()
        if (isUse) {
            dates?.let {
                this@SelectNewMediaTimeActivity.tv_date.text = it
                this@SelectNewMediaTimeActivity.tv_day_num.text = "1 天"
                XspManage.getInstance().newMediaData.dates = it
                if (!tv_sure.isEnabled) {
                    tv_sure.isEnabled = true
                    tv_sure.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
                }
            }
        } else {
            toast("当前时间不可用")
        }
    }
}
