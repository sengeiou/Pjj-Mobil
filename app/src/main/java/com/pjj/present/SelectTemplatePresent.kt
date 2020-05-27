package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.SelectTemplateContract
import com.pjj.intent.RetrofitService
import com.pjj.module.*
import com.pjj.module.parameters.MakeOrder
import com.pjj.module.parameters.Template
import com.pjj.module.xspad.XspManage
import com.pjj.utils.JsonUtils

/**
 * Create by xinheng on 2018/11/29 13:44。
 * describe：P层
 */
class SelectTemplatePresent(view: SelectTemplateContract.View) : BasePresent<SelectTemplateContract.View>(view, SelectTemplateContract.View::class.java), SelectTemplateContract.Present {
    /**
     * 加载个人模板数据
     */
    override fun loadUserTemplateListTask() {
        var userId = PjjApplication.application.userId
        var type = XspManage.getInstance().adType
        var purposeType = when (type) {
            1 -> {
                "1"
            }
            2 -> {
                "1"
            }
            3 -> {
                "3"
            }
            else -> {
                "2"
            }
        }
        if (type != 2 && type != 4) {
            var template = Template(userId, XspManage.getInstance().identityType.toString(), purposeType)
            retrofitService.loadUserTempletListTask(template, object : RetrofitService.CallbackClassResult<UserTempletBean>(UserTempletBean::class.java) {
                override fun successResult(t: UserTempletBean) {
                    mView.updateTemplateListView(t.data)
                }

                override fun fail(error: String?) {
                    super.fail(error)
                    mView.showNotice(error)
                }
            })
        } else {
            var template = Template()
            template.userId = userId
            template.infoType = XspManage.getInstance().identityType.toString()
            template.purposeType = purposeType
            retrofitService.loadBianMinListTask(template, object : RetrofitService.CallbackClassResult<BianMinBean>(BianMinBean::class.java) {
                override fun successResult(t: BianMinBean) {
                    mView.updateBMTemplateListView(t.data)
                }
            })
        }
    }

    override fun loadMakeOrderTask() {
        mView.showWaiteStatue()
        var type = XspManage.getInstance().adType
        //type=3
        var makeOrder = MakeOrder()
        var callBack = object : RetrofitService.CallbackClassResult<MakeOrderBean>(MakeOrderBean::class.java) {
            override fun successResult(t: MakeOrderBean) {
                //mView.cancelWaiteStatue()
                mView.updateMakeOrderSuccess(t.orderId)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.updateMakeOrderFail(error)
            }
        }
        makeOrder.run {
            authType = XspManage.getInstance().identityType.toString()
            playType = XspManage.getInstance().playType
            userId = PjjApplication.application.userId
            orderType = type.toString()
            playDate = mView.getPlayDate().replace("-", ".")
            playTime = XspManage.getInstance().releaseTime_
            isShowPhone = mView.isShowPhone()
            isShowName = mView.isShowName()
        }
        if (type == 1 || type == 2) {
            makeOrder.screen_time = JsonUtils.toJsonString(XspManage.getInstance().xspHashMap)
            if (type == 2) {
                makeOrder.peopleInfoId = XspManage.getInstance().orderContent
            } else {
                makeOrder.templetIds = XspManage.getInstance().orderContent
            }
            retrofitService.loadMakeOrderTaskDiyAndBm(makeOrder, callBack)
        } else {//填空，随机
            makeOrder.communityId = mView.getCommunityIds()
            makeOrder.startTime = mView.getPlayDate() + " " + mView.getPlayTime()
            makeOrder.playlongtime = XspManage.getInstance().releaseTime
            makeOrder.dayplaytime = XspManage.getInstance().releaseTime
            makeOrder.playDate += (" " + mView.getPlayTime() + ":00")
            if (type == 3) {//diy随机
                makeOrder.templetIds = XspManage.getInstance().orderContent
            } else if (type == 5) {
                makeOrder.pieceType = "1"
                var data = XspManage.getInstance().speedScreenData
                makeOrder.pieceNum = data.pieceNum.toString()
                makeOrder.file_id = data.file_id
                makeOrder.identificationId = data.identificationId
                retrofitService.generateSplicingTemplate(makeOrder, callBack)
                return
            } else {//便民随机
                makeOrder.peopleInfoId = XspManage.getInstance().orderContent
                if (XspManage.getInstance().bianMinPing == 1) {
                    makeOrder.pieceType = "1"
                    makeOrder.pieceNum = "10"
                    makeOrder.pieceColour = XspManage.getInstance().tagColorPing.toString()
                    makeOrder.pieceTitle = XspManage.getInstance().nameTypePing
                }
            }
            retrofitService.loadMakeOrderTaskTianKong(makeOrder, callBack)
        }
    }

    override fun loadAliPayTask(orderId: String) {
        mView.showWaiteStatue()
        retrofitService.loadAliPayTask(orderId, object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(t: CipherPayZhifubaoBean) {
                //mView.payCipherSuccess()
                mView.payCipherSuccess(t.data, orderId)
                mView.cancelWaiteStatue()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun loadWeiXinPayTask(orderId: String) {
        mView.showWaiteStatue()
        retrofitService.loadWeiXinPayTask(orderId, object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(t: CipherPayZhifubaoBean) {
                mView.payCipherSuccess(t.data, orderId)
                mView.cancelWaiteStatue()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun loadPayResult(orderId: String) {
        retrofitService.loadPayResultTask(orderId, object : RetrofitService.CallbackClassResult<MyPayResultBean>(MyPayResultBean::class.java) {
            override fun successResult(t: MyPayResultBean) {
                mView.payResult(true, "")
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.payResult(false, error)
            }
        })
    }
}
