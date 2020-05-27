package com.pjj.contract;

/**
 * Create by xinheng on 2018/11/06 14:45。
 * describe：
 */
public interface RegisterContract {
    interface View extends BaseView {
        void registerSuccess(String result);
        String getPhone();
        String getCode();
        String getPassword();

        /**
         * 登录成功
         */
        void loginSuccess();
        /**
         * 更新验证码倒计时
         */
        void updateCodeText(String text, boolean effiect);
    }

    interface Present {
        /**
         * 获取验证码
         */
        void loadGetCodeTask(String fromWhere);

        /**
         * 注册
         */
        void loadRegisterTask(String reset);

        /**
         * 验证码登录
         */
        void loginCode();
    }
}