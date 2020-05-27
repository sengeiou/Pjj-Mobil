package com.pjj.contract

import com.pjj.module.AddressBean

/**
 * Create by xinheng on 2019/04/12 13:55。
 * describe：
 */
interface SelectAddressContract {
    interface View : BaseView {
        fun updateAddressList(dataList: MutableList<AddressBean.AddressData>?)
    }

    interface Present {
        fun loadAddressListTask()
    }
}