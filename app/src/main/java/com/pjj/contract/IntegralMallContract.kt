package com.pjj.contract

import com.pjj.module.GoodsListBean
import com.pjj.module.MallClassificationBean

/**
 * Create by xinheng on 2019/04/04 13:30。
 * describe：
 */
interface IntegralMallContract {
    interface View : BaseView {
        fun updateList(list: MutableList<GoodsListBean.GoodsListData>?)
        fun updateClassificationData(data: MutableList<MallClassificationBean.DataBean>)
    }


    interface Present {
        fun loadClassificationTask()
        /**
         * 加载商品
         */
        fun loadMall(pageNo: Int, pageNum: Int, integraGoodsTypeId: String)
    }
}