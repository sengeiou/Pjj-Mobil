package com.pjj.contract

import com.pjj.module.SpeedBean
import com.pjj.module.SpeedDataBean

/**
 * Created by XinHeng on 2019/03/15.
 * describe：
 */
interface SpeedScreenContract {
    interface View:BaseView{
        fun updateSpeedData(dataBean: SpeedBean.DateBean)
    }
    interface Present{
        fun loadSpeedDataTask()
    }
}