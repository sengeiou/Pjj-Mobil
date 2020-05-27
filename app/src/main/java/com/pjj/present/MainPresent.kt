package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.MainContract
import com.pjj.intent.RetrofitService
import com.pjj.module.*
import com.pjj.module.parameters.Template
import com.pjj.module.xspad.XspManage
import com.pjj.utils.FileUtils
import com.pjj.utils.TextUtils

/**
 * Created by XinHeng on 2018/12/15.
 * describe：
 */
class MainPresent(view: MainContract.View) : MainContract.Present, BasePresent<MainContract.View>(view, MainContract.View::class.java) {

    override fun certificationUser(oneTag: Boolean) {
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

    override fun getTemplateList() {
        var template = Template()
        template.userId = PjjApplication.application.userId
        var type = XspManage.getInstance().adType
        //template.purposeType = type.toString()
        when (type) {
            1 -> {
                template.purposeType = "1"
                template.identityType = XspManage.getInstance().identityType.toString()
            }
            2 -> {
                template.purposeType = "1"
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
            7, 9 -> {
                template.purposeType = type.toString()
                template.identityType = XspManage.getInstance().identityType.toString()
                //mView.allowNext("-13", "", "")
                loadMediaTask(template)
                return
            }
        }
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

    override fun loadAppVersionTask() {
        retrofitService.loadAppVersionTask(object : RetrofitService.CallbackClassResult<AppUpdateBean>(AppUpdateBean::class.java) {
            override fun successResult(t: AppUpdateBean) {
                mView.appVersionResult(t)
            }
        })
    }

    override fun loadDownloadAppTask(url: String) {
        mView.showWaiteStatue()
        retrofitService.downloadFile(url, PjjApplication.App_Path + "file/", object : FileUtils.OnDownloadListener {
            override fun success(filePath: String) {
                mView.cancelWaiteStatue()
                mView.installApk(filePath)
            }

            override fun fail() {
                mView.cancelWaiteStatue()
                mView.showNotice("下载失败")
            }

        })
    }

    private fun loadSpeedTemplatesTask() {
        retrofitService.getSpellTempleByUserId(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<SpeedBean>(SpeedBean::class.java) {
            override fun successResult(t: SpeedBean) {
                mView.cancelWaiteStatue()
                if (t.data.isNotEmpty) {
                    mView.allowNext("-11", "", "")
                } else {
                    mView.allowNext("-12", "", "")
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun loadMediaTask(template: Template) {
        //mView.allowNext("-13", "", "") -14
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
        })
    }
}