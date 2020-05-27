package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.ShowSpeedTemplateContract
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean
import com.pjj.module.SpeedSimpleBean

/**
 * Create by xinheng on 2019/03/20 11:47。
 * describe：P层
 */
class ShowSpeedTemplatePresent(view: ShowSpeedTemplateContract.View) : BasePresent<ShowSpeedTemplateContract.View>(view, ShowSpeedTemplateContract.View::class.java), ShowSpeedTemplateContract.Present {
    override fun loadSpeedSimpleTask(tag: String) {
        mView.showWaiteStatue()
        retrofitService.getUserSpellTempleByIdentificationId(PjjApplication.application.userId, tag, object : RetrofitService.CallbackClassResult<SpeedSimpleBean>(SpeedSimpleBean::class.java) {
            override fun successResult(t: SpeedSimpleBean) {
                mView.cancelWaiteStatue()
                mView.updateData(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun delete(id: String, bianMin: Boolean) {
        var callBack = object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.deleteSuccess()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        }
        call = when {
            bianMin -> retrofitService.deleteBianMin(id, callBack)
            else -> retrofitService.deleteDIY(id, callBack)
        }
    }

    override fun loadSpeedTemplateTask() {
        /*mView.showWaiteStatue()
        retrofitService.getSpellTempleByUserId(PjjApplication.application.userId,object :RetrofitService.CallbackClassResult<SpeedBean>(SpeedBean::class.java){
            override fun successResult(t: SpeedBean) {
                mView.cancelWaiteStatue()
                mView.updateData(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })*/
    }
}
