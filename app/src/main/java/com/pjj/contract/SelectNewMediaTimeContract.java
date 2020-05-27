package com.pjj.contract;

/**
 * Create by xinheng on 2019/03/29 17:17。
 * describe：
 */
public interface SelectNewMediaTimeContract {
    interface View extends BaseView {
        void updateResult(boolean isUse,String dates);
    }

    interface Present {
        void loadUseTime(String screenIds, String selectDate, String orderType);
    }
}