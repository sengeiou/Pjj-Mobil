package com.pjj.module.parameters

import android.os.Parcel
import android.os.Parcelable

class ReleaseFreeOrder() : Parcelable {
    var userId: String? = null
    var title: String? = null
    var orderType: String? = null
    var fileName: String? = null
    var content: String? = null
    var topId: String? = null
    var price=0f
    var identityType: String? = null//1个人 2商家

    constructor(parcel: Parcel) : this() {
        userId = parcel.readString()
        title = parcel.readString()
        orderType = parcel.readString()
        fileName = parcel.readString()
        content = parcel.readString()
        topId = parcel.readString()
        price = parcel.readFloat()
        identityType = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(orderType)
        parcel.writeString(fileName)
        parcel.writeString(content)
        parcel.writeString(topId)
        parcel.writeFloat(price)
        parcel.writeString(identityType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReleaseFreeOrder> {
        override fun createFromParcel(parcel: Parcel): ReleaseFreeOrder {
            return ReleaseFreeOrder(parcel)
        }

        override fun newArray(size: Int): Array<ReleaseFreeOrder?> {
            return arrayOfNulls(size)
        }
    }

}