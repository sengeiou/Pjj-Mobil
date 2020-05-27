package com.pjj.contract

import com.pjj.module.UserTempletBean
import com.pjj.module.parameters.ZhiDing

/**
 * Create by xinheng on 2019/07/25 14:25。
 * describe：
 */
interface ZhiDingContract {
    interface View : BaseView {
        fun updateDatas(list: MutableList<UserTempletBean.DataBean>?, indexEmpty: Int)
        fun resetPage()
    }

    interface Present {
        fun loadZhiDingSearchTask(bean: ZhiDing)

        fun loadTuiJianZhiDingTask(bean:ZhiDing)
    }
}