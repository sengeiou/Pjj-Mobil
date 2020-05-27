package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.SelfUseContract
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean
import com.pjj.module.ScreenModelBean

class SelfUsePresent(view: SelfUseContract.View) : BasePresent<SelfUseContract.View>(view, SelfUseContract.View::class.java), SelfUseContract.Present {
    override fun loadSelfUseTemplate() {
        retrofitService.getOwnFile(PjjApplication.application.userId, "1", object : PresentCallBack<ScreenModelBean>(ScreenModelBean::class.java) {
            override fun successResult(t: ScreenModelBean) {
                mView.updateData(t.data)
            }
        })
    }

    override fun deleteTemplate(id: String) {
        mView.showWaiteStatue()
        retrofitService.deleteOwnFile(id, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.deleteSuccess(true)
            }
        })
    }

    override fun changeTemplateName(templateId: String, templateName: String) {
        mView.showWaiteStatue()
        retrofitService.loadUploadTemplateNameTask(templateId, templateName, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                mView.cancelWaiteStatue()
                mView.updateNameSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }
}