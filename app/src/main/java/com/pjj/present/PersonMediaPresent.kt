package com.pjj.present

import com.pjj.PjjApplication
import com.pjj.contract.PersonMediaContract
import com.pjj.module.ModuleMoreBean
import com.pjj.utils.TextUtils

class PersonMediaPresent(view: PersonMediaContract.View) : BasePresent<PersonMediaContract.View>(view, PersonMediaContract.View::class.java), PersonMediaContract.Present {
    private var callBack = object : PresentCallBack<ModuleMoreBean>(ModuleMoreBean::class.java) {
        override fun successResult(t: ModuleMoreBean) {
            if (TextUtils.isNotEmptyList(t.data)) {
                mView.update(t.data)
            }
        }
    }

    override fun loadModuleTask(purposeType: String) {
        retrofitService.getAdTemplet(PjjApplication.application.userId, purposeType, callBack)
    }

    override fun loadSelfUseTask() {
        retrofitService.getOwnAdTemplet(PjjApplication.application.userId, null, callBack)
    }
}