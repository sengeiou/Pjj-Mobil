package com.pjj.contract

import com.pjj.module.ScreenModelBean

interface SelfUseContract {
    interface View : BaseView {
        fun updateData(list: MutableList<ScreenModelBean.DataBean>?)
        fun deleteSuccess(tag: Boolean)
        fun updateNameSuccess()
    }

    interface Present {
        fun loadSelfUseTemplate()
        fun deleteTemplate(id: String)
        fun changeTemplateName(templateId: String, templateName: String)
    }
}