package com.pjj.contract

import com.pjj.module.BianMinBean
import com.pjj.module.UserTempletBean

/**
 * Create by xinheng on 2018/11/29 13:44。
 * describe：
 */
interface SelectTemplateContract {
    interface View : BaseView {
        /**
         * 更新模板列表
         */
        fun updateTemplateListView(dataList: List<UserTempletBean.DataBean>)

        /**
         * 便民模板
         */
        fun updateBMTemplateListView(dataList: List<BianMinBean.DataBean>)

        fun updateMakeOrderSuccess(orderId: String)
        fun updateMakeOrderFail(error: String?)
        fun isShowPhone(): String
        fun isShowName(): String
        fun getPlayDate(): String
        fun getPlayTime(): String
        fun getCommunityIds(): String
        /**
         * 获取成功，支付信息密文
         */
        fun payCipherSuccess(orderInfo: String,orderId:String)
        fun payResult(result:Boolean,msg:String?)
    }

    interface Present {
        /**
         * 加载个人模板数据
         */
        fun loadUserTemplateListTask()

        /**
         * 生成订单
         */
        fun loadMakeOrderTask()

        /**
         * 支付密文
         */
        fun loadAliPayTask(orderId: String)
        fun loadWeiXinPayTask(orderId: String)
        /**
         * 支付结果
         */
        fun loadPayResult(orderId: String)
    }
}