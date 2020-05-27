package com.pjj.view.dialog

import android.app.Activity
import com.google.gson.JsonParser
import com.pjj.utils.Log
import com.pjj.utils.pay.AliPayHelp
import com.tencent.mm.opensdk.constants.Build
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * Created by XinHeng on 2018/12/27.
 * describe：支付以及支付结果弹窗
 */
class PayDialogHelp(var activity: Activity, private var onPayListener: OnPayListener) {
    var orderId_: String? = null
    private var payType = -1
    private var msgApi: IWXAPI? = null
    private val payDialog: PayOrderDialog by lazy {
        PayOrderDialog(activity).apply {
            onPayListener = object : PayOrderDialog.OnPayListener {
                override fun pay(payType: Int) {
                    this@PayDialogHelp.payType = payType
                    if (payType < 0) {
                        this@PayDialogHelp.onPayListener.showNotice("请选择支付方式")
                        return
                    }
                    if (payType == PayOrderDialog.PAY_YINLIAN) {
                        this@PayDialogHelp.onPayListener.showNotice("暂未开放")
                        return
                    }
                    if (payType == PayOrderDialog.PAY_WEIXIN) {
                        if (null == msgApi) {
                            msgApi = WXAPIFactory.createWXAPI(activity, null)
                        }
                        if (!msgApi!!.isWXAppInstalled) {
                            this@PayDialogHelp.onPayListener.showNotice("请安装微信")
                            return
                        }
                        var wxAppSupportAPI = msgApi!!.wxAppSupportAPI < Build.PAY_SUPPORTED_SDK_INT
                        if (wxAppSupportAPI) {
                            this@PayDialogHelp.onPayListener.showNotice("请安装新版微信，当前微信版本不支持支付")
                            return
                        }
                    }
                    this@PayDialogHelp.onPayListener.reLoadPay(payType, orderId_!!)
                    //this@PayDialogHelp.onPayListener.makeOrder(payType)
                    dismiss(false)
                }

                override fun close() {
                    if (null != orderId_)
                        this@PayDialogHelp.onPayListener.startOrderView(0)
                }
            }
        }
    }
    private var payResultListener = object : PayResultDialog.OnPayResultListener {
        override fun result(success: Boolean) {
            //TODO 支付失败、成功-查看订单
            if (success) {
                onPayListener.startOrderView(1)
            } else {//重新支付，从生成加密信息开始
                if (null != orderId_) {
                    onPayListener.reLoadPay(payType, orderId_!!)
                } else {
                    onPayListener.makeOrder(payType)
                }
            }
        }

        override fun close() {
            if (null != orderId_)
                onPayListener.startOrderView(0)
        }

        override fun refresh() {
            //重新获取支付结果，向公司服务器
            orderId_?.let {
                when (payType) {
                    PayOrderDialog.PAY_ZHIFUBAO -> onPayListener.loadAliPayResult(it)
                    PayOrderDialog.PAY_WEIXIN -> onPayListener.loadWeiXinPayResult(it)
                    PayOrderDialog.PAY_YINLIAN -> onPayListener.loadYinHangPayResult(it)
                }
            }
        }
    }
    private val successDialog: PayResultDialog by lazy {
        PayResultDialog(true, activity).apply {
            onPayResultListener = payResultListener
        }
    }
    private val failDialog: PayResultDialog by lazy {
        PayResultDialog(false, activity!!).apply {
            onPayResultListener = payResultListener
        }
    }

    interface OnPayListener {
        /**
         * 消息展示
         */
        fun showNotice(msg: String)

        /**
         * 已选支付方式
         * 目的：生产订单
         */
        fun makeOrder(payType: Int)

        /**
         * 重新支付
         */
        fun reLoadPay(payType: Int, orderId: String)

        /**
         * 前往订单页面
         * @param index 下表
         */
        fun startOrderView(index: Int)

        /**
         * 向公司服务器获取支付结果
         */
        fun loadAliPayResult(orderId: String)

        fun loadWeiXinPayResult(orderId: String)
        fun loadYinHangPayResult(orderId: String)
        /**
         * 付款成功
         */
        fun paySuccess(payType: Int)
    }

    fun loadPayResult() {
        orderId_?.let {
            when (payType) {
                PayOrderDialog.PAY_ZHIFUBAO -> onPayListener.loadAliPayResult(it)
                PayOrderDialog.PAY_WEIXIN -> onPayListener.loadWeiXinPayResult(it)
                PayOrderDialog.PAY_YINLIAN -> onPayListener.loadYinHangPayResult(it)
            }
        }
    }

    /**
     * 已获取到密文
     * 支付
     */
    fun payForOrderInfo(orderInfo: String, payType: Int) {
        if (this.payType != payType) {
            onPayListener.showNotice("支付方式错乱，请重新下单")
            return
        }
        when (payType) {
            PayOrderDialog.PAY_ZHIFUBAO -> aliPay(orderInfo)
            PayOrderDialog.PAY_WEIXIN -> weiXinPay(orderInfo)
            PayOrderDialog.PAY_YINLIAN -> yinHangPay(orderInfo)
        }
    }

    fun paySuccess() {
        successDialog.show()
    }

    private fun payFail(error: String?) {
        failDialog.errorMsg = error
    }

    fun setPayResult(result: Boolean, msg: String?) {
        if (result) {
            onPayListener.paySuccess(payType)
            paySuccess()
        } else {
            payFail(msg)
        }
    }

    fun showPayTypeDialog(price: String?, zheKou0Tag: Boolean = true) {
        payDialog.show(price, zheKou0Tag)
    }

    private fun aliPay(orderInfo: String) {
        AliPayHelp().apply {
            onPayResultListener = object : AliPayHelp.OnPayResultListener {
                override fun paySuccess() {
                    onPayListener.loadAliPayResult(orderId_!!)
                }

                override fun payFail(error: String) {
                    //showNotice(error)
                    failDialog.errorMsg = error
                }
            }
        }.pay(orderInfo, activity)
    }

    private fun weiXinPay(orderInfo: String) {
        if (null == msgApi) {
            msgApi = WXAPIFactory.createWXAPI(activity, null)
        }
        msgApi?.registerApp("wxf294318db839d271")
        Log.e("TAG", "weiXinPay: orderInfo=$orderInfo")
        var parse = JsonParser().parse(orderInfo)
        if (parse.isJsonObject) {
            var jsonObject = parse.asJsonObject
            var sendReq = msgApi?.sendReq(PayReq().apply {
                appId = jsonObject.get("appid").asString
                partnerId = jsonObject.get("partnerid").asString
                prepayId = jsonObject.get("prepayid").asString
                packageValue = jsonObject.get("package").asString
                nonceStr = jsonObject.get("noncestr").asString
                timeStamp = jsonObject.get("timestamp").asString
                sign = jsonObject.get("sign").asString
            })
            Log.e("TAG", "weiXinPay: sendReq=$sendReq")
        }
    }

    private fun yinHangPay(orderInfo: String) {

    }
}