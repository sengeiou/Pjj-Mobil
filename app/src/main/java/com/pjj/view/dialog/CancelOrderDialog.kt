package com.pjj.view.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.CalculateUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_cancel_order.*

/**
 * Created by XinHeng on 2019/01/03.
 * describe：取消订单
 */
class CancelOrderDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private lateinit var orderId: String
    private var onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.tv_sure -> {
                onCancelOrderListener?.let { listener ->
                    var msg = tv_error.text.toString()
                    if (TextUtils.isEmpty(msg) || msg == "请选择") {
                        listener.showNotice("请选择取消原因")
                        return@let
                    }
                    listener.sureCancel(msg, orderId)
                }
            }
            R.id.tv_error -> onCancelOrderListener?.selectCancelReason()
            R.id.iv_close -> dismiss()
        }
    }

    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    init {
        setContentView(R.layout.layout_cancel_order)
        tv_sure.setOnClickListener(onClickListener)
        iv_close.setOnClickListener(onClickListener)
        tv_error.setOnClickListener(onClickListener)
        setCanceledOnTouchOutside(false)
    }

    fun setPrice(orderId: String, orderPrice: String, backPrice1: String? = null) {
        this.orderId = orderId
        tv_order_price.text = orderPrice + "元"//订单金额
        var backPrice = try {
            CalculateUtils.m1(orderPrice.toFloat())
        } catch (e: Exception) {
            null
        }
        if (TextUtils.isEmpty(backPrice)) {
            tv_back_price.visibility = View.GONE
            tv_back_price_text.visibility = View.GONE
        } else {
            tv_back_price.visibility = View.VISIBLE
            tv_back_price_text.visibility = View.VISIBLE
            tv_back_price.text = backPrice + "元"//可退款金额
        }
        tv_error.text = "请选择"
        tv_error.setTextColor(ViewUtils.getColor(R.color.color_999999))
        if (!isShowing) {
            show()
        }
    }

    fun updateCancelReason(msg: String) {
        tv_error.text = msg
        tv_error.setTextColor(ViewUtils.getColor(R.color.color_333333))
    }

    var onCancelOrderListener: OnCancelOrderListener? = null

    interface OnCancelOrderListener {
        /**
         * 选择取消原因
         */
        fun selectCancelReason()

        fun showNotice(msg: String)
        fun sureCancel(msg: String, orderId: String)
    }
}