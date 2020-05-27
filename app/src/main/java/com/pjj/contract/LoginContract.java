package com.pjj.contract;

/**
 * Create by xinheng on 2018/10/29 16:57。
 * describe：
 */
public interface LoginContract {
    interface View extends BaseView {
        String getPhone();
        String getPassword();
        void login();
        void toRegisterView();
        void toForgetPasswordView();
        void loginSuccess();
    }

    interface Present {
        /**
         * 登录
         */
        void loadLoginTask();
    }
}