package com.pjj.view.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.View
import com.pjj.R
import kotlinx.android.synthetic.main.layout_pay_order.*

/**
 * Created by XinHeng on 2018/12/05.
 * describe：
 */
class PayOrderDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    companion object {
        var PAY_WEIXIN = 0
        var PAY_ZHIFUBAO = 1
        var PAY_YINLIAN = 2
    }

    private var payType: Int = -1
    private var click = View.OnClickListener {
        when (it.id) {
            R.id.iv_weixin_select -> {
                recoverClickStatue()
                iv_weixin_select.setImageResource(R.mipmap.select)
                payType = PAY_WEIXIN
            }
            R.id.iv_zhifubao_select -> {
                recoverClickStatue()
                iv_zhifubao_select.setImageResource(R.mipmap.select)
                payType = PAY_ZHIFUBAO
            }
            R.id.iv_yinlian_select -> {
                recoverClickStatue()
                iv_yinlian_select.setImageResource(R.mipmap.select)
                payType = PAY_YINLIAN
            }
            R.id.iv_close -> {
                //onPayListener?.close()
                dismiss()
            }
            R.id.tv_pay -> onPayListener?.pay(payType)
        }
    }
    private var tag = true
    fun dismiss(tag: Boolean) {
        this.tag = tag
        super.dismiss()
    }

    init {
        setContentView(R.layout.layout_pay_order)
        iv_weixin_select.setOnClickListener(click)
        iv_zhifubao_select.setOnClickListener(click)
        iv_yinlian_select.setOnClickListener(click)
        tv_pay.setOnClickListener(click)
        iv_close.setOnClickListener(click)
        setOnDismissListener {
            if (tag) {
                onPayListener?.close()
            } else {
                tag = false
            }
        }
    }

    private fun recoverClickStatue() {
        iv_weixin_select.setImageResource(R.mipmap.unselect)
        iv_zhifubao_select.setImageResource(R.mipmap.unselect)
        iv_yinlian_select.setImageResource(R.mipmap.unselect)
    }

    fun show(allPrice: String?, zheKou0Tag: Boolean = true) {
        if (null != allPrice)
            tv_order_price_old.text = "¥$allPrice"
        if (!zheKou0Tag) {
            tv_order_zhe_kou.text = "无折扣"
            tv_order_price.text = "¥$allPrice"
        }
        show()
    }

    override fun getDialogGravity(): Int {
        return Gravity.BOTTOM
    }

    var onPayListener: OnPayListener? = null

    interface OnPayListener {
        fun pay(payType: Int)
        fun close()
    }
}
