package com.pjj.present;

import android.os.CountDownTimer;

import com.pjj.PjjApplication;
import com.pjj.contract.RegisterContract;
import com.pjj.intent.RetrofitService;
import com.pjj.module.LoginResult;
import com.pjj.module.ResultBean;
import com.pjj.module.parameters.User;
import com.pjj.utils.SharedUtils;
import com.pjj.utils.TextUtils;
import com.pjj.utils.UserUtils;

/**
 * Create by xinheng on 2018/11/06 14:45。
 * describe：P层
 */
public class RegisterPresent extends BasePresent<RegisterContract.View> implements RegisterContract.Present {
    private TimeCount timeCount;
    private final int MAX_TIME = 60 * 1000;//60秒

    public RegisterPresent(RegisterContract.View view) {
        super(view, RegisterContract.View.class);
        timeCount = new TimeCount(MAX_TIME, 1000);
    }

    @Override
    public void loadGetCodeTask(String fromWhere) {
        String phone = mView.getPhone();
        String result = UserUtils.isRightPhone(phone);
        if (null == result) {
            call = getRetrofitService().loadGetCodeTask(phone, fromWhere, new RetrofitService.CallbackClassResult<ResultBean>(ResultBean.class) {
                @Override
                protected void successResult(ResultBean resultBean) {
                    timeCount.start();
                }

                @Override
                protected void fail(String error) {
                    super.fail(error);
                    mView.showNotice(error);
                }
            });
        } else {
            mView.showNotice(result);
        }
    }

    @Override
    public void loadRegisterTask(String reset) {
        String phone = mView.getPhone();
        String password = mView.getPassword();
        String code = mView.getCode();
        String result = UserUtils.checkRegisterInf(phone, password, code);
        if (null == result) {
            User user = new User(phone, password, code, reset);
            call = getRetrofitService().loadRegisterTask(user, new RetrofitService.CallbackClassResult<ResultBean>(ResultBean.class) {
                @Override
                protected void successResult(ResultBean resultBean) {
                    mView.registerSuccess("ok");
                }

                @Override
                protected void fail(String error) {
                    super.fail(error);
                    mView.showNotice(error);
                }
            });
        } else {
            mView.showNotice(result);
        }
    }

    @Override
    public void recycle() {
        super.recycle();
        if (timeCount != null) {
            try {
                timeCount.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            mView.updateCodeText("重新发送", true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mView.updateCodeText("倒计时" + millisUntilFinished / 1000 + "s", false);

        }
    }

    @Override
    public void loginCode() {
        String phone = mView.getPhone();
        String code = mView.getCode();
        String result = UserUtils.checkRegisterInf(phone, "adff", code);
        if (null == result) {
            User user = new User();
            user.setPhone(mView.getPhone());
            user.setCheckCode(mView.getCode());
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

        } else {
            mView.showNotice(result);
        }
    }
}
