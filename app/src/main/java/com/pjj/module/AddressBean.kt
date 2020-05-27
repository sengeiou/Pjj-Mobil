package com.pjj.module

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by XinHeng on 2019/04/12.
 * describe：收货地址
 */
class AddressBean : ResultBean() {
    var data: MutableList<AddressData>? = null

    class AddressData() : Parcelable {
        var addressId: String? = null //  --收货地址id
        var areaCode: String? = null // --区域编码
        var position: String? = null //  -- 省市区
        var describe: String? = null //   --详细地址
        var phone: String? = null //  -- 收件人电话
        var name: String? = null //  --收件人姓名
        var isDefault: String? = null //  --是否默认 1默认 2非默认

        constructor(parcel: Parcel) : this() {
            addressId = parcel.readString()
            areaCode = parcel.readString()
            position = parcel.readString()
            describe = parcel.readString()
            phone = parcel.readString()
            name = parcel.readString()
            isDefault = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(addressId)
            parcel.writeString(areaCode)
            parcel.writeString(position)
            parcel.writeString(describe)
            parcel.writeString(phone)
            parcel.writeString(name)
            parcel.writeString(isDefault)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<AddressData> {
            override fun createFromParcel(parcel: Parcel): AddressData {
                return AddressData(parcel)
            }

            override fun newArray(size: Int): Array<AddressData?> {
                return arrayOfNulls(size)
            }
        }
    }

}