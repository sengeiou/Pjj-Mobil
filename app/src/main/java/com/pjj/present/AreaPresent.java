package com.pjj.present;

import com.pjj.contract.AreaContract;
import com.pjj.intent.RetrofitService;
import com.pjj.module.AreaBean;
import com.pjj.module.AreaCodeBean;

import java.util.List;

/**
 * Create by xinheng on 2018/12/01 15:53。
 * describe：P层
 */
public class AreaPresent extends BasePresent<AreaContract.View> implements AreaContract.Present {

    public AreaPresent(AreaContract.View view) {
        super(view, AreaContract.View.class);
    }

    @Override
    public void loadAreaListTask(String cityName) {
        call = getRetrofitService().getAreaCodeByName(cityName, new RetrofitService.CallbackClassResult<AreaCodeBean>(AreaCodeBean.class) {
            @Override
            protected void successResult(AreaCodeBean areaCodeBean) {
                String cityCode = areaCodeBean.getCityCode();
                if (null != cityCode) {
                    loadAreaListTask_(cityCode);
                }
            }

            @Override
            protected void fail(String error) {
                super.fail(error);
                mView.showNotice(error);
            }
        });
    }

    private void loadAreaListTask_(String cityCode) {
        call = getRetrofitService().getCountyList(cityCode, new RetrofitService.CallbackClassResult<AreaBean>(AreaBean.class) {
            @Override
            protected void successResult(AreaBean areaBean) {
                List<AreaBean.CountyListBean> countyList = areaBean.getCountyList();
                mView.updateListView(countyList);
            }

            @Override
            protected void fail(String error) {
                super.fail(error);
                mView.showNotice(error);
            }
        });
    }
}
