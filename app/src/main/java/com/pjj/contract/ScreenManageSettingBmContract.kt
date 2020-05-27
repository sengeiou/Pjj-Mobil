package com.pjj.contract

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：
 */
interface ScreenManageSettingBmContract {
    interface View : BaseView {
        fun setSuccess()
    }

    interface Present {
        fun setBm(propertyTitle:String,propertyInfo: String,screenId :String,isShow:String)
    }
}