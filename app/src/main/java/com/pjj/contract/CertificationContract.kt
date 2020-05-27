package com.pjj.contract

import com.pjj.module.CertificationResultBean
import com.pjj.module.parameters.IdentityInf

/**
 * Create by xinheng on 2018/12/10 18:31。
 * describe：认证
 */
interface CertificationContract {
    interface View : BaseView {
        fun uploadResult(tag: Boolean, msg: String?)
        fun updateHasSelectedImage(bean: CertificationResultBean)
    }

    interface Present {
        fun loadUploadInformation(uploadTemplate: IdentityInf, tag: Int)
        fun loadCertificationFailTask(userId: String)
    }
}