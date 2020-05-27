package com.pjj.contract

import com.pjj.module.ScreenModelBean
import com.pjj.module.parameters.ReleaseScreenModel

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：
 */
interface ScreenManageReleaseContract {
    interface View : BaseView {
        fun update(list: MutableList<ScreenModelBean.DataBean>?)
        fun deleteOrAddSuccess(deleteTag:Boolean)
        fun pushSuccess()
    }

    interface Present {
        fun loadDeleteModel(id: String)
        fun loadManageModelTask(sourceType: String)
        fun loadPushManageModelTask(bean: ReleaseScreenModel)
        fun loadAddManageModelTask(userId: String, materialName: String, fileName: String, fileType: String, sourceType: String)
    }
}