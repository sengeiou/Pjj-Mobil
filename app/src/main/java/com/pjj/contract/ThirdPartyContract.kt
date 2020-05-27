package com.pjj.contract

/**
 * Created by XinHeng on 2019/03/12.
 * describe：
 */
interface ThirdPartyContract{
    interface View:BaseView
    interface Present{
        /**
         * 解绑
         */
        fun loadUnbind()

        /**
         * 获取第三方信息
         */
        fun loadGetThirdPartyInf()
    }
}