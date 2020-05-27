package com.pjj.view.adapter

import android.content.Context
import com.pjj.R
import com.pjj.module.OrderResultBean
import com.pjj.utils.CalculateUtils
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/07.
 * describe：审核中
 */
class OrderAuditAdapter(c: Context) : OrderAdapter(c) {
    override fun getOrderStatueColor(): Int {
        return ViewUtils.getColor(R.color.color_theme)
    }

    override fun getOrderStatue(): String {
        return "订单审核中"
    }

    override fun getLeftText(): String {
        return "取消订单"
    }

    override fun getRightText(): String {
        return "查看详情"
    }

    override fun getOnLeftClick(date: OrderResultBean.DataBean) {
       /* var makeText = Toast.makeText(PjjApplication.application, "暂未开通", Toast.LENGTH_SHORT)
        makeText.setGravity(Gravity.CENTER, 0, 0)
        makeText.show()*/
        onAdapterAudiListener?.cancelOrder(date.orderId,CalculateUtils.m1(date.amount.toFloat()))
    }

    var onAdapterAudiListener: OnAdapterAudiListener? = null

    interface OnAdapterAudiListener {
        fun cancelOrder(orderId:String,orderPrice:String)
    }

}
