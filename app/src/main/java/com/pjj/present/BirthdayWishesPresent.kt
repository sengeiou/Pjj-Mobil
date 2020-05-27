package com.pjj.present

import com.pjj.contract.BirthdayWishesContract
import com.pjj.module.DiyDataBean

/**
 * Create by xinheng on 2019/05/28 17:58。
 * describe：P层
 */
class BirthdayWishesPresent(view: BirthdayWishesContract.View) : BasePresent<BirthdayWishesContract.View>(view, BirthdayWishesContract.View::class.java), BirthdayWishesContract.Present {

    override fun loadTemplateInfTask(id: String) {
        retrofitService.getAdTempletDetails(id, object : PresentCallBack<DiyDataBean>(DiyDataBean::class.java) {
            override fun successResult(t: DiyDataBean) {
                mView.update(t)
            }
        })
    }
}
