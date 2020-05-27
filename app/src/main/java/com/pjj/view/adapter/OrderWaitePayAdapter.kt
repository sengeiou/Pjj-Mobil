package com.pjj.view.adapter

import android.app.Activity
import android.content.Context
import com.pjj.R
import com.pjj.module.OrderResultBean
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.PayDialogHelp
import java.math.BigDecimal

/**
 * Created by XinHeng on 2018/12/07.
 * describe：待支付
 */
class OrderWaitePayAdapter(c: Context) : OrderAdapter(c) {
    override fun getOrderStatue(): String {
        return "订单待支付"
    }

    override fun getOrderStatueColor(): Int {
        return ViewUtils.getColor(R.color.color_ea4a4a)
    }

    override fun getLeftText(): String {
        return "支付订单"
    }

    override fun getRightText(): String {
        return "查看详情"
    }

    override fun getOnLeftClick(date: OrderResultBean.DataBean) {
        var orderId = date.orderId
        onWaitePayListener?.pay(orderId, date.amount.toString(),date.orderType)
    }

    var onWaitePayListener: OnWaitePayListener? = null

    interface OnWaitePayListener {
        fun pay(orderId: String, price: String?, orderType: String? = null)
    }

}
