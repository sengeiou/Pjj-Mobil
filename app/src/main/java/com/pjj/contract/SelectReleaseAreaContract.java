package com.pjj.contract;

import com.pjj.module.BuildingBean;
import com.pjj.module.parameters.Area;

import java.util.List;

/**
 * Create by xinheng on 2018/12/01 14:11。
 * describe：
 */
public interface SelectReleaseAreaContract {
    interface View extends BaseView {
        void updateGuessBuildingList(List<BuildingBean.CommunityListBean> list);
    }

    interface Present {
        /**
         * 定位
         */
        void startLocation();
        void loadGuessListTask(Area area);

        /**
         * 全国价格
         */
        void loadNationalPrice();
    }
}