package com.pjj.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View.GONE
import android.view.View.VISIBLE
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.ElevatorContract
import com.pjj.module.ElevatorBean
import com.pjj.module.parameters.Elevator
import com.pjj.module.xspad.XspManage
import com.pjj.present.ElevatorPresent
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.ElevatorExpandableListAdapter
import kotlinx.android.synthetic.main.activity_elevator.*

/**
 * Create by xinheng on 2018/11/21 09:48。
 * describe：选择电梯
 */
class ElevatorActivity : BaseActivity<ElevatorPresent>(), ElevatorContract.View {
    private var adapter: ElevatorExpandableListAdapter? = null

    companion object {
        fun start(activity: Context, communityId: String, communityName: String) {
            activity.startActivity(Intent(activity, ElevatorActivity::class.java).putExtra("communityId", communityId).putExtra("communityName", communityName))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elevator)
        setTitle("选择电梯", Color.WHITE, 0)
        mPresent = ElevatorPresent(this)
        mPresent?.loadElevatorListTask(Elevator(intent.getStringExtra("communityId"), intent.getStringExtra("communityName")))
        iv_all_select.visibility = GONE
        var drawable = ContextCompat.getDrawable(this, R.mipmap.add_shop)
        var dp = ViewUtils.getDp(R.dimen.dp_22)
        drawable?.setBounds(0, 0, dp, dp)
        tv_back_add.setCompoundDrawables(drawable, null, null, null)
        tv_sure.setCompoundDrawables(ContextCompat.getDrawable(this, R.mipmap.select_white).apply {
            this?.setBounds(0, 0, dp, dp)
        }, null, null, null)
        tv_sure.setOnClickListener(onClick)
        tv_back_add.setOnClickListener(onClick)
        noticeDialog.updateImage(R.mipmap.cry_white)
        //XspManage.getInstance().clearXspData()
    }

    override fun onResume() {
        if (!onCreateTag) {
            adapter?.updateForMangeXsp()
        }
        super.onResume()
    }


    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_sure -> {
                adapter?.setXspManageData()
                if (XspManage.getInstance().groupLocation.size > 0) {
                    startActivity(Intent(this@ElevatorActivity, TimeActivity::class.java))
                } else {
                    showNotice("请选择您要发布的屏幕")
                }
            }
            R.id.tv_back_add -> {
                adapter?.setXspManageData()
                finish()
            }
        }
    }

    private var requestOptions = RequestOptions().error(R.mipmap.building)
    override fun updateList(dataBean: ElevatorBean.DataBean) {
        dataBean.run {
            Glide.with(this@ElevatorActivity).load(PjjApplication.filePath + imgName).apply(requestOptions).into(iv_building)
            tv_building_name.text = communityName
            tv_building_location.text = position
            tv_ad_name.text = "${elevatorNum}部电梯"
            tv_elevator_num.text = "${screenNum}面屏幕"
            getGroupList()
        }
    }

    private fun ElevatorBean.DataBean.getGroupList() {
        if (TextUtils.isNotEmptyList(elevatorList)) {
            var groupList = ArrayList<ElevatorBean.DataBean.ElevatorListBean>()
            var childList = ArrayList<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>>()
            elevatorList.forEach {
                if (TextUtils.isNotEmptyList(it.screenList)) {
                    childList.add(it.screenList)
                    it.size = it.screenList.size
                    it.screenList = null
                    groupList.add(it)
                }
            }
            adapter = ElevatorExpandableListAdapter().apply {
                setOnSelectStatueListener {
                    iv_all_select.setImageResource(when (it) {
                        true -> R.mipmap.select
                        false -> R.mipmap.unselect
                    })
                }
                setList(communityName, groupList, childList)
                expandableList.setAdapter(this)
                iv_all_select.visibility = VISIBLE
                tv_iv_all_select.setOnClickListener {
                    selectAll()
                }
                iv_all_select.setOnClickListener {
                    selectAll()
                }
            }
        }
    }
}
