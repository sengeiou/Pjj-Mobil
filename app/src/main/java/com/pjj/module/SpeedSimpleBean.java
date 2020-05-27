package com.pjj.module;

import java.util.List;

/**
 * Created by XinHeng on 2019/03/21.
 * describe：
 */
public class SpeedSimpleBean extends ResultBean {
    private List<SpeedDataBean> data;

    public List<SpeedDataBean> getData() {
        return data;
    }

    public void setData(List<SpeedDataBean> data) {
        this.data = data;
    }
}
