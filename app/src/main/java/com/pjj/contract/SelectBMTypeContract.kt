package com.pjj.contract

import com.pjj.module.BmTypeBean

/**
 * Created by XinHeng on 2019/01/18.
 * describe：
 */
interface SelectBMTypeContract {
    interface View:BaseView{
        fun updateList(data: MutableList<BmTypeBean.DataBean>)
    }
    interface Present{
        fun loadBmTypeTask()
    }
}