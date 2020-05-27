package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.SelectAddressContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.AddressBean

/**
 * Create by xinheng on 2019/04/12 13:55。
 * describe：P层
 */
class SelectAddressPresent(view: SelectAddressContract.View) : BasePresent<SelectAddressContract.View>(view, SelectAddressContract.View::class.java), SelectAddressContract.Present {

    override fun loadAddressListTask() {
        mView.showWaiteStatue()
        IntegralRetrofitService.instance.getReceivingAddressList(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<AddressBean>(AddressBean::class.java) {
            override fun successResult(t: AddressBean) {
                mView.cancelWaiteStatue()
                mView.updateAddressList(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }
}
