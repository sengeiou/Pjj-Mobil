package com.pjj.present

import com.pjj.contract.ZhiDingContract
import com.pjj.module.UserTempletBean
import com.pjj.module.parameters.ZhiDing
import com.pjj.utils.TextUtils

/**
 * Create by xinheng on 2019/07/25 14:25。
 * describe：P层
 */
class ZhiDingPresent(view: ZhiDingContract.View) : BasePresent<ZhiDingContract.View>(view, ZhiDingContract.View::class.java), ZhiDingContract.Present {

    override fun loadZhiDingSearchTask(bean: ZhiDing) {
        retrofitService.getOrderTempletList(bean, object : PresentCallBack<UserTempletBean>(UserTempletBean::class.java) {
            override fun successResult(t: UserTempletBean) {
                var count = 1
                if (bean.pageNo > 1 || TextUtils.isEmpty(bean.templetName) || TextUtils.isNotEmptyList(t.data)) {
                    mView.updateDatas(t.data, count)
                } else {
                    mView.resetPage()
                }
            }
        })
    }

    override fun loadTuiJianZhiDingTask(bean: ZhiDing) {
        retrofitService.getRecommendedOrderTempletList(bean, object : PresentCallBack<UserTempletBean>(UserTempletBean::class.java) {
            override fun successResult(t: UserTempletBean) {
                var count = 1
                if (bean.pageNo == 1) {//第一次
                    if (null == t.data) {
                        t.data = ArrayList<UserTempletBean.DataBean>()
                    }
                    val size = t.data.size
                    t.data.add(0, UserTempletBean.DataBean().apply {
                        templetName = null
                    })
                    if (size > 0) {
                        t.data.add(1, UserTempletBean.DataBean().apply {
                            templetName = "recommended"
                            templet_id = "recommended"
                        })
                        count = 2
                    }
                }
                mView.updateDatas(t.data, count)
            }
        })
    }
}
