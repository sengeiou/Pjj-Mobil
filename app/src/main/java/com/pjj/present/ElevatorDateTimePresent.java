package com.pjj.present;

import android.util.Log;

import com.pjj.contract.ElevatorDateTimeContract;
import com.pjj.contract.ElevatorDateTimeContract.Present;
import com.pjj.intent.RetrofitService;
import com.pjj.module.ElevatorTimeBean;
import com.pjj.module.parameters.ElevatorTime;
import com.pjj.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by XinHeng on 2018/11/23.
 * describe：电梯时间获取
 */
public class ElevatorDateTimePresent extends BasePresent<ElevatorDateTimeContract.View> implements Present {
    private ElevatorTimeBean elevatorTimeBean;

    public ElevatorDateTimePresent(ElevatorDateTimeContract.View view) {
        super(view, ElevatorDateTimeContract.View.class);
    }

    @Override
    public void loadElevatorTimeTask(ElevatorTime elevatorTime) {
        if(TextUtils.isEmpty(elevatorTime.getScreentIds())){
            mView.cancelWaiteStatue();
            mView.updateTimes(new ArrayList<>());
            return;
        }
        getRetrofitService().loadElevatorTimeTask(elevatorTime, new RetrofitService.MyCallback() {
            @Override
            protected void success(String s) {
                mView.cancelWaiteStatue();
                elevatorTimeBean = new ElevatorTimeBean(s);
                int startHour = mView.getStartHour();
                if (elevatorTimeBean.result()) {//成功
                    if (startHour > 0) {
                        elevatorTimeBean.resetHour(startHour, mView.getStartDate(), mView.getEndDate());
                    }
                    List<Integer> allUseTime = elevatorTimeBean.getAllUseTime();//可用时间的并集
                    mView.updateTimes(allUseTime);
                } else {
                    fail(elevatorTimeBean.getMsg());
                }
            }

            @Override
            protected void fail(String error) {
                mView.cancelWaiteStatue();
                mView.showNotice(error);
                Log.e(TAG, "fail: " + error);
            }
        });
    }

    public ElevatorTimeBean getElevatorTimeBean() {
        return elevatorTimeBean;
    }
}
