package com.pjj.contract

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：
 */
interface ScreenManageSettingTypeContract {
    interface View : BaseView {
        fun setSuccess()
    }

    interface Present {
        fun loadSetType(screenId: String, mediaPrice: String?, cooperationMode: String?, setVoice: String?)
        //fun loadSetVolumeTask(screenId: String,)
    }
}