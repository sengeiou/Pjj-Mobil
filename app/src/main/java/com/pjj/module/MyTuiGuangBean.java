package com.pjj.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyTuiGuangBean extends ResultBean {
    /**
     * data : [{"orderId":"20190726135636156412059657995185","topId":"f787fe86547f435983d0fa6bbe7db692","templet":{"templetName":"模板名称","templet_id":"756705c726724d98b8d8fa899526a593","templetType":"3","fileList":[{"fileName":"7F6BA390BDB061BB42C82F3FE0E33F31.mp4","type":"2","filePlace":"1"},{"fileName":"2B6C69C210C0B714A2055114351C7AA8.jpg","type":"1","filePlace":"1"}]},"topOrderId":"15641214274702019072614102769506","discount":1,"communityList":[{"communityName":"朗琴国际B座","communityId":"95"}],"userId":"6a9ed10373c8429284e85c9ed154ca1b","sourcePrice":0,"releaseCreateTime":1564120596000,"revokeTime":1564121451000,"dataTimeLong":1,"createTime":1564121427000,"price":0,"playDate":"2019.07.26","payOverTime":1564122027000,"orderOverTime":1564297200000,"revokeMsg":"不符合发布规则","isDel":"0","orderStartTime":1564210800000,"status":"4"}]
     * dataNum : 1
     */

    private int dataNum;
    private List<DataBean> data;

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderId : 20190726135636156412059657995185
         * topId : f787fe86547f435983d0fa6bbe7db692
         * templet : {"templetName":"模板名称","templet_id":"756705c726724d98b8d8fa899526a593","templetType":"3","fileList":[{"fileName":"7F6BA390BDB061BB42C82F3FE0E33F31.mp4","type":"2","filePlace":"1"},{"fileName":"2B6C69C210C0B714A2055114351C7AA8.jpg","type":"1","filePlace":"1"}]}
         * topOrderId : 15641214274702019072614102769506
         * discount : 1
         * communityList : [{"communityName":"朗琴国际B座","communityId":"95"}]
         * userId : 6a9ed10373c8429284e85c9ed154ca1b
         * sourcePrice : 0
         * releaseCreateTime : 1564120596000
         * revokeTime : 1564121451000
         * dataTimeLong : 1
         * createTime : 1564121427000
         * price : 0
         * playDate : 2019.07.26
         * payOverTime : 1564122027000
         * orderOverTime : 1564297200000
         * revokeMsg : 不符合发布规则
         * isDel : 0
         * orderStartTime : 1564210800000
         * status : 4
         */

        private String orderId;
        private String topId;
        private UserTempletBean.DataBean templet;
        private String topOrderId;
        private int discount;
        private String userId;
        private float sourcePrice;
        private long releaseCreateTime;
        private long revokeTime;
        private int dataTimeLong;
        private long createTime;
        private long lastTopOrderTime;
        private float price;
        private String playDate;
        private long payOverTime;
        private long orderOverTime;
        private String revokeMsg;
        private String isDel;
        private long orderStartTime;
        @SerializedName("status")
        private String statusX;

        public long getLastTopOrderTime() {
            return lastTopOrderTime;
        }

        public void setLastTopOrderTime(long lastTopOrderTime) {
            this.lastTopOrderTime = lastTopOrderTime;
        }

        private List<CommunityListBean> communityList;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getTopId() {
            return topId;
        }

        public void setTopId(String topId) {
            this.topId = topId;
        }

        public UserTempletBean.DataBean getTemplet() {
            return templet;
        }

        public void setTemplet(UserTempletBean.DataBean templet) {
            this.templet = templet;
        }

        public String getTopOrderId() {
            return topOrderId;
        }

        public void setTopOrderId(String topOrderId) {
            this.topOrderId = topOrderId;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


        public long getReleaseCreateTime() {
            return releaseCreateTime;
        }

        public void setReleaseCreateTime(long releaseCreateTime) {
            this.releaseCreateTime = releaseCreateTime;
        }

        public long getRevokeTime() {
            return revokeTime;
        }

        public void setRevokeTime(long revokeTime) {
            this.revokeTime = revokeTime;
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

        public float getSourcePrice() {
            return sourcePrice;
        }

        public void setSourcePrice(float sourcePrice) {
            this.sourcePrice = sourcePrice;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getPlayDate() {
            return playDate;
        }

        public void setPlayDate(String playDate) {
            this.playDate = playDate;
        }

        public long getPayOverTime() {
            return payOverTime;
        }

        public void setPayOverTime(long payOverTime) {
            this.payOverTime = payOverTime;
        }

        public long getOrderOverTime() {
            return orderOverTime;
        }

        public void setOrderOverTime(long orderOverTime) {
            this.orderOverTime = orderOverTime;
        }

        public String getRevokeMsg() {
            return revokeMsg;
        }

        public void setRevokeMsg(String revokeMsg) {
            this.revokeMsg = revokeMsg;
        }

        public String getIsDel() {
            return isDel;
        }

        public void setIsDel(String isDel) {
            this.isDel = isDel;
        }

        public long getOrderStartTime() {
            return orderStartTime;
        }

        public void setOrderStartTime(long orderStartTime) {
            this.orderStartTime = orderStartTime;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public List<CommunityListBean> getCommunityList() {
            return communityList;
        }

        public void setCommunityList(List<CommunityListBean> communityList) {
            this.communityList = communityList;
        }

        public static class CommunityListBean {
            /**
             * communityName : 朗琴国际B座
             * communityId : 95
             */

            private String communityName;
            private String communityId;

            public String getCommunityName() {
                return communityName;
            }

            public void setCommunityName(String communityName) {
                this.communityName = communityName;
            }

            public String getCommunityId() {
                return communityId;
            }

            public void setCommunityId(String communityId) {
                this.communityId = communityId;
            }
        }
    }
}
