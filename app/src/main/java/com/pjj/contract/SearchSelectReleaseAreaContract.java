package com.pjj.contract;

import com.pjj.module.BuildingBean;

import java.util.List;

/**
 * Create by xinheng on 2018/12/03 14:03。
 * describe：
 */
public interface SearchSelectReleaseAreaContract {
    interface View extends BaseView {
        void updateBuildingList(List<BuildingBean.CommunityListBean> list);
    }

    interface Present {
        void loadAreaBuildingTask(String areaCode);
    }
}