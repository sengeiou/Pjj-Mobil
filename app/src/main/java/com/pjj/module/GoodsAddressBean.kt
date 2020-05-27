package com.pjj.module

/**
 * Created by XinHeng on 2019/04/11.
 * describe：
 */
class GoodsAddressBean : ResultBean() {
    var data: MutableList<GoodsAddressData>? = null

    class GoodsAddressData {
        var addressId: String? = null //92f902b69678434fb16f67f1107d4d5a;
        var areaCode: String? = null //110102;
        var describe: String? = null //"\U6717\U7434\U56fd\U9645b\U5ea7";
        var isDefault: String? = null //2;
        var name: String? = null //"\U6d4b\U8bd5";
        var phone: String? = null //13800000001;
        var position: String? = null //"\U5317\U4eac\U5317\U4eac\U5e02\U897f\U57ce\U533a";
        var userId: String? = null //d1b944ba78f544458c401409cfbc4762;
    }
}