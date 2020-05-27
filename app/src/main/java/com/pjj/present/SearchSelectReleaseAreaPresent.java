package com.pjj.present;

import com.pjj.contract.SearchSelectReleaseAreaContract;
import com.pjj.intent.RetrofitService;
import com.pjj.module.BuildingBean;

/**
 * Create by xinheng on 2018/12/03 14:03。
 * describe：P层
 */
public class SearchSelectReleaseAreaPresent extends BasePresent<SearchSelectReleaseAreaContract.View> implements SearchSelectReleaseAreaContract.Present {

    public SearchSelectReleaseAreaPresent(SearchSelectReleaseAreaContract.View view) {
        super(view, SearchSelectReleaseAreaContract.View.class);
    }

    @Override
    public void loadAreaBuildingTask(String areaCode) {
        getRetrofitService().loadAreaBuildingTask(areaCode, new RetrofitService.CallbackClassResult<BuildingBean>(BuildingBean.class) {
            @Override
            protected void successResult(BuildingBean buildingBean) {
                mView.updateBuildingList(buildingBean.getCommunityList());
            }
        });
    }
}
