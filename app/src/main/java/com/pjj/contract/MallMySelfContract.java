package com.pjj.contract;

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：
 */
public interface MallMySelfContract {
    interface View extends BaseView {
        void updateMyIntegral(String count);
    }

    interface Present {
        void loadMyIntegral();
    }
}