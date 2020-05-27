package com.pjj.contract;

import com.pjj.module.ElevatorBean;
import com.pjj.module.parameters.Elevator;

/**
 * Create by xinheng on 2018/11/21 09:48。
 * describe：选择电梯
 */
public interface ElevatorContract {
    interface View extends BaseView {
        void updateList(ElevatorBean.DataBean dataBean);
    }

    interface Present {
        /**
         * 加载电梯列表
         * @param elevator
         */
        void loadElevatorListTask(Elevator elevator);
    }
}