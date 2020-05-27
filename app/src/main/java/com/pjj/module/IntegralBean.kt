package com.pjj.module

/**
 * Created by XinHeng on 2019/04/10.
 * describe：
 */
class IntegralBean : ResultBean() {
    var data: MutableList<IntegralData>? = null

    class IntegralData {
        var createTime: Long = 0L
        var describe: String? = null
        var integralNum: Int = 0
        var integralOrdId: String? = null
        var type = "1"
    }
}