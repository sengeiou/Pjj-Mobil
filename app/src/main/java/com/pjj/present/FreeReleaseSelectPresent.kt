package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.FreeReleaseSelectContract
import com.pjj.module.VerificaBean

class FreeReleaseSelectPresent(view: FreeReleaseSelectContract.View) : BasePresent<FreeReleaseSelectContract.View>(view, FreeReleaseSelectContract.View::class.java), FreeReleaseSelectContract.Present {
    override fun loadRenZhengTask() {
        mView.showWaiteStatue()
        retrofitService.loadCertificationTask(PjjApplication.application.userId, object : PresentCallBack<VerificaBean>(VerificaBean::class.java) {
            override fun successResult(t: VerificaBean) {
                mView.cancelWaiteStatue()
                mView.result(t)
            }
        })
    }

}