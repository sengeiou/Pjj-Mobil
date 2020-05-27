package com.pjj.view.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pjj.R
import kotlinx.android.synthetic.main.layout_pay_result_fail.*

/**
 * Created by XinHeng on 2018/12/05.
 * describe：
 */
class PayResultDialog(var success: Boolean, context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    override fun getDialogGravity(): Int {
        return Gravity.BOTTOM
    }

    private var clock = View.OnClickListener {
        when (it.id) {
            R.id.iv_close -> {
                onPayResultListener?.close()
                dismiss()
            }
            R.id.tv_order -> {
                //TODO 查看订单
                onPayResultListener?.result(success)
                dismiss()
            }
            R.id.tv_refresh -> {
                onPayResultListener?.refresh()
                dismiss()
            }
        }
    }

    init {
        if (success) {
            setContentView(R.layout.layout_pay_result_success)
        } else {
            setContentView(R.layout.layout_pay_result_fail)
            tv_refresh.setOnClickListener(clock)
        }
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        var tvOrder = findViewById<TextView>(R.id.tv_order)
        var ivClose = findViewById<ImageView>(R.id.iv_close)
        tvOrder.setOnClickListener(clock)
        ivClose.setOnClickListener(clock)
    }

    var errorMsg: String? = ""
        set(value) {
            if (!success && null != value) {
                tv_explain.text = value
                if(value.contains("取消")){
                    tv_refresh.visibility=View.INVISIBLE
                }else{
                    tv_refresh.visibility=View.VISIBLE
                }
                tv_explain.visibility = View.VISIBLE
                if (!isShowing) {
                    show()
                }
            }
            field = value
        }
    var onPayResultListener: OnPayResultListener? = null

    interface OnPayResultListener {
        fun result(success: Boolean)
        /**
         * 支付失败 刷新
         */
        fun refresh()
        fun close()
    }
}