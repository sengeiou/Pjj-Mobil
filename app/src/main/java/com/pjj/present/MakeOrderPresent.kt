package com.pjj.present

import com.pjj.contract.MakeOrderContract
import com.pjj.intent.RetrofitService
import com.pjj.module.*
import com.pjj.module.parameters.NewMediaMakeOrder
import com.pjj.module.xspad.XspManage
import com.pjj.utils.JsonUtils
import com.pjj.utils.Log

/**
 * Create by xinheng on 2019/03/27 16:33。
 * describe：P层
 */
class MakeOrderPresent(view: MakeOrderContract.View) : BasePresent<MakeOrderContract.View>(view, MakeOrderContract.View::class.java), MakeOrderContract.Present {
    override fun loadUseTime(screenIds: String, selectDate: String, orderType: String) {
        retrofitService.getElevatorTime(screenIds, selectDate, orderType, object : RetrofitService.MyCallback() {
            override fun success(s: String?) {
                Log.e("TAG", "success: $s")
                val timeOJ = TimeObject(s)
                if (timeOJ.isSuccess) {
                    val filter = timeOJ.filter(selectDate)
                    mView.updateResult(filter[0], filter[1])
                } else {
                    fail(timeOJ.msg)
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadMakeOrder(newMediaMakeOrder: NewMediaMakeOrder) {
        mView.showWaiteStatue()//MakeOrderScreenBean
        retrofitService.generateNewMediaOrder(newMediaMakeOrder, object : RetrofitService.MyCallback() {
            override fun success(s: String) {
                Log.e("TAG", "success=$s")
                //var s = "{\"msg\":\"时间段已被占用或广告屏已不存在,请刷新页面\",\"flag\":\"2\",\"data\":{\"useFullScreen\":[{\"screenId\":\"20_32_33_40_4e_a6_test\",\"orderTime\":\"2019-06-04\",\"screenName\":\"朗琴国际B座二楼小屏\"}],\"offLineScreen\":[]},\"status\":\"error\"}"
                val parse = JsonUtils.parse(s, MakeOrderScreenBean::class.java)
                when {
                    parse.isSuccess -> successResult(parse)
                    parse.flag == "0" -> fail(parse.msg)
                    else -> {
                        mView.cancelWaiteStatue()
                        mView.updateMakeOrderFail(parse.data)
                    }
                }
            }

            private fun successResult(t: MakeOrderScreenBean) {
                //mView.cancelWaiteStatue()
                mView.updateMakeOrderSuccess(t.orderId)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.updateMakeOrderFail(error)
            }
        })
    }

    override fun loadAliPayTask(orderId: String) {
        mView.showWaiteStatue()
        retrofitService.loadAliPayTask(orderId, object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(t: CipherPayZhifubaoBean) {
                //mView.payCipherSuccess()
                mView.payCipherSuccess(t.data, orderId)
                mView.cancelWaiteStatue()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun loadWeiXinPayTask(orderId: String) {
        mView.showWaiteStatue()
        retrofitService.loadWeiXinPayTask(orderId, object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(t: CipherPayZhifubaoBean) {
                mView.payCipherSuccess(t.data, orderId)
                mView.cancelWaiteStatue()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun loadPayResult(orderId: String) {
        retrofitService.loadPayResultTask(orderId, object : RetrofitService.CallbackClassResult<MyPayResultBean>(MyPayResultBean::class.java) {
            override fun successResult(t: MyPayResultBean) {
                mView.payResult(true, "")
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.payResult(false, error)
            }
        })
    }
}
