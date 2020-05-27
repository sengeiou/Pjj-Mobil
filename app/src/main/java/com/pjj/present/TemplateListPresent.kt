package com.pjj.present

import com.pjj.contract.TemplateListContract
import com.pjj.intent.RetrofitService
import com.pjj.module.BianMinBean
import com.pjj.module.ResultBean
import com.pjj.module.UserTempletBean
import com.pjj.module.parameters.Template
import com.pjj.utils.TextUtils

/**
 * Create by xinheng on 2018/12/06 13:41。
 * describe：P层
 */
class TemplateListPresent(view: TemplateListContract.View) : BasePresent<TemplateListContract.View>(view, TemplateListContract.View::class.java), TemplateListContract.Present {
    override fun loadOtherTemplateListTask(template: Template) {
        //val list=ArrayList<UserTempletBean.DataBean>()
        if (template.purposeType == "7") {
            template.purposeType = "9"
        } else {
            template.purposeType = "7"
        }
        if (template.purposeType == "7") {
            call = retrofitService.getNewMediaTempletList(template, object : RetrofitService.CallbackClassResult<UserTempletBean>(UserTempletBean::class.java) {
                override fun successResult(t: UserTempletBean) {
                    mView.updateOtherTemplate(t.data)
                    return
                }

                override fun fail(error: String?) {
                    super.fail(error)
                    //mView.showNotice(error)
                    mView.updateOtherTemplate(null)
                }
            })
            return
        }
        call = retrofitService.loadUserTempletListTask(template, object : RetrofitService.CallbackClassResult<UserTempletBean>(UserTempletBean::class.java) {
            override fun successResult(t: UserTempletBean) {
                mView.updateOtherTemplate(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                //mView.showNotice(error)
                mView.updateOtherTemplate(null)
            }
        })
    }

    override fun loadTemplateListTask(template1: Template) {
        val template = template1.clone()
        if (template.purposeType == "7") {
            call = retrofitService.getNewMediaTempletList(template, object : RetrofitService.CallbackClassResult<UserTempletBean>(UserTempletBean::class.java) {
                override fun successResult(t: UserTempletBean) {
                    mView.updateTemplate(t.data)
                }

                override fun fail(error: String?) {
                    super.fail(error)
                    mView.showNotice(error)
                }
            })
            return
        }
        if (template.infoType == null) {
            call = retrofitService.loadUserTempletListTask(template, object : RetrofitService.CallbackClassResult<UserTempletBean>(UserTempletBean::class.java) {
                override fun successResult(t: UserTempletBean) {
                    mView.updateTemplate(t.data)
                }

                override fun fail(error: String?) {
                    super.fail(error)
                    mView.showNotice(error)
                }
            })
        } else {//便民
            template.identityType = null
            call = retrofitService.loadBianMinListTask(template, object : RetrofitService.CallbackClassResult<BianMinBean>(BianMinBean::class.java) {
                override fun successResult(t: BianMinBean) {
                    mView.updateBMTemplateListView(t.data)
                }

                override fun fail(error: String?) {
                    super.fail(error)
                    mView.showNotice(error)
                }
            })
        }
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
