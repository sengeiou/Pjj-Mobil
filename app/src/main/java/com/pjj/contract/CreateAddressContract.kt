package com.pjj.contract

import com.google.gson.JsonArray

/**
 * Create by xinheng on 2019/04/11 16:57。
 * describe：
 */
interface CreateAddressContract {
    interface View : BaseView {
        fun getCityData(array: JsonArray?)
        fun saveSuccess()
        fun deleteSuccess()
    }

    interface Present {
        fun loadAreaData()
        fun insertReceivingAddress(areaCode: String, position: String, describe: String, postId: String, phone: String, name: String, isDefault: String)
        fun updateReceivingAddress(addressId: String, areaCode: String, position: String, describe: String, postId: String, phone: String, name: String, isDefault: String)
        fun loadDeleteAddressTask(addressId: String)
    }
}