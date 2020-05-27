package com.pjj.contract;

import com.pjj.module.AreaBean;

import java.util.List;

/**
 * Create by xinheng on 2018/12/01 15:53。
 * describe：
 */
public interface AreaContract {
    interface View extends BaseView {
        void updateListView(List<AreaBean.CountyListBean> list);
    }

    interface Present {
        /**
         * 区列表
         *
         * @param cityName
         */
        void loadAreaListTask(String cityName);

    }
}