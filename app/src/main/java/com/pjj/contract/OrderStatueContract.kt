package com.pjj.contract

import com.pjj.module.BuildingBean
import com.pjj.module.OrderResultBean
import com.pjj.module.parameters.OrderStatue

/**
 * Created by XinHeng on 2018/12/07.
 * describe：
 */
interface OrderStatueContract {
    interface View : BaseView {
        /**
         * 更新
         */
        fun uploadList(list: MutableList<OrderResultBean.DataBean>?)

        /**
         * 不可监播
         */
        fun noPlay()

        /**
         * 可监播
         */
        fun play(path: String?)

        /**
         * 获取成功，支付信息密文
         */
        fun payCipherSuccess(orderInfo: String, orderId: String)

        fun payResult(result: Boolean, msg: String?)
        fun updateTemplateId(tag: Boolean)
        /**
         * 取消订单结果
         */
        fun cancelOrder(tag: Boolean, msg: String?)

        fun certificationResult(tag: String,msg:String?)
        fun allowNext(code:String,msg1:String?,msg2:String)
    }

    interface Present {
        /**
         * 加载订单状态数据
         */
        fun loadOrderStatueTask(orderStatue: OrderStatue)

        /**
         * 是否可监播
         */
        fun loadIsPlayTask(orderId: String, screenId: String, zhiBoUrl: String?)

        /**
         * 支付密文
         */
        fun loadPayResultTask(orderId: String)

        /**
         * 阿里支付
         */
        fun loadAliPay(orderId: String)

        /**
         * 微信支付
         */
        fun loadWeiXinPay(orderId: String)

        /**
         * 全国价格
         */
        fun loadNationalPrice(data: MutableList<BuildingBean.CommunityListBean>, b: Boolean)

        /**
         * 订单详情
         */
        fun loadOrderInfTask(orderId: String, type: String)

        /**
         * 取消订单
         */
        fun cancelOrder(orderId: String, reason: String)
    }
}