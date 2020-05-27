package com.pjj.contract

import com.pjj.module.MyTuiGuangBean

/**
 * Create by xinheng on 2019/07/26 15:20。
 * describe：
 */
interface MyTuiGuangContract {
    interface View : TuiGuangContract.View {
        fun updateData(list: MutableList<MyTuiGuangBean.DataBean>)
        fun cancelTopSuccess()
    }

    interface Present {
        fun loadDataTask(pageNo: Int, pageNum: Int)
        fun loadCancelTopTask(topOrderId: String)
    }
}