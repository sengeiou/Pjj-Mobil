package com.pjj.view.adapter

import android.content.Context
import com.pjj.R
import com.pjj.module.OrderResultBean
import com.pjj.utils.DateUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.JianBoListDialog
import java.util.*

/**
 * Created by XinHeng on 2018/12/07.
 * describe：发布中
 */
class OrderReleaseAdapter(context: Context) : OrderAdapter(context) {
    private var orderId: String? = null
    override fun getOrderStatue(): String {
        return "订单发布中"
    }

    override fun getOrderStatueColor(): Int {
        return ViewUtils.getColor(R.color.color_theme)
    }

    override fun getLeftText(): String {
        return "监 播"
    }

    override fun isJianBo(): Boolean {
        return true
    }

    override fun getRightText(): String {
        return "查看详情"
    }

    override fun getOnLeftClick(date: OrderResultBean.DataBean) {
        orderId = date.orderId
        val orderType = date.orderType
/*        if (orderType == "7") {
            if (TextUtils.isNotEmptyList(date.screenList)) {
                adapterAskNetWorkListener?.toNewMediaPager(date.orderId, date.screenList[0].screenId)
            } else {
                adapterAskNetWorkListener?.noPlay()
            }
            return
        }*/
        var canPlayElevator = date.canPlayElevator
        //
        if (TextUtils.isNotEmptyList(canPlayElevator)) {
            jianBoDialog.setDataList(canPlayElevator, orderType != "7")
        } else {
            adapterAskNetWorkListener?.noPlay()
        }
    }

    /**
     * 监播屏幕列表弹窗
     */
    private val jianBoDialog: JianBoListDialog by lazy {
        JianBoListDialog(context).apply {
            onItemClickListener = object : JianBoListDialog.OnItemClickListener {
                override fun itemClick(register: String, zhiBoUrl: String?) {
                    orderId?.let {
                        adapterAskNetWorkListener?.loadIsPlayTask(it, register, zhiBoUrl)
                    }
                }

                override fun screenshots(screenId: String) {
                    orderId?.let {
                        adapterAskNetWorkListener?.screenshots(it, screenId)
                    }
                }
            }
        }
    }

    override fun tuiGuang(date: OrderResultBean.DataBean) {
        val fileList = date.templet.fileList
        val second = if (fileList.size == 2) fileList[1].fileUrl else null
        adapterAskNetWorkListener?.tuiGuang(fileList[0].fileUrl, second, date.name + "(等" + date.communityNum + "小区)",
                date.playDate, DateUtils.getSf1(Date(date.createTime)), date.orderId)
    }

    var adapterAskNetWorkListener: AdapterAskNetWorkListener? = null

    interface AdapterAskNetWorkListener {
        fun loadIsPlayTask(orderId: String, screenId: String, zhiBoUrl: String?)
        fun noPlay()
        fun toNewMediaPager(orderId: String, screenId: String)
        fun screenshots(orderId: String, screenId: String)
        fun tuiGuang(first: String, second: String?, communityNum: String, releaseTime: String, createTime: String, orderId: String)
    }
}
