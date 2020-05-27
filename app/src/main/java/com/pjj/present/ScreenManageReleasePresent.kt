package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.ScreenManageReleaseContract
import com.pjj.module.ResultBean
import com.pjj.module.ScreenModelBean
import com.pjj.module.parameters.ReleaseScreenModel

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：P层
 */
class ScreenManageReleasePresent(view: ScreenManageReleaseContract.View) : BasePresent<ScreenManageReleaseContract.View>(view, ScreenManageReleaseContract.View::class.java), ScreenManageReleaseContract.Present {
    override fun loadAddManageModelTask(userId: String, materialName: String, fileName: String, fileType: String, sourceType: String) {
        retrofitService.inserOwnFile(userId, materialName, fileName, fileType, sourceType, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.deleteOrAddSuccess(false)
            }
        })
    }

    override fun loadManageModelTask(sourceType: String) {
        retrofitService.getOwnFile(PjjApplication.application.userId, sourceType, object : PresentCallBack<ScreenModelBean>(ScreenModelBean::class.java) {
            override fun successResult(t: ScreenModelBean) {
                mView.update(t.data)
            }
        })
    }

    override fun loadPushManageModelTask(bean: ReleaseScreenModel) {
        mView.showWaiteStatue()
        retrofitService.generateOwnManageOrder(bean, object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.pushSuccess()
            }
        })
    }

    override fun loadDeleteModel(id: String) {
        mView.showWaiteStatue()
        retrofitService.deleteOwnFile(id,object : PresentCallBack<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                mView.deleteOrAddSuccess(true)
            }
        })
    }
}
