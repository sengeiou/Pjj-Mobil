package com.pjj.contract

/**
 * Create by xinheng on 2019/04/27 10:05。
 * describe：
 */
interface ReleaseRuleContract {
    interface View : BaseView{
        fun allowNext(code:String,msg1:String,msg2:String)
    }

    interface Present{
        fun certificationUser(oneTag: Boolean)
    }
}