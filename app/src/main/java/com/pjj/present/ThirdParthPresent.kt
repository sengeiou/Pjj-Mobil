package com.pjj.present

import com.pjj.contract.ThirdPartyContract

/**
 * Created by XinHeng on 2019/03/12.
 * describe：
 */
class ThirdParthPresent(view:ThirdPartyContract.View):BasePresent<ThirdPartyContract.View>(view,ThirdPartyContract.View::class.java),ThirdPartyContract.Present {
    /**
     * 解绑
     */
    override fun loadUnbind() {
    }

    /**
     * 获取第三方信息
     */
    override fun loadGetThirdPartyInf() {
    }

}