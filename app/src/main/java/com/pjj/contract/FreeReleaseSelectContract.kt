package com.pjj.contract

import com.pjj.module.VerificaBean

interface FreeReleaseSelectContract {
    interface View : BaseView {
        fun result(result: VerificaBean)
    }

    interface Present {
        fun loadRenZhengTask()
    }
}