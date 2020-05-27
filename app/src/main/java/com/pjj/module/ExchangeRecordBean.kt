package com.pjj.module

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by XinHeng on 2019/04/11.
 * describe：
 */
class ExchangeRecordBean : ResultBean() {
    var dataNum: Int = 0
    var data: MutableList<ExchangeRecordData>? = null

    class ExchangeRecordData() : Parcelable {
        var address: String? = null //朗琴国际",  -- 详细地址
        var createTime: String? = null //2019-04-10 17:12:48.0",   --订单时间
        var goodsIntegral: Int = 0 //2000,  -- 商品金币
        var goodsName: String? = null //测试商品",   -- 商品名称
        var goodsPicture: String? = null //9ea440be3f5bd7491056bf233409315b.jpg",  --商品图片
        var integralNum: Int = 0 //2000,  -- 消费金币
        var name: String? = null //测试",  --收件人姓名
        var orderId: String? = null //20000016777272842019041017124914",  --订单编号
        var phone: String? = null //13800000002",  -- 联系电话
        var position: String? = null //北京北京市西城区",  --省市区
        var postCost: Int = 0 //0,  --邮费
        var status: String? = null //1",   --状态  1代发货 2收货 3已完成
        var courierNumber: String? = null //" , -- 运单号
        var express: String? = null //"  -- 快递名称

        constructor(parcel: Parcel) : this() {
            address = parcel.readString()
            createTime = parcel.readString()
            goodsIntegral = parcel.readInt()
            goodsName = parcel.readString()
            goodsPicture = parcel.readString()
            integralNum = parcel.readInt()
            name = parcel.readString()
            orderId = parcel.readString()
            phone = parcel.readString()
            position = parcel.readString()
            postCost = parcel.readInt()
            status = parcel.readString()
            courierNumber = parcel.readString()
            express = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(address)
            parcel.writeString(createTime)
            parcel.writeInt(goodsIntegral)
            parcel.writeString(goodsName)
            parcel.writeString(goodsPicture)
            parcel.writeInt(integralNum)
            parcel.writeString(name)
            parcel.writeString(orderId)
            parcel.writeString(phone)
            parcel.writeString(position)
            parcel.writeInt(postCost)
            parcel.writeString(status)
            parcel.writeString(courierNumber)
            parcel.writeString(express)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ExchangeRecordData> {
            override fun createFromParcel(parcel: Parcel): ExchangeRecordData {
                return ExchangeRecordData(parcel)
            }

            override fun newArray(size: Int): Array<ExchangeRecordData?> {
                return arrayOfNulls(size)
            }
        }
    }

}