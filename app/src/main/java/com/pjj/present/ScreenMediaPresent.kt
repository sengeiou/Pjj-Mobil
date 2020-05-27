package com.pjj.present

import com.pjj.contract.ScreenMediaContract
import com.pjj.intent.RetrofitService
import com.pjj.module.NewMediaBuildingBean
import com.pjj.module.NewMediaScreenBean

/**
 * Create by xinheng on 2019/03/27 14:00。
 * describe：P层
 */
class ScreenMediaPresent(view: ScreenMediaContract.View) : BasePresent<ScreenMediaContract.View>(view, ScreenMediaContract.View::class.java), ScreenMediaContract.Present {

    override fun loadScreenDataList(communityId: String) {
        retrofitService.getNewMediaScreenInfoByCommunity(communityId,object :RetrofitService.CallbackClassResult<NewMediaScreenBean>(NewMediaScreenBean::class.java){
            override fun successResult(t: NewMediaScreenBean) {
                var data = t.data
                mView.updateView(data)
            }
        })
    }
}
