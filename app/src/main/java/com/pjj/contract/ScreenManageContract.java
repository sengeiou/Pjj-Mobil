package com.pjj.contract;

import com.pjj.module.ScreenBean;

import java.util.List;

/**
 * Create by xinheng on 2019/05/20 17:55。
 * describe：
 */
public interface ScreenManageContract {
    interface View extends BaseView {
        void updateData(List<ScreenBean.DataBean> list);
    }

    interface Present {
        void loadScreenDataTask(String queryType);
    }
}