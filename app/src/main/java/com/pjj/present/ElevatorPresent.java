package com.pjj.present;

import com.pjj.contract.ElevatorContract;
import com.pjj.intent.RetrofitService;
import com.pjj.module.ElevatorBean;
import com.pjj.module.parameters.Elevator;

/**
 * Create by xinheng on 2018/11/21 09:48。
 * describe：P层
 */
public class ElevatorPresent extends BasePresent<ElevatorContract.View> implements ElevatorContract.Present {

    public ElevatorPresent(ElevatorContract.View view) {
        super(view, ElevatorContract.View.class);
    }

    @Override
    public void loadElevatorListTask(Elevator elevator) {
        getRetrofitService().loadElevatorListTask(elevator, new RetrofitService.CallbackClassResult<ElevatorBean>(ElevatorBean.class) {
            @Override
            protected void successResult(ElevatorBean elevatorBean) {
                mView.updateList(elevatorBean.getData());
            }
        });
    }
}
