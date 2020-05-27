package com.pjj.module.xspad

import com.pjj.module.ShopCarBean

/**
 * Created by XinHeng on 2019/04/11.
 * describe：构建金币商品订单
 */
class IntegralGoods {
    var storeId: String? = null
    var integral: String? = null //金币
    var describe: String? = null //   -- 商品名称
    var integraGoodsId: String? = null //  -- 商品id
    var position: String? = null //   -- 省市区
    var address: String? = null //   -- 详细地址
    var name: String? = null //   -- 收件人姓名
    var phone: String? = null //  --收件人电话
    var postCost: String? = null //,--邮费
    var goodsPicture: String? = null // --商品图片
    var count = 0
    var goods: MutableList<ShopCarBean.DataBean>? = null
}