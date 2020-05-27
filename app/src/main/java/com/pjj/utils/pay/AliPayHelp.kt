package com.pjj.utils.pay

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import android.widget.Toast
import com.pjj.PjjApplication
import com.pjj.utils.Log


/**
 * Created by XinHeng on 2018/12/25.
 * describe：
 */
class AliPayHelp {
    private val SDK_PAY_FLAG = 1
    private var mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
//                    Toast.makeText(PjjApplication.application, payResult.getResult(),
//                            Toast.LENGTH_LONG).show()
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo = payResult.result// 同步返回需要验证的信息
                    val resultStatus = payResult.resultStatus
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        //showAlert(this@PayDemoActivity, getString(R.string.pay_success) + payResult)
                        onPayResultListener?.paySuccess()
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        //showAlert(this@PayDemoActivity, getString(R.string.pay_failed) + payResult)
                        onPayResultListener?.payFail(payResult.memo)
                    }
                }
                else -> super.handleMessage(msg)
            }
        }
    }

    fun pay(orderInfo: String, activity: Activity) {
        Thread(Runnable {
            val alipay = PayTask(activity)
            val result = alipay.payV2(orderInfo, true)
            var version = alipay.version
            Log.e("TAG", "pay: version=$version")
            val msg = Message()
            msg.what = SDK_PAY_FLAG
            msg.obj = result
            mHandler.sendMessage(msg)
        }).start()
    }

    var onPayResultListener: OnPayResultListener? = null

    interface OnPayResultListener {
        fun paySuccess()
        fun payFail(error: String)
    }
}