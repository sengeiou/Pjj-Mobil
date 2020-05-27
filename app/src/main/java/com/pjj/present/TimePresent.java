package com.pjj.present;

import com.pjj.contract.TimeContract;
import com.pjj.intent.RetrofitService;
import com.pjj.module.TimeDiscountBean;

/**
 * Create by xinheng on 2018/11/21 17:33。
 * describe：P层
 */
public class TimePresent extends BasePresent<TimeContract.View> implements TimeContract.Present {


    public TimePresent(TimeContract.View view) {
        super(view, TimeContract.View.class);
    }

    @Override
    public void loadTimeDiscountTask() {
        mView.showWaiteStatue();
        getRetrofitService().loadTimeDiscountTask(new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                TimeDiscountBean timeDiscountBean = new TimeDiscountBean(s);
                if (timeDiscountBean.result()) {
                    mView.saveTimeDiscountBean(timeDiscountBean);
                } else {
                    fail(timeDiscountBean.getMsg());
                }
            }

            @Override
            protected void fail(String error) {
                super.fail(error);
                mView.showNotice(error);
            }
        });
    }
}
