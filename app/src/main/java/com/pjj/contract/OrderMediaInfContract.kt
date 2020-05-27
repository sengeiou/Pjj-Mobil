package com.pjj.contract

import com.pjj.module.MediaOrderInfBean

/**
 * Created by XinHeng on 2019/04/19.
 * describe：
 */
interface OrderMediaInfContract {
    interface View : BaseView {
        fun updateListView(data: MediaOrderInfBean)
        fun certificationResult(tag: String,msg:String?)
        fun allowNext(code:String,msg1:String?,msg2:String)
        fun updateTemplateId(tag: Boolean)
    }

    interface Present {
        fun loadMediaOrderInfTask(orderId: String)
        fun loadOrderInfTask(orderId: String, type: String)
    }
}