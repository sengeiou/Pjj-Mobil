package com.pjj.contract

/**
 * Create by xinheng on 2019/01/19 17:16。
 * describe：
 */
interface OpinionContract {
    interface View : BaseView{
        fun loadSuccess();
    }

    interface Present{
        fun loadOpinionTask(msg:String)
    }
}