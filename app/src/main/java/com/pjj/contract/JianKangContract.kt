package com.pjj.contract

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pjj.module.parameters.JiangKang


/**
 * Created by XinHeng on 2019/03/26.
 * describe：
 */
interface JianKangContract {
    interface View : BaseView {
        fun commitSuccess()
        fun getCityData(array: JsonArray?)
    }

    interface Present {
        fun commit(param: JiangKang)
        //fun getCityData():JsonObject
        fun loadAreaData()
    }
}