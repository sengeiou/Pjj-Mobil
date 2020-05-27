package com.pjj.module;

import java.util.List;

public class ScreenManageOrderListBean extends ResultBean {
    /**
     * data : [{"fileName":"CAF869C52B180F79C0FE661913405BA1.mp4","createTime":1558669123000,"playDate":"2019.05.24","type":"2","ownOrderId":"20190524113843155866912389692829","fileType":"2","orderName":"朗琴国际B座二楼小屏"}]
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
         * fileName : CAF869C52B180F79C0FE661913405BA1.mp4
         * createTime : 1558669123000
         * playDate : 2019.05.24
         * type : 2
         * ownOrderId : 20190524113843155866912389692829
         * fileType : 2
         * orderName : 朗琴国际B座二楼小屏
         */

        private String fileName;
        private long createTime;
        private String playDate;
        private String type;
        private String ownOrderId;
        private String fileType;
        private String orderName;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getPlayDate() {
            return playDate;
        }

        public void setPlayDate(String playDate) {
            this.playDate = playDate;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getOwnOrderId() {
            return ownOrderId;
        }

        public void setOwnOrderId(String ownOrderId) {
            this.ownOrderId = ownOrderId;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getOrderName() {
            return orderName;
        }

        public void setOrderName(String orderName) {
            this.orderName = orderName;
        }
    }
}
