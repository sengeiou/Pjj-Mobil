package com.pjj.present

import com.pjj.contract.OrderInfContract
import com.pjj.intent.RetrofitService
import com.pjj.module.OrderInfBean
import com.pjj.module.SpeedDataBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.DateUtils
import java.util.*

/**
 * Create by xinheng on 2018/12/08 11:43。
 * describe：P层
 */
class OrderInfPresent(view: OrderInfContract.View) : BasePresent<OrderInfContract.View>(view, OrderInfContract.View::class.java), OrderInfContract.Present {

    override fun loadOrderInfTask(orderId: String, type: String) {
        when (type) {
            "1", "2" -> {
                retrofitService.loadOrderInfTask(orderId, object : RetrofitService.CallbackClassResult<OrderInfBean>(OrderInfBean::class.java) {
                    override fun successResult(t: OrderInfBean) {
                        dealWith(t, type)
                    }

                    override fun fail(error: String?) {
                        super.fail(error)
                        mView.showNotice(error)
                    }
                })
            }
            "7" -> {
                retrofitService.findNewMediaOrderDetail(orderId, object : RetrofitService.CallbackClassResult<OrderInfBean>(OrderInfBean::class.java) {
                    override fun successResult(t: OrderInfBean) {
                        dealWith(t, type)
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
                        dealWith(t, type)
                    }

                    override fun fail(error: String?) {
                        super.fail(error)
                        mView.showNotice(error)
                    }
                })
            }
        }
    }

    private fun dealWith(t: OrderInfBean, type: String) {
        var orderInfo: OrderInfBean.OrderInfoBean
        if (type != "4") {
            mView.updatePingInf(null, "")
        }
        when (type) {
            "2" -> {
                orderInfo = t.orderInfo
                mView.updateOrderInf(orderInfo.orderType, orderInfo.peopleInfo, orderInfo.peopleInfoTitle, orderInfo.isShowPhone == "1", orderInfo.isShowName == "1", DateUtils.getSfs1(Date(orderInfo.createTime)), orderInfo.orderId, "2", orderInfo.amount, "0")
                mView.updateOrderElevatorList(t.orderElevatorBeanList)
                mView.updateHours(t.hours)
            }
            "1" -> {
                orderInfo = t.orderInfo
                var templet = t.templetList[0]
                mView.updateOrderInf(orderInfo.orderType, templet.fileList[0].fileUrl, templet.templetName, orderInfo.isShowPhone == "1", orderInfo.isShowName == "1", DateUtils.getSfs1(Date(orderInfo.createTime)), orderInfo.orderId, "2", orderInfo.amount, templet.fileList[0].type)
                mView.updateOrderElevatorList(t.orderElevatorBeanList)
                mView.updateHours(t.hours)
            }
            "3" -> {
                orderInfo = t.orderData
                var templet = t.templetList[0]
                mView.updateOrderInf(orderInfo.orderType, templet.fileList[0].fileUrl, templet.templetName, orderInfo.isShowPhone == "1", orderInfo.isShowName == "1", DateUtils.getSfs1(Date(orderInfo.createTime)), orderInfo.orderId, "2", orderInfo.amount, templet.fileList[0].type)
                mView.updateOrderElevatorList(t.orderElevatorBeanRandomList)
                var hours = 0
                try {
                    hours = orderInfo.playlongtime.toInt()
                } catch (e: Exception) {
                }
                mView.updateHours(hours)
            }
            "4" -> {
                orderInfo = t.orderData
                mView.updateOrderInf(orderInfo.orderType, orderInfo.peopleInfo, orderInfo.peopleInfoTitle, orderInfo.isShowPhone == "1", orderInfo.isShowName == "1", DateUtils.getSfs1(Date(orderInfo.createTime)), orderInfo.orderId, "2", orderInfo.amount, "0")
                if ("1" == orderInfo.pieceType) {
                    mView.updatePingInf(orderInfo.pieceTitle, orderInfo.pieceColour)
                } else {
                    mView.updatePingInf(null, "")
                }
                mView.updateOrderElevatorList(t.orderElevatorBeanRandomList)
                var hours = 0
                try {
                    hours = orderInfo.playlongtime.toInt()
                } catch (e: Exception) {
                }
                mView.updateHours(hours)
            }
            "5" -> {
                orderInfo = t.orderData
                XspManage.getInstance().speedScreenData.clone = t.speliPicture
                mView.updateOrderInf(orderInfo.orderType, "", "", orderInfo.isShowPhone == "1", orderInfo.isShowName == "1", DateUtils.getSfs1(Date(orderInfo.createTime)), orderInfo.orderId, "2", orderInfo.amount, null)
                mView.updateOrderElevatorList(t.orderElevatorBeanRandomList)
                var hours = 0
                try {
                    hours = orderInfo.playlongtime.toInt()
                } catch (e: Exception) {
                }
                mView.updateHours(hours)
            }
            "7" -> {
                orderInfo = t.orderInfo
                var templet = t.templetList[0]
                var templetType = templet.templetType
                var speedDataBean: SpeedDataBean? = null
                if ("3" == templetType) {
                    speedDataBean = templet.speedDataBean
                }
                mView.updateOrderInf(orderInfo.orderType, templet.fileList[0].fileUrl, templet.templetName, orderInfo.isShowPhone == "1", orderInfo.isShowName == "1", DateUtils.getSfs1(Date(orderInfo.createTime)), orderInfo.orderId, "2", orderInfo.amount, templet.fileList[0].type, speedDataBean)
                mView.updateOrderElevatorList(t.orderElevatorBeanList)

                mView.updateHours(-1)
            }
        }

    }
}
