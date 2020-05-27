package com.pjj.contract

/**
 * Created by XinHeng on 2018/12/14.
 * describe：
 */
interface MyContract {
    interface View : BaseView {
        /**
         * 认证结果
         * @param tag
         * @param msg 失败原因
         */
        fun updateVerifiResult(tag: String,tab_b:String,msg:String?)

        /**
         * 更新头像
         * @param path
         */
        fun updateHeadImg(path: String)
        fun updateMyIntegral(s:String)
    }

    interface Present {
        fun loadVerificaTask()
        fun uploadHeadImage(path: String)
        fun loadMyIntegral()
    }
}
