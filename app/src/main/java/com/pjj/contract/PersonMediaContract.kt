package com.pjj.contract

import com.pjj.module.ModuleMoreBean

interface PersonMediaContract {
    interface View : BaseView {
        fun update(list: MutableList<ModuleMoreBean.DataBean>)
    }

    interface Present {
        fun loadModuleTask(purposeType: String)
        fun loadSelfUseTask()
    }
}