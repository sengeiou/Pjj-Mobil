package com.pjj.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MallOrderListBean extends ResultBean {
    /**
     * data : [{"postage":0,"goodsOrderDetail":[{"goodsNumber":10,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","postage":0},{"goodsNumber":20,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","postage":0}],"goodsNumber":30,"goodsPrice":0,"storeName":"官方自营","storeId":"myself","goodOrderId":"14792120190515150014","status":"1"},{"postage":0,"goodsOrderDetail":[{"goodsNumber":30,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"9ea440be3f5bd7491056bf233409315b.jpg","postage":0},{"goodsNumber":10,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","postage":0},{"goodsNumber":20,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","postage":0}],"goodsNumber":60,"goodsPrice":0,"storeName":"官方自营","storeId":"myself","goodOrderId":"19392520190515100245","status":"1"},{"postage":0,"goodsOrderDetail":[{"goodsNumber":10,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","postage":0}],"goodsNumber":10,"goodsPrice":0,"storeName":"官方自营","storeId":"myself","goodOrderId":"15904820190515095809","status":"1"},{"postage":0,"goodsOrderDetail":[{"goodsNumber":10,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","postage":0}],"goodsNumber":10,"goodsPrice":0,"storeName":"官方自营","storeId":"myself","goodOrderId":"13221520190515095242","status":"1"}]
     * dataNum : 4
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
         * postage : 0
         * goodsOrderDetail : [{"goodsNumber":10,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","postage":0},{"goodsNumber":20,"goodsPrice":0,"goodsName":"iPhone 8全面屏手机","goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","postage":0}]
         * goodsNumber : 30
         * goodsPrice : 0
         * storeName : 官方自营
         * storeId : myself
         * goodOrderId : 14792120190515150014
         * status : 1
         */

        private int postage;
        private int goodsNumber;
        private float goodsPrice;
        private String storeName;
        private String storeId;
        private String goodOrderId;
        @SerializedName("status")
        private String statusX;//0待支付 1未发货 2已发货 3已完成  4订单取消
        private List<GoodsOrderDetailBean> goodsOrderDetail;

        public int getPostage() {
            return postage;
        }

        public void setPostage(int postage) {
            this.postage = postage;
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

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public List<GoodsOrderDetailBean> getGoodsOrderDetail() {
            return goodsOrderDetail;
        }

        public void setGoodsOrderDetail(List<GoodsOrderDetailBean> goodsOrderDetail) {
            this.goodsOrderDetail = goodsOrderDetail;
        }

        public static class GoodsOrderDetailBean {
            /**
             * goodsNumber : 10
             * goodsPrice : 0
             * goodsName : iPhone 8全面屏手机
             * goodsPicture : b5a01db8388786a36734a1a963794101.jpg
             * postage : 0
             */

            private int goodsNumber;
            private float goodsPrice;
            private String goodsName;
            private String goodsPicture;
            private int postage;

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

            public int getPostage() {
                return postage;
            }

            public void setPostage(int postage) {
                this.postage = postage;
            }
        }
    }
}
