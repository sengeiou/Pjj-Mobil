package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.CreateAddressContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.AllCityObject
import com.pjj.module.ResultBean

/**
 * Create by xinheng on 2019/04/11 16:57。
 * describe：P层
 */
class CreateAddressPresent(view: CreateAddressContract.View) : BasePresent<CreateAddressContract.View>(view, CreateAddressContract.View::class.java), CreateAddressContract.Present {
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

    override fun insertReceivingAddress(areaCode: String, position: String, describe: String, postId: String, phone: String, name: String, isDefault: String) {
        IntegralRetrofitService.instance.insertReceivingAddress(PjjApplication.application.userId, areaCode, position, describe, postId, phone, name, isDefault, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.saveSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun updateReceivingAddress(addressId: String, areaCode: String, position: String, describe: String, postId: String, phone: String, name: String, isDefault: String) {
        IntegralRetrofitService.instance.updateReceivingAddress(PjjApplication.application.userId, addressId, areaCode, position, describe, postId, phone, name, isDefault, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.saveSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadDeleteAddressTask(addressId: String) {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.deleteReceivingAddress(addressId,object :RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java){
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.deleteSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }
}
