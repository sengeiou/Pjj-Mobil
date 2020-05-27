package com.pjj.present

import com.pjj.contract.MallHomepageContract
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.MallClassificationBean
import com.pjj.module.MallHomepageGoodsBean

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：P层
 */
class MallHomepagePresent(view: MallHomepageContract.View) : BasePresent<MallHomepageContract.View>(view, MallHomepageContract.View::class.java), MallHomepageContract.Present {
    override fun loadClassificationTask() {
        IntegralRetrofitService.instance.getAdverBanner(object : RetrofitService.CallbackClassResult<MallHomepageGoodsBean>(MallHomepageGoodsBean::class.java) {
            override fun successResult(t: MallHomepageGoodsBean) {
                mView.updateBanner(t.data.adverBanner)
                t.data.category.add(0, MallClassificationBean.DataBean().apply {
                    integraGoodsTypeId = "-1"
                    categoryName = "全部"
                })
                mView.updateTitleList(t.data.category)
            }

            override fun fail(error: String?) {
                super.fail(error)
                var arrayList = ArrayList<MallClassificationBean.DataBean>()
                arrayList.add(MallClassificationBean.DataBean().apply {
                    integraGoodsTypeId = "-1"
                    categoryName = "全部"
                })
                mView.updateTitleList(arrayList)
                mView.showNotice(error)
            }
        })
    }
}
