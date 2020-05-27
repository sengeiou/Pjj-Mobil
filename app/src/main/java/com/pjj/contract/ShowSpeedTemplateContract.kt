package com.pjj.contract

import com.pjj.module.SpeedBean
import com.pjj.module.SpeedDataBean
import com.pjj.module.SpeedSimpleBean

/**
 * Create by xinheng on 2019/03/20 11:47。
 * describe：
 */
interface ShowSpeedTemplateContract {
    interface View : BaseView {
        fun updateData(dateBean: MutableList<SpeedDataBean>)
        fun deleteSuccess()
    }

    interface Present {
        fun loadSpeedTemplateTask()
        fun delete(id: String, bianMin: Boolean)
        fun loadSpeedSimpleTask(tag: String)
    }
}