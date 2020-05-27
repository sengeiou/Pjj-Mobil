package com.pjj.present

import com.pjj.contract.BuildingContract
import com.pjj.intent.RetrofitService
import com.pjj.module.BuildingBean
import com.pjj.module.NationalPriceBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.CalculateUtils
import com.pjj.utils.TextUtils

/**
 * Create by xinheng on 2018/12/19 15:22。
 * describe：P层
 */
class BuildingPresent(view: BuildingContract.View) : BasePresent<BuildingContract.View>(view, BuildingContract.View::class.java), BuildingContract.Present {
    private lateinit var mBuildingBean: BuildingBean
    override fun loadBuildingListTask(areaCode: String) {
        retrofitService.loadAreaBuildingTask(areaCode, object : RetrofitService.CallbackClassResult<BuildingBean>(BuildingBean::class.java) {

            override fun successResult(buildingBean: BuildingBean) {
                mBuildingBean = buildingBean
                getXspBasePrice()
            }
        })
    }

    private fun getXspBasePrice() {
        retrofitService.getBasePrice(object : RetrofitService.CallbackClassResult<NationalPriceBean>(NationalPriceBean::class.java) {

            override fun successResult(nationalPriceBean: NationalPriceBean) {
                val data = mBuildingBean.communityList
                for (i in data.indices) {
                    val communityListBean = data[i]
                    val screenType = communityListBean.screenType
                    var v = -1.0
                    if (TextUtils.isNotEmptyList(screenType)) {
                        v = nationalPriceBean.getPrice(screenType[0]) * communityListBean.realDiscount
                    }
                    val price: Float
                    if (v >= 0) {
                        price = CalculateUtils.m1f(v.toFloat())
                        communityListBean.price = price
                    }
                }
                mView.updateBuildingList(data)
            }
        })
    }
}
