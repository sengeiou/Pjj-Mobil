package com.pjj.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MallOrderInfBean extends ResultBean {
    /**
     * data : {"address":"朗琴国际b座","storeId":"myself","goodOrderId":"14792120190515150014","postage":0,"phone":"110","goodsNumber":30,"goodsPrice":0,"name":"屏小家","orderDetail":[{"postage":0,"goodsId":"12019050916160052589506788774405","goodsNumber":10,"goodsPrice":0,"storeName":"官方自营","specificName":"银色,128g,中国大陆","storeId":"myself","goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg"},{"postage":0,"goodsId":"12019050916160052589506788774405","goodsNumber":20,"goodsPrice":0,"storeName":"官方自营","specificName":"黑色,64g,港澳台","storeId":"myself","goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg"}],"position":"北京北京市西城区","status":"1","courierNumber":"快递单号","express":"快递名称","notes":"取消订单注释","createTime":"下单时间","cancelTime":"取消订单时间"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * address : 朗琴国际b座
         * storeId : myself
         * goodOrderId : 14792120190515150014
         * postage : 0
         * phone : 110
         * goodsNumber : 30
         * goodsPrice : 0
         * name : 屏小家
         * orderDetail : [{"postage":0,"goodsId":"12019050916160052589506788774405","goodsNumber":10,"goodsPrice":0,"storeName":"官方自营","specificName":"银色,128g,中国大陆","storeId":"myself","goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg"},{"postage":0,"goodsId":"12019050916160052589506788774405","goodsNumber":20,"goodsPrice":0,"storeName":"官方自营","specificName":"黑色,64g,港澳台","storeId":"myself","goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg"}]
         * position : 北京北京市西城区
         * status : 1
         * courierNumber : 快递单号
         * express : 快递名称
         * notes : 取消订单注释
         * createTime : 下单时间
         * cancelTime : 取消订单时间
         */

        private String address;
        private String storeId;
        private String goodOrderId;
        private int postage;
        private String phone;
        private int goodsNumber;
        private float goodsPrice;
        private String name;
        private String position;
        @SerializedName("status")
        private String statusX;
        private String courierNumber;
        private String express;
        private String notes;
        private String createTime;
        private String cancelTime;
        private List<OrderDetailBean> orderDetail;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getGoodOrderId() {
            return goodOrderId;
        }

        public void setGoodOrderId(String goodOrderId) {
            this.goodOrderId = goodOrderId;
        }

        public int getPostage() {
            return postage;
        }

        public void setPostage(int postage) {
            this.postage = postage;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getGoodsNumber() {
            return goodsNumber;
        }

        public void setGoodsNumber(int goodsNumber) {
            this.goodsNumber = goodsNumber;
        }

        public float getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(float goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public String getCourierNumber() {
            return courierNumber;
        }

        public void setCourierNumber(String courierNumber) {
            this.courierNumber = courierNumber;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCancelTime() {
            return cancelTime;
        }

        public void setCancelTime(String cancelTime) {
            this.cancelTime = cancelTime;
        }

        public List<OrderDetailBean> getOrderDetail() {
            return orderDetail;
        }

        public void setOrderDetail(List<OrderDetailBean> orderDetail) {
            this.orderDetail = orderDetail;
        }

        public static class OrderDetailBean {
            /**
             * postage : 0
             * goodsId : 12019050916160052589506788774405
             * goodsNumber : 10
             * goodsPrice : 0
             * storeName : 官方自营
             * specificName : 银色,128g,中国大陆
             * storeId : myself
             * goodsName : iPhone 8全面屏手机
             * goodsPicture : b5a01db8388786a36734a1a963794101.jpg
             */

            private int postage;
            private String goodsId;
            private int goodsNumber;
            private float goodsPrice;
            private String storeName;
            private String specificName;
            private String storeId;
            private String goodsName;
            private String goodsPicture;

            public int getPostage() {
                return postage;
            }

            public void setPostage(int postage) {
                this.postage = postage;
            }

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public int getGoodsNumber() {
                return goodsNumber;
            }

            public void setGoodsNumber(int goodsNumber) {
                this.goodsNumber = goodsNumber;
            }

            public float getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(float goodsPrice) {
                this.goodsPrice = goodsPrice;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }

            public String getSpecificName() {
                return specificName;
            }

            public void setSpecificName(String specificName) {
                this.specificName = specificName;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsPicture() {
                return goodsPicture;
            }

            public void setGoodsPicture(String goodsPicture) {
                this.goodsPicture = goodsPicture;
            }
        }
    }
}
