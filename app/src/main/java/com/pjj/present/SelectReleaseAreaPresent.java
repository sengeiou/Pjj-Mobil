package com.pjj.present;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.pjj.PjjApplication;
import com.pjj.contract.SelectReleaseAreaContract;
import com.pjj.intent.RetrofitService;
import com.pjj.module.BuildingBean;
import com.pjj.module.NationalPriceBean;
import com.pjj.module.parameters.Area;
import com.pjj.utils.CalculateUtils;
import com.pjj.utils.Log;
import com.pjj.utils.TextUtils;

import java.util.List;

/**
 * Create by xinheng on 2018/12/01 14:11。
 * describe：P层
 */
public class SelectReleaseAreaPresent extends BasePresent<SelectReleaseAreaContract.View> implements SelectReleaseAreaContract.Present {
    private AMapLocationClientOption mLocationOption;

    public SelectReleaseAreaPresent(SelectReleaseAreaContract.View view) {
        super(view, SelectReleaseAreaContract.View.class);
    }

    @Override
    public void startLocation() {
    }

    private BuildingBean mBuildingBean;

    @Override
    public void loadGuessListTask(Area area) {
        mView.showWaiteStatue();
        getRetrofitService().loadGuessListTask(area, new RetrofitService.CallbackClassResult<BuildingBean>(BuildingBean.class) {
            @Override
            protected void successResult(BuildingBean buildingBean) {
                mBuildingBean = buildingBean;
                loadNationalPrice();
                mView.cancelWaiteStatue();
            }

            @Override
            protected void fail(String error) {
                super.fail(error);
                mView.cancelWaiteStatue();
                mView.showNotice(error);
            }
        });
    }


    @Override
    public void loadNationalPrice() {
        getRetrofitService().getBasePrice(new RetrofitService.CallbackClassResult<NationalPriceBean>(NationalPriceBean.class) {

            @Override
            protected void successResult(NationalPriceBean nationalPriceBean) {
                List<BuildingBean.CommunityListBean> data = mBuildingBean.getData();
                for (int i = 0; i < data.size(); i++) {
                    BuildingBean.CommunityListBean communityListBean = data.get(i);
                    List<String> screenType = communityListBean.getScreenType();
                    double v = -1;
                    if (TextUtils.isNotEmptyList(screenType)) {
                        v = nationalPriceBean.getPrice(screenType.get(0)) * communityListBean.getRealDiscount();
                    }
                    float price;
                    if (v >= 0) {
                        price = CalculateUtils.m1f((float) v);
                        communityListBean.setPrice(price);
                    }
                }
                mView.updateGuessBuildingList(data);
            }
        });
    }

    public void startLocation_() {
        //mLocationOption.s
    }
}
