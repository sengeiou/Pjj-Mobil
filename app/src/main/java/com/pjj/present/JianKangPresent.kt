package com.pjj.present

import com.google.gson.JsonObject
import com.pjj.PjjApplication
import com.pjj.contract.JianKangContract
import com.pjj.intent.RetrofitService
import com.pjj.module.AllCityObject
import com.pjj.module.ResultBean
import com.pjj.module.parameters.JiangKang
import com.pjj.utils.JsonUtils
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by XinHeng on 2019/03/26.
 * describe：
 */
class JianKangPresent(view: JianKangContract.View) : BasePresent<JianKangContract.View>(view, JianKangContract.View::class.java), JianKangContract.Present {

    init {
        loadAreaData()
    }

    override fun commit(param: JiangKang) {
        mView.showWaiteStatue()
        retrofitService.insertHealthyRegister(param, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.commitSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun loadAreaData() {
        retrofitService.getAreaAll(object : RetrofitService.MyCallback() {
            override fun success(s: String) {
                var cityObject = AllCityObject(s)
                mView.cancelWaiteStatue()
                if (cityObject.isSuccess) {
                    mView.getCityData(cityObject.jsonArray)
                } else {
                    mView.showNotice(cityObject.msg)
                }
            }
        })
    }
}