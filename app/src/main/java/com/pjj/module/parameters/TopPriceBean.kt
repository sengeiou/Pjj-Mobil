package com.pjj.module.parameters

import android.os.Parcel
import android.os.Parcelable

class TopPriceBean() : Parcelable {
    var dataTimeLong: Int = 0
    var topId: String? = null
    var price: Double = 0.0
    var discount = 0.0
    var topName: String? = null

    constructor(parcel: Parcel) : this() {
        dataTimeLong = parcel.readInt()
        topId = parcel.readString()
        price = parcel.readDouble()
        discount = parcel.readDouble()
        topName = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(dataTimeLong)
        parcel.writeString(topId)
        parcel.writeDouble(price)
        parcel.writeDouble(discount)
        parcel.writeString(topName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopPriceBean> {
        override fun createFromParcel(parcel: Parcel): TopPriceBean {
            return TopPriceBean(parcel)
        }

        override fun newArray(size: Int): Array<TopPriceBean?> {
            return arrayOfNulls(size)
        }
    }
}