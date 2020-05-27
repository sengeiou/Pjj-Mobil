package com.pjj.module;

import java.util.List;

/**
 * Created by XinHeng on 2018/12/11.
 * describe：
 */
public class IndustryBean extends ResultBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dicId : 1
         * describe : restaurant
         * dicName : 餐饮
         * dicValue : 1
         */

        private String dicId;
        private String describe;
        private String dicName;
        private String dicValue;

        public String getDicId() {
            return dicId;
        }

        public void setDicId(String dicId) {
            this.dicId = dicId;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }

        public String getDicName() {
            return dicName;
        }

        public void setDicName(String dicName) {
            this.dicName = dicName;
        }

        public String getDicValue() {
            return dicValue;
        }

        public void setDicValue(String dicValue) {
            this.dicValue = dicValue;
        }
    }
}
