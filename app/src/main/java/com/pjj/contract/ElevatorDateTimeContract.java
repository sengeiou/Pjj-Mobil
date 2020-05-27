package com.pjj.contract;

import com.pjj.module.parameters.ElevatorTime;

import java.util.List;

/**
 * Created by XinHeng on 2018/11/23.
 * describe：
 */
public interface ElevatorDateTimeContract {
    interface View extends BaseView {
        /**
         * 更新天
         *
         * @param data
         */
        void updateDateDays(String data);

        /**
         * 更新可用时间 并集
         *
         * @param allUseTime
         */
        void updateTimes(List<Integer> allUseTime);

        /**
         * 起始时间，仅整周月有效
         *
         * @return
         */
        int getStartHour();

        String getStartDate();

        String getEndDate();
    }

    interface Present {
        /**
         * 加载不可用时间，取反
         *
         * @param elevatorTime
         */
        void loadElevatorTimeTask(ElevatorTime elevatorTime);
    }
}
