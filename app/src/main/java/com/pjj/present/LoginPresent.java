package com.pjj.present;

import com.pjj.PjjApplication;
import com.pjj.contract.LoginContract;
import com.pjj.intent.RetrofitService;
import com.pjj.module.LoginResult;
import com.pjj.module.parameters.User;
import com.pjj.utils.SharedUtils;
import com.pjj.utils.TextUtils;
import com.pjj.utils.UserUtils;

/**
 * Create by xinheng on 2018/10/29 16:57。
 * describe：P层
 */
public class LoginPresent extends BasePresent<LoginContract.View> implements LoginContract.Present {

    public LoginPresent(LoginContract.View view) {
        super(view, LoginContract.View.class);
    }

    @Override
    public void loadLoginTask() {
        String phone = mView.getPhone();
        String password = mView.getPassword();
        String result = UserUtils.checkLoginInf(phone, password);
        if (null == result) {
            User user = new User();
            user.setPhone(phone);
            user.setPassword(password);
            login(user);
        } else {
            mView.showNotice(result);
        }
    }

    public void login(User user) {
        mView.showWaiteStatue();
        getRetrofitService().loadLoginTask(user, new RetrofitService.CallbackClassResult<LoginResult>(LoginResult.class) {
            @Override
            protected void successResult(LoginResult resultBean) {
                String token = resultBean.getToken();
                mView.cancelWaiteStatue();
                if (TextUtils.isEmpty(token)) {
                    mView.showNotice("登录信息错误");
                    return;
                }
                String userId = resultBean.getData().getUserId();
                String userType = resultBean.getData().getUserType();
                if (TextUtils.isEmpty(userId)) {
                    mView.showNotice("登录信息错误");
                    return;
                }
                RetrofitService.getInstance().setToken(token);
                SharedUtils.saveForXml(SharedUtils.TOKEN, token);
                SharedUtils.saveForXml(SharedUtils.USER_ID, userId);
                SharedUtils.saveForXml(SharedUtils.USER_TYPE, TextUtils.clean(userType));
                SharedUtils.saveForXml(SharedUtils.USER_PHONE, resultBean.getData().getPhoneNumber());
                SharedUtils.saveForXml(SharedUtils.USER_NICKNAME, resultBean.getData().getNickname());
                PjjApplication.application.setUserId(userId);
                SharedUtils.saveForXml(SharedUtils.HEAD_IMG, resultBean.getData().getHeadName());
                mView.loginSuccess();
            }

            @Override
            protected void fail(String error) {
                super.fail(error);
                mView.cancelWaiteStatue();
                mView.showNotice(error);
            }
        });
    }
}
