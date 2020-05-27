package com.pjj.present

import com.pjj.contract.IntegralMallContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.GoodsListBean
import com.pjj.module.MallClassificationBean
import com.pjj.utils.TextUtils

/**
 * Create by xinheng on 2019/04/04 13:30。
 * describe：P层
 */
class IntegralMallPresent(view: IntegralMallContract.View, private var mallTag: Boolean) : BasePresent<IntegralMallContract.View>(view, IntegralMallContract.View::class.java), IntegralMallContract.Present {
    override fun loadClassificationTask() {
        IntegralRetrofitService.instance.getIntegralGoodsType(object : RetrofitService.CallbackClassResult<MallClassificationBean>(MallClassificationBean::class.java) {
            override fun successResult(t: MallClassificationBean) {
                t.data.add(0, MallClassificationBean.DataBean().apply {
                    integraGoodsTypeId = "-1"
                    categoryName = "全部"
                })
                mView.updateClassificationData(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                var arrayList = ArrayList<MallClassificationBean.DataBean>()
                arrayList.add(MallClassificationBean.DataBean().apply {
                    integraGoodsTypeId = "-1"
                    categoryName = "全部"
                })
                mView.updateClassificationData(arrayList)
                mView.showNotice(error)
            }
        })
    }

    override fun loadMall(pageNo: Int, pageNum: Int, integraGoodsTypeId: String) {
        var callback = object : RetrofitService.CallbackClassResult<GoodsListBean>(GoodsListBean::class.java) {
            override fun successResult(t: GoodsListBean) {
                mView.updateList(t.data)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.showNotice(error)
            }
        }
        if (mallTag) {
            IntegralRetrofitService.instance.getGoodsByQuery(integraGoodsTypeId, pageNo, pageNum, null, callback)
        } else {
            IntegralRetrofitService.instance.getAppIntergralGoodsList(pageNo, pageNum, callback, integraGoodsTypeId)
        }
    }

}
