package com.pjj.module;

import java.util.List;

public class TopPriceBean extends ResultBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * dataTimeLong : 1
         * createTime : 1562913951000
         * topId : f787fe86547f435983d0fa6bbe7db692
         * price : 200
         * discount : 1
         * topName : 1天
         * isDel : 0
         */

        private int dataTimeLong;
        private long createTime;
        private String topId;
        private float price;
        private float discount;
        private String topName;
        private String isDel;
        private boolean selectTag;

        public boolean isSelectTag() {
            return selectTag;
        }

        public void setSelectTag(boolean selectTag) {
            this.selectTag = selectTag;
        }

        public int getDataTimeLong() {
            return dataTimeLong;
        }

        public void setDataTimeLong(int dataTimeLong) {
            this.dataTimeLong = dataTimeLong;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getTopId() {
            return topId;
        }

        public void setTopId(String topId) {
            this.topId = topId;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getDiscount() {
            return discount;
        }

        public void setDiscount(float discount) {
            this.discount = discount;
        }

        public String getTopName() {
            return topName;
        }

        public void setTopName(String topName) {
            this.topName = topName;
        }

        public String getIsDel() {
            return isDel;
        }

        public void setIsDel(String isDel) {
            this.isDel = isDel;
        }
    }
}
