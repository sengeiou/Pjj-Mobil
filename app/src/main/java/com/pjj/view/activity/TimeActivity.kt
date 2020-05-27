package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.pjj.BuildConfig

import com.pjj.R
import com.pjj.contract.TimeContract
import com.pjj.module.TimeDiscountBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.TimePresent
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.TimeExpandableListAdapter
import com.pjj.view.dialog.AddTemplateDialog
import com.pjj.view.fragment.*
import kotlinx.android.synthetic.main.activity_time.*
import kotlinx.android.synthetic.main.layout_xsp_count_price.*

/**
 * Create by xinheng on 2018/11/21 17:33。
 * describe：选择日期，进之前需进行广告屏数量的判断
 */
class TimeActivity : FragmentsActivity<ABTimeFragment<*>, TimePresent>(), TimeContract.View, ABTimeFragment.OnFragmentInteractionListener {
    private var timeDiscountBean: TimeDiscountBean? = null
    private var listFragment: ArrayList<OnXspCountChangeListener>? = null
    private var xspAllPrice: String = ""
    private var xspAllHours: Int = 0
    /**
     * 删除 标记
     */
    private var group_position = -1
    private var child_position = -1
    private val timeExpandableListAdapter: TimeExpandableListAdapter
        get() {
            return TimeExpandableListAdapter().apply {
                setOnDeleteListener(object : TimeExpandableListAdapter.OnDeleteListener {
                    override fun expanded(groupPosition: Int) {
                        if (expandableListTime.isGroupExpanded(groupPosition)) {
                            expandableListTime.collapseGroup(groupPosition)
                        } else {
                            expandableListTime.expandGroup(groupPosition)
                        }
                    }

                    override fun askDelete(group_position: Int, child_position: Int) {
                        this@TimeActivity.group_position = group_position
                        this@TimeActivity.child_position = child_position
                        deleteScreenDialog.show()
                    }

                    override fun onDelete() {
                        //TODO expandableListTime的更新没有效果，原因未知，暂时这样写
                        this@TimeActivity.expandableListTime.setAdapter(timeExpandableListAdapter)
                        ViewUtils.setHtmlText(tv_xsp_count, "屏幕数量：<font color=\"#40BBF7\">${XspManage.getInstance().xspNum}</font> 面")
                        noticeAllFragmentXspCountChange()
                    }
                })
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        setTitle("选择时间", Color.WHITE, 0)
        titleView.background = ColorDrawable(color)
        titleView.post {
            Log.e("TAG", "title : top=${titleView.bottom} ${titleView.measuredHeight}")
        }
        mPresent = TimePresent(this)
        mPresent?.loadTimeDiscountTask()
        tv_sure.setOnClickListener(onClick)
        expandableListTime.setAdapter(timeExpandableListAdapter)
        tv_dan_ri.setOnClickListener(onClick)
        tv_duo_ri.setOnClickListener(onClick)
        tv_zheng_zhou.setOnClickListener(onClick)
        tv_zheng_yue.setOnClickListener(onClick)
        ViewUtils.setHtmlText(tv_xsp_count, "屏幕数量：<font color=\"#40BBF7\">${XspManage.getInstance().xspNum}</font> 面")
        ViewUtils.setHtmlText(tv_xsp_time, "发布时长：<font color=\"#40BBF7\">0</font> 小时")
        ViewUtils.setHtmlText(tv_xsp_price, "订单金额：<font color=\"#40BBF7\">0</font> 元")
        onClick(R.id.tv_dan_ri)
        if (BuildConfig.APP_TYPE) {
            tv_zheng_zhou.isEnabled = false
            tv_zheng_yue.isEnabled = false
            tv_zheng_zhou.setTextColor(ViewUtils.getColor(R.color.color_666666))
            tv_zheng_yue.setTextColor(ViewUtils.getColor(R.color.color_666666))
        }
    }

    override fun getFragmentContainerViewId(): Int {
        return R.id.fl_content
    }

    override fun onResume() {
        super.onResume()
        XspManage.getInstance().orderContent = ""
    }

    private val deleteScreenDialog: AddTemplateDialog by lazy {
        AddTemplateDialog(this).apply {
            var dp70 = ViewUtils.getDp(R.dimen.dp_70)
            setImageResource(R.mipmap.screen_local, false, dp70, ViewUtils.getDp(R.dimen.dp_62))
            notice = "是否删除此屏幕？"
            leftText = "确定"
            onItemClickListener = object : AddTemplateDialog.OnItemClickListener {
                override fun leftClick() {
                    timeExpandableListAdapter.deleteChild(group_position, child_position)
                }
            }
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_dan_ri -> showFragment("tv_dan_ri")
            R.id.tv_duo_ri -> showFragment("tv_duo_ri")
            R.id.tv_zheng_zhou -> showFragment("tv_zheng_zhou")
            R.id.tv_zheng_yue -> showFragment("tv_zheng_yue")
            R.id.tv_sure -> {
                if (XspManage.getInstance().xspNum <= 0) {
                    showNotice("请选择广告屏")
                    return
                }
                if (xspAllHours > 0) {
                    startActivity(Intent(this@TimeActivity, SelectTemplateActivity::class.java)
                            .putExtra("xspAllPrice", xspAllPrice)
                            .putExtra("xspAllHours", xspAllHours.toString()))
                } else {
                    showNotice("请选择时间")
                }
            }
        }
    }

    override fun showWaiteStatue() {

    }

    override fun cancelWaiteStatue() {

    }


    override fun getFragment(tag: String): ABTimeFragment<*>? {
        var aBTimeFragment: ABTimeFragment<*>? = when (tag) {
            "tv_dan_ri" -> SelectDayTimeFragment.newInstance(SelectDayTimeFragment.ONE_DAY)
            "tv_duo_ri" -> SelectDayTimeFragment.newInstance(SelectDayTimeFragment.MORE_DAY)
            "tv_zheng_zhou" -> SelectWeekTimeFragment.newInstance(SelectWeekTimeFragment.WEEK_DAY)
            "tv_zheng_yue" -> SelectWeekTimeFragment.newInstance(SelectWeekTimeFragment.MONTH_DAY)
            else -> null
        }
        if (null == listFragment) {
            listFragment = ArrayList()
        }
        aBTimeFragment?.let {
            listFragment?.add(it)
        }
        return aBTimeFragment
    }

    /**
     * 通知所有fragment广告屏数量变化
     */
    private fun noticeAllFragmentXspCountChange() {
        listFragment?.run {
            this.forEach {
                it.countChange()
            }
        }
    }

    override fun saveTimeDiscountBean(timeDiscountBean: TimeDiscountBean) {
        this.timeDiscountBean = timeDiscountBean
    }

    /**
     * 获取一组广告屏id
     * @return 逗号隔开
     */
    override fun getXspIds(): String {
        return XspManage.getInstance().xspIds
    }

    /**
     * 广告类型
     * @return 便民/DIY
     */
    override fun getAdType(): Int {
        return XspManage.getInstance().adType
    }

    /**
     * 更新总额
     * @param money 总额
     */
    override fun updateAllXspPrices(money: String, hours: Int) {
        xspAllHours = hours
        xspAllPrice = money
        ViewUtils.setHtmlText(tv_xsp_price, "订单金额：<font color=\"#40BBF7\">$money</font> 元")
        ViewUtils.setHtmlText(tv_xsp_time, "发布时长：<font color=\"#40BBF7\">$hours</font> 小时")
    }

    override fun getTimeDiscountBean(): TimeDiscountBean? {
        return timeDiscountBean
    }
}
