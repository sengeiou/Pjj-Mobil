package com.pjj.module

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by XinHeng on 2019/04/09.
 * describe：商品列表
 */
class GoodsListBean : ResultBean() {
    var data: MutableList<GoodsListData>? = null
    /**
     * 商品数量
     */
    var dataNum: Int = 0

    class GoodsListData() : Parcelable {
        /**
         * 商品描述
         */
        var goodsDescribe: String? = null
        /**
         * 商品金币
         */
        @SerializedName(value ="goodsIntegral",alternate = ["goodsPrice"])
        var goodsIntegral: Float = 0f
        /**
         * 商品名称
         */
        var goodsName: String? = null
        /**
         * 商品图片
         */
        var goodsPicture: String? = null
        /**
         * 商品id
         */
        @SerializedName(value = "integraGoodsId", alternate = ["goodsId"])
        var integraGoodsId: String? = null
        /**
         * 商品种类  1礼品(暂时只有一类商品)
         */
        var integraGoodsTypeId: String? = null
        /**
         * 运费
         */
        var postCost: String? = null
        var inseralGoodsDescribeList: MutableList<GoodsDescribeImages>? = null

        constructor(parcel: Parcel) : this() {
            goodsDescribe = parcel.readString()
            goodsIntegral = parcel.readFloat()
            goodsName = parcel.readString()
            goodsPicture = parcel.readString()
            integraGoodsId = parcel.readString()
            integraGoodsTypeId = parcel.readString()
            postCost = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(goodsDescribe)
            parcel.writeFloat(goodsIntegral)
            parcel.writeString(goodsName)
            parcel.writeString(goodsPicture)
            parcel.writeString(integraGoodsId)
            parcel.writeString(integraGoodsTypeId)
            parcel.writeString(postCost)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GoodsListData> {
            override fun createFromParcel(parcel: Parcel): GoodsListData {
                return GoodsListData(parcel)
            }

            override fun newArray(size: Int): Array<GoodsListData?> {
                return arrayOfNulls(size)
            }
        }


    }

    class GoodsDescribeImages() : Parcelable {
        var goodsPicture: String? = null
        var integraGoodsId: String? = null
        var place: String? = null
        var goodType: String? = null

        constructor(parcel: Parcel) : this() {
            goodsPicture = parcel.readString()
            integraGoodsId = parcel.readString()
            place = parcel.readString()
            goodType = parcel.readString()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(goodsPicture)
            parcel.writeString(integraGoodsId)
            parcel.writeString(place)
            parcel.writeString(goodType)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<GoodsDescribeImages> {
            override fun createFromParcel(parcel: Parcel): GoodsDescribeImages {
                return GoodsDescribeImages(parcel)
            }

            override fun newArray(size: Int): Array<GoodsDescribeImages?> {
                return arrayOfNulls(size)
            }
        }

    }

}
