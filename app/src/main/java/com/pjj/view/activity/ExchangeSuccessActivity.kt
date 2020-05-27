package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle

import com.pjj.R
import com.pjj.contract.ExchangeSuccessContract
import com.pjj.module.xspad.XspManage
import com.pjj.present.ExchangeSuccessPresent
import kotlinx.android.synthetic.main.activity_exchange_success.*

/**
 * Create by xinheng on 2019/04/12 17:09。
 * describe：兑换成功
 */
class ExchangeSuccessActivity : BaseActivity<ExchangeSuccessPresent>(), ExchangeSuccessContract.View {
    private var orderId: String? = null
    private var storeId: String? = null
    private var mallTag = false
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_success)
        mallTag = intent.getBooleanExtra("mallTag", false)
        setBlackTitle(if (mallTag) "支付成功" else "兑换成功")
        tv_text.text = if (mallTag) "恭喜您支付成功" else "恭喜您兑换成功"
        tv_look_inf.text = if (mallTag) "查看订单" else "查看详情"
        orderId = intent.getStringExtra("orderId")
        storeId = intent.getStringExtra("storeId")
        tv_name.text = "收货人：${intent.getStringExtra("name")}"
        tv_phone.text = intent.getStringExtra("phone")
        tv_address.text = "收货地址：${intent.getStringExtra("address")}"
        tv_look_inf.setOnClickListener(onClick)
        XspManage.getInstance().cleanIntegralGoods()
    }

    override fun titleLeftClick() {
        if (mallTag) {
            startActivity(Intent(this, MallHomepageActivity::class.java))
        } else
            startActivity(Intent(this, IntegralMallActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        titleLeftClick()
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_look_inf -> {
                if (mallTag)
                    startActivity(Intent(this, MallOrderInfActivity::class.java)
                            .putExtra("storeId", storeId)
                            .putExtra("goodOrderId", orderId))
                else
                    startActivity(Intent(this, ExchangeRecordInfActivity::class.java).putExtra("orderId", orderId))
            }
        }
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }
}
