package com.pjj.contract

import com.pjj.module.AppUpdateBean
import com.pjj.module.parameters.Template

/**
 * Created by XinHeng on 2018/12/15.
 * describe：
 */
interface MainContract {
    interface View : BaseView {
        /**
         * 是否允许下一步
         * -11 通过
         * 0未认证  1审核中  2认证失败  3认证成功
         * 1 未认证
         * -10 无模板
         * -12 拼屏无模板
         * -13 直接进入模板列表
         */
        fun allowNext(tag: String, tag_b: String, msg: String? = "")

        fun allowAdDialog()
        fun appVersionResult(data: AppUpdateBean)
        fun installApk(filePath: String)
    }

    interface Present {
        fun certificationUser(oneTag: Boolean = false)
        fun getTemplateList()
        fun loadAppVersionTask()
        fun loadDownloadAppTask(url: String)
        fun loadMediaTask(template: Template)
    }
}