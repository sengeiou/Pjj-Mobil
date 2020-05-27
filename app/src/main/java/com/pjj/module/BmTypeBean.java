package com.pjj.module;

import java.util.List;

/**
 * Created by XinHeng on 2019/01/18.
 * describe：
 */
public class BmTypeBean extends ResultBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dicId : 4
         * dicName : 便民信息
         */

        private String dicId;
        private String dicName;

        public String getDicId() {
            return dicId;
        }

        public void setDicId(String dicId) {
            this.dicId = dicId;
        }

        public String getDicName() {
            return dicName;
        }

        public void setDicName(String dicName) {
            this.dicName = dicName;
        }
    }
}
