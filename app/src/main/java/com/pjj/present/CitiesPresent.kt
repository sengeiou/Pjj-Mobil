package com.pjj.present

import com.pjj.contract.CitiesContract
import com.pjj.intent.RetrofitService
import com.pjj.module.CityBean

/**
 * Create by xinheng on 2018/12/19 16:38。
 * describe：P层
 */
class CitiesPresent(view: CitiesContract.View) : BasePresent<CitiesContract.View>(view, CitiesContract.View::class.java), CitiesContract.Present {

    override fun loadCitiesTask() {
        mView.showWaiteStatue()
        retrofitService.getCities(object : RetrofitService.CallbackClassResult<CityBean>(CityBean::class.java) {
            override fun successResult(t: CityBean) {
                mView.cancelWaiteStatue()
                mView.updateCities(t.cityList)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }
}
