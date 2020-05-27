package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.OrderStatueContract
import com.pjj.intent.RetrofitService
import com.pjj.module.*
import com.pjj.module.parameters.OrderStatue
import com.pjj.module.parameters.Template
import com.pjj.module.xspad.XspManage
import com.pjj.utils.CalculateUtils
import com.pjj.utils.TextUtils

/**
 * Created by XinHeng on 2018/12/07.
 * describe：订单P层
 */
class OrderStatuePresent(view: OrderStatueContract.View) : BasePresent<OrderStatueContract.View>(view, OrderStatueContract.View::class.java), OrderStatueContract.Present {
    override fun loadOrderStatueTask(orderStatue: OrderStatue) {
        retrofitService.loadOrderStatueTask(orderStatue, object : RetrofitService.CallbackClassResult<OrderResultBean>(OrderResultBean::class.java) {
            override fun successResult(t: OrderResultBean) {
                mView.uploadList(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.uploadList(null)
                mView.showNotice(error)
            }
        })
    }

    override fun loadIsPlayTask(orderId: String, screenId: String, zhiBoUrl: String?) {
        mView.showWaiteStatue()
        retrofitService.loadIsPlayTask(orderId, screenId, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                mView.cancelWaiteStatue()
                // 监播播放
                mView.play(zhiBoUrl)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    override fun loadAliPay(orderId: String) {
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

    override fun loadWeiXinPay(orderId: String) {
        mView.showWaiteStatue()
        retrofitService.loadWeiXinPayTask(orderId, object : RetrofitService.CallbackClassResult<CipherPayZhifubaoBean>(CipherPayZhifubaoBean::class.java) {
            override fun successResult(t: CipherPayZhifubaoBean) {
                //mView.payCipherSuccess()
                var data = t.data
                if (!TextUtils.isEmpty(data)) {
                    mView.payCipherSuccess(t.data, orderId)
                    mView.cancelWaiteStatue()
                } else {
                    mView.cancelWaiteStatue()
                    mView.showNotice("获取微信支付失败")
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()

            }
        })
    }

    override fun loadPayResultTask(orderId: String) {
        mView.showWaiteStatue()
        retrofitService.loadPayResultTask(orderId, object : RetrofitService.CallbackClassResult<MyPayResultBean>(MyPayResultBean::class.java) {
            override fun successResult(t: MyPayResultBean) {
                mView.payResult(true, "")
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.payResult(false, error)
            }
        })
    }

    //随机使用
    override fun loadNationalPrice(data: MutableList<BuildingBean.CommunityListBean>, b: Boolean) {
        retrofitService.getBasePrice(object : RetrofitService.CallbackClassResult<NationalPriceBean>(NationalPriceBean::class.java) {

            override fun successResult(nationalPriceBean: NationalPriceBean) {
                for (i in data.indices) {
                    val communityListBean = data[i]
                    val screenType = communityListBean.screenType
                    var v = -1.0
                    if (TextUtils.isNotEmptyList(screenType)) {
                        v = nationalPriceBean.getPrice(screenType.get(0)) * communityListBean.realDiscount
                    }
                    val price: Float
                    if (v >= 0) {
                        price = CalculateUtils.m1f(v.toFloat())
                        communityListBean.price = price
                    }
                }
                XspManage.getInstance().buildList = data
                mView.updateTemplateId(false)
            }
        })
    }

    private fun loadOrderInfTask_(orderId: String, type: String) {
        when (type) {
            "1", "2" -> {
                retrofitService.loadOrderInfTask(orderId, object : RetrofitService.CallbackClassResult<OrderInfBean>(OrderInfBean::class.java) {
                    override fun successResult(t: OrderInfBean) {
                        var orderInfo = t.orderInfo
                        XspManage.getInstance().templateId = when (type) {
                            "2" -> orderInfo.peopleInfoId
                            else -> t.templetList[0].templet_id
                        }
                        dealWithInf(t.orderScreenList)
                    }

                    override fun fail(error: String?) {
                        super.fail(error)
                        mView.cancelWaiteStatue()
                        mView.showNotice(error)
                    }
                })
            }
            "7", "9" -> {
                retrofitService.findNewMediaOrderDetail(orderId, object : RetrofitService.CallbackClassResult<MediaOrderInfBean>(MediaOrderInfBean::class.java) {
                    override fun successResult(t: MediaOrderInfBean) {
                        var templateId: String? = null
                        if (TextUtils.isNotEmptyList(t.templetList)) {
                            templateId = t.templetList!![0].templet_id
                        }
                        loadUserTemplate(type, templateId)
                    }

                    override fun fail(error: String?) {
                        super.fail(error)
                        mView.showNotice(error)
                    }
                })
            }
            else -> {
                retrofitService.loadOrderRandomInfTask(orderId, object : RetrofitService.CallbackClassResult<OrderInfBean>(OrderInfBean::class.java) {
                    override fun successResult(t: OrderInfBean) {
                        var data = t.data
                        var bmPingTag = false
                        XspManage.getInstance().templateId = when (type) {
                            "3" -> t.templetList[0].templet_id
                            else -> {
                                bmPingTag = true
                                t.orderData.peopleInfoId
                            }
                        }
                        if (bmPingTag) {
                            XspManage.getInstance().bianMinPing = 1
                        }
                        dealWithRandomInf(data)
                    }

                    override fun fail(error: String?) {
                        super.fail(error)
                        mView.cancelWaiteStatue()
                        mView.showNotice(error)
                    }
                })
            }
        }
    }

    private fun loadUserTemplate(type: String, templateId: String?) {
        val template = Template()
        template.purposeType = type
        template.userId = PjjApplication.application.userId
        template.identityType = XspManage.getInstance().identityType.toString()
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
                    mView.allowNext("-13", templateId, "")
                } else {
                    mView.allowNext("-14", templateId, "")
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        })
    }

    override fun loadOrderInfTask(orderId: String, type: String) {
        mView.showWaiteStatue()
        retrofitService.loadCertificationTask(PjjApplication.application.userId, object : RetrofitService.CallbackClassResult<VerificaBean>(VerificaBean::class.java) {
            override fun successResult(t: VerificaBean) {
                mView.cancelWaiteStatue()
                var identityType = XspManage.getInstance().identityType
                when (identityType) {
                    1 -> {//个人
                        if (t.userAuth == "3") {
                            loadOrderInfTask_(orderId, type)
                        } else {
                            mView.cancelWaiteStatue()
                            mView.certificationResult(t.userAuth, t.userAuthOpinion)
                        }
                    }
                    else -> {
                        if (t.userBusinessAuth == "3") {
                            loadOrderInfTask_(orderId, type)
                        } else {
                            mView.cancelWaiteStatue()
                            mView.certificationResult(t.userBusinessAuthOpinion, t.userBusinessAuthOpinion)
                        }
                    }
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }

    private fun dealWithInf(orderScreenList: MutableList<OrderInfBean.OrderScreenListBean>) {
        if (TextUtils.isNotEmptyList(orderScreenList)) {
            XspManage.getInstance().clearXspData()
            var groupLocation = XspManage.getInstance().groupLocation
            var childXsp = XspManage.getInstance().childXsp
            var map = HashMap<String, MutableList<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>>()
            orderScreenList.forEach { data ->
                var elevatorList = data.elevatorList
                if (TextUtils.isNotEmptyList(elevatorList)) {
                    elevatorList.forEach { item ->
                        var screenList = item.screenList
                        if (TextUtils.isNotEmptyList(screenList)) {
                            screenList.forEach {
                                var villageName = it.villageName
                                var list = map[villageName]
                                if (null == list) {
                                    list = ArrayList()
                                    map[villageName] = list
                                }
                                list.add(ElevatorBean.DataBean.ElevatorListBean.ScreenListBean().apply {
                                    screenId = it.screenId
                                    peoplePrice = it.peoplePrice.toFloat()
                                    price = it.price.toFloat()
                                    screenUrl = it.screenUrl
                                    screenType = it.screenType
                                    discount = it.discount.toFloat()
                                    screenName = it.screenName
                                    peopleDiscount = it.peopleDiscount.toFloat()
                                    screenCode = it.screenCode
                                })
                            }
                        }
                    }
                }
            }
            map.entries.forEach {
                groupLocation.add(it.key)
                childXsp.add(it.value)
            }
            mView.updateTemplateId(true)
        } else {
            mView.cancelWaiteStatue()
            mView.showNotice("无广告屏")
        }
    }

    private fun dealWithRandomInf(date: MutableList<OrderInfBean.DataBean>) {
        if (TextUtils.isNotEmptyList(date)) {
            var list = ArrayList<BuildingBean.CommunityListBean>()
            date.forEach { index ->
                var communityList = index.communityList
                var position1 = index.position
                if (TextUtils.isNotEmptyList(communityList)) {
                    communityList.forEach {
                        list.add(BuildingBean.CommunityListBean().apply {
                            imgName = it.imgName
                            elevatorNum = it.elevatorNum
                            blankDiscount = it.blankDiscount
                            blankPeopleDiscount = it.blankPeopleDiscount
                            //discount=it.dis
                            screenNum = it.screenNum
                            communityName = it.communityName
                            position = position1
                            communityId = it.communityId
                            screenType = ArrayList<String>(1).apply {
                                add("1")
                            }
                        })
                    }
                }
            }
            loadNationalPrice(list, false)
        } else {
            mView.cancelWaiteStatue()
            mView.showNotice("无广告屏")
        }
    }

    override fun cancelOrder(orderId: String, reason: String) {
        mView.showWaiteStatue()
        retrofitService.loadCancelOrderTask(PjjApplication.application.userId, orderId, reason, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                mView.cancelWaiteStatue()
                mView.cancelOrder(true, null)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.cancelOrder(false, error)
            }
        })
    }
}