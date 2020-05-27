package com.pjj.contract;

/**
 * Create by xinheng on 2019/04/04 15:32。
 * describe：
 */
public interface MyIntegralContract {
    interface View extends BaseView {
        void updateMyIntegral(String integral);
    }

    interface Present {
        void loadMyIntegralTask();
    }
}