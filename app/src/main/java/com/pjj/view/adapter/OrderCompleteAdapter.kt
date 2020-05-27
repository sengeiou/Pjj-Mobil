package com.pjj.view.adapter

import android.content.Context
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.OrderResultBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.DateUtils
import com.pjj.utils.ViewUtils
import java.util.*

/**
 * Created by XinHeng on 2018/12/07.
 * describe：已结束
 */
class OrderCompleteAdapter(c: Context) : OrderAdapter(c) {
    override fun getOrderStatue(): String {
        return "订单已结束"
    }

    override fun getOrderStatueColor(): Int {
        return ViewUtils.getColor(R.color.color_a7a8a8)
    }

    override fun getLeftText(): String {
        return "再次发布"
    }

    override fun getRightText(): String {
        return "查看详情"
    }

    override fun getOnLeftClick(date: OrderResultBean.DataBean) {
        var orderType = date.orderType
        if (orderType == "5" /*|| orderType == "7" || orderType == "9"*/) {
            onOrderCompleteListener?.notice(PjjApplication.Un_Show)
            return
        }
        var orderId = date.orderId
        try {
            XspManage.getInstance().identityType = date.authType.toInt()
            XspManage.getInstance().adType = orderType.toInt()
        } catch (e: Exception) {
            onOrderCompleteListener?.notice("订单类型错误")
            return
        }

        onOrderCompleteListener?.reRelease(orderId, orderType)
    }

    var onOrderCompleteListener: OnOrderCompleteListener? = null
    override fun tuiGuang(date: OrderResultBean.DataBean) {
        val fileList = date.templet.fileList
        val second = if (fileList.size == 2) fileList[1].fileUrl else null
        onOrderCompleteListener?.tuiGuang(fileList[0].fileUrl, second, date.name + "(等" + date.communityNum + "小区)",
                date.playDate, DateUtils.getSf1(Date(date.createTime)), date.orderId)
    }

    interface OnOrderCompleteListener {
        fun reRelease(orderId: String, adType: String)
        fun notice(msg: String)
        fun tuiGuang(first: String, second: String?, communityNum: String, releaseTime: String, createTime: String, orderId: String)
    }
}
