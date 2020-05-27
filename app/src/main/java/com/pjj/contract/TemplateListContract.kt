package com.pjj.contract

import com.pjj.module.BianMinBean
import com.pjj.module.UserTempletBean
import com.pjj.module.parameters.Template

/**
 * Create by xinheng on 2018/12/06 13:41。
 * describe：
 */
interface TemplateListContract {
    interface View : BaseView {
        fun updateTemplate(list: MutableList<UserTempletBean.DataBean>)
        fun updateOtherTemplate(list: MutableList<UserTempletBean.DataBean>?)
        fun updateBMTemplateListView(data: MutableList<BianMinBean.DataBean>)
        fun deleteSuccess()
        fun updateNameSuccess()
    }

    interface Present {
        fun loadTemplateListTask(template: Template)
        fun loadOtherTemplateListTask(template: Template)
        fun delete(id:String,bianMin:Boolean)
        fun changeTemplateName(templateId:String,templateName:String)
    }
}