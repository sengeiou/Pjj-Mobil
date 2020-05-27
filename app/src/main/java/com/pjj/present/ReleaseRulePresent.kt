package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.ReleaseRuleContract
import com.pjj.intent.RetrofitService
import com.pjj.module.BianMinBean
import com.pjj.module.UserTempletBean
import com.pjj.module.VerificaBean
import com.pjj.module.parameters.Template
import com.pjj.module.xspad.XspManage
import com.pjj.utils.Log

/**
 * Create by xinheng on 2019/04/27 10:05。
 * describe：P层
 */
class ReleaseRulePresent(view: ReleaseRuleContract.View) : BasePresent<ReleaseRuleContract.View>(view, ReleaseRuleContract.View::class.java), ReleaseRuleContract.Present {

    override fun certificationUser(oneTag: Boolean) {
        if (XspManage.getInstance().identityType != 2 && (XspManage.getInstance().adType == 9 || XspManage.getInstance().adType == 7) && !XspManage.getInstance().newMediaData.releaseTag) {
            mView.allowNext("-13", "", "")
            return
        }
        val shangjiaMediaTag = (XspManage.getInstance().identityType == 2 && (XspManage.getInstance().adType == 9 || XspManage.getInstance().adType == 7) && !XspManage.getInstance().newMediaData.releaseTag)
        retrofitService.loadCertificationTask(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<VerificaBean>(VerificaBean::class.java) {
            override fun successResult(t: VerificaBean) {
                var opinion: String
                if (oneTag) {
                    if (t.userAuth == "3" || t.userBusinessAuth == "3") {
                        mView.allowNext("-77", "", "")
                    } else {
                        mView.cancelWaiteStatue()
                        mView.allowNext(t.userAuth, t.userBusinessAuth, t.userAuthOpinion)
                    }
                    return
                }
                if (shangjiaMediaTag) {
                    if (t.userBusinessAuth == "3") {
                        mView.allowNext("-13", "", "")
                    } else {
                        mView.allowNext(t.userAuth, t.userBusinessAuth, t.userAuthOpinion)
                    }
                    return
                }
                var isAuth: String = when (XspManage.getInstance().identityType) {
                    1 -> {//个人
                        opinion = t.userAuthOpinion
                        t.userAuth
                    }
                    3 -> {
                        if (t.userAuth == "3" || t.userBusinessAuth == "3") {
                            mView.allowNext("-11", "", "")
                        } else {
                            mView.cancelWaiteStatue()
                            mView.allowNext(t.userAuth, t.userBusinessAuth, t.userAuthOpinion)
                        }
                        return
                    }
                    else -> {//商家
                        opinion = t.userBusinessAuthOpinion
                        t.userBusinessAuth
                    }
                }
                if ("3" == isAuth) {
                    getTemplateList()
                    //mView.allowAdDialog()
                } else {
                    mView.cancelWaiteStatue()
                    mView.allowNext(t.userAuth, t.userBusinessAuth, opinion)
                }
                //PjjApplication.application.renZheng = isAuth
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    private fun getTemplateList() {
        var template = Template()
        template.userId = PjjApplication.application.userId
        var type = XspManage.getInstance().adType//1 DIY类型 2便民信息
        //template.purposeType = type.toString()
        when (type) {
            1 -> {
                template.purposeType = "1"
                template.identityType = XspManage.getInstance().identityType.toString()
            }
            2 -> {
                template.purposeType = "1"//1 DIY类型 2便民信息 3填空传媒 //1
                template.infoType = XspManage.getInstance().identityType.toString()
            }
            3 -> {
                template.purposeType = "3"
                template.identityType = XspManage.getInstance().identityType.toString()
            }
            4 -> {
                template.purposeType = "2"
                template.infoType = XspManage.getInstance().identityType.toString()
            }
            //传统传媒
            7 -> {
                template.purposeType = "7"
                template.identityType = XspManage.getInstance().identityType.toString()
                //mView.allowNext("-13", "", "")
                loadMediaTask(template)
                return
            }
            9 -> {//传统信息
                template.purposeType = "9"
                template.identityType = XspManage.getInstance().identityType.toString()
                loadMediaTask(template)
                return
            }
        }
        Log.e("TAG", "template=$template")
        if (template.infoType == null) {
            call = retrofitService.loadUserTempletListTask(template, object : RetrofitService.CallbackClassResult<UserTempletBean>(UserTempletBean::class.java) {
                override fun successResult(t: UserTempletBean) {
                    mView.cancelWaiteStatue()
                    if (t.data.size > 0) {
                        mView.allowNext("-11", "", "")
                    } else {
                        mView.allowNext("-10", "", "")
                    }
                }

                override fun fail(error: String?) {
                    super.fail(error)
                    mView.cancelWaiteStatue()
                    mView.showNotice(error)
                }
            })
        } else {//便民
            template.identityType = null
            call = retrofitService.loadBianMinListTask(template, object : RetrofitService.CallbackClassResult<BianMinBean>(BianMinBean::class.java) {
                override fun successResult(t: BianMinBean) {
                    mView.cancelWaiteStatue()
                    if (t.data.size > 0) {
                        mView.allowNext("-11", "", "")
                    } else {
                        mView.allowNext("-10", "", "")
                    }
                }

                override fun fail(error: String?) {
                    super.fail(error)
                    mView.cancelWaiteStatue()
                    mView.showNotice(error)
                }
            })
        }
    }

    private fun loadMediaTask(template: Template) {
        mView.allowNext("-13", "", "") //-14
        /*
        retrofitService.getNewMediaTempletList(template, object : RetrofitService.CallbackClassResult<UserTempletBean>(UserTempletBean::class.java) {
            override fun successResult(t: UserTempletBean) {
                var pass = false
                t.data?.forEach {
                    if ("1" == it.status) {
                        pass = true
                        return@forEach
                    }
                }
                if (pass) {
                    mView.allowNext("-13", "", "")
                } else {
                    mView.allowNext("-14", "", "")
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })*/
    }
}
