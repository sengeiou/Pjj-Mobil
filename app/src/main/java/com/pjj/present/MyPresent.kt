package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MyContract
import com.pjj.intent.AliFile
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.GoodsListBean
import com.pjj.module.MyIntegralBean
import com.pjj.module.UploadHeadImageBean
import com.pjj.module.VerificaBean

/**
 * Created by XinHeng on 2018/12/14.
 * describe：
 */
class MyPresent(view: MyContract.View) : BasePresent<MyContract.View>(view, MyContract.View::class.java), MyContract.Present {
    override fun loadVerificaTask() {
        mView.showWaiteStatue()
        retrofitService.loadCertificationTask(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<VerificaBean>(VerificaBean::class.java) {
            override fun successResult(t: VerificaBean) {
                mView.cancelWaiteStatue()
                mView.updateVerifiResult(t.userAuth, t.userBusinessAuth, "")
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun uploadHeadImage(path: String) {
        mView.showWaiteStatue()
        AliFile.getInstance().uploadFile(path, object : AliFile.UploadResult() {
            override fun success(result: String) {
                retrofitService.loadUpdateHead(result, PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<UploadHeadImageBean>(UploadHeadImageBean::class.java) {
                    override fun successResult(t: UploadHeadImageBean) {
                        mView.cancelWaiteStatue()
                        mView.updateHeadImg(t.headName)
                    }

                    override fun fail(error: String?) {
                        super.fail(error)
                        mView.cancelWaiteStatue()
                        mView.showNotice(error)
                    }
                })
            }

            override fun fail(error: String) {
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun loadMyIntegral() {
        if (null == PjjApplication.application.userId) {
            mView.updateMyIntegral("")
            return
        }
        IntegralRetrofitService.instance.getUserIntegralByUserId(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<MyIntegralBean>(MyIntegralBean::class.java) {
            override fun successResult(t: MyIntegralBean) {
                mView.updateMyIntegral(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }
}