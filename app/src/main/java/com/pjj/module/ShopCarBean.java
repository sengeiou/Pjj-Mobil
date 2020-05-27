package com.pjj.module;

import com.google.gson.annotations.SerializedName;
import com.pjj.view.adapter.ShopCarAdapter;

import java.util.List;

public class ShopCarBean extends ResultBean {

    /**
     * data : [{"goodsList":[{"postage":0,"specificId":"78a05dcae6d847c0a6bb44e0ad62083f","goodsId":"12019050916160052589506788774405","goodsPrice":0,"specificName":"港澳台","shoppingCartId":"88845a32761911e9a45200163e0204d5","goodsName":"iPhone 8全面屏手机","goodsNum":1,"goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","status":"1"},{"postage":0,"specificId":"a8850321e2804418a7a78793c249dc67","goodsId":"12019051009224179852433265873461","goodsPrice":0,"specificName":"土豪金","shoppingCartId":"a2b34f3b756211e9a45200163e0204d5","goodsName":"iPhone 8全面屏手机","goodsNum":1,"goodsPicture":"9ea440be3f5bd7491056bf233409315b.jpg","status":"1"},{"postage":0,"specificId":"5a5c8e68e2a3403a894726887971106f","goodsId":"12019050916160052589506788774405","goodsPrice":0,"specificName":"中国大陆","shoppingCartId":"b8effb04756011e9a45200163e0204d5","goodsName":"iPhone 8全面屏手机","goodsNum":1,"goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","status":"1"}],"storeName":"官方自营","storeId":"myself"}]
     * dataNum : 3
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

    public static class DataBean extends SelectParent {
        /**
         * goodsList : [{"postage":0,"specificId":"78a05dcae6d847c0a6bb44e0ad62083f","goodsId":"12019050916160052589506788774405","goodsPrice":0,"specificName":"港澳台","shoppingCartId":"88845a32761911e9a45200163e0204d5","goodsName":"iPhone 8全面屏手机","goodsNum":1,"goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","status":"1"},{"postage":0,"specificId":"a8850321e2804418a7a78793c249dc67","goodsId":"12019051009224179852433265873461","goodsPrice":0,"specificName":"土豪金","shoppingCartId":"a2b34f3b756211e9a45200163e0204d5","goodsName":"iPhone 8全面屏手机","goodsNum":1,"goodsPicture":"9ea440be3f5bd7491056bf233409315b.jpg","status":"1"},{"postage":0,"specificId":"5a5c8e68e2a3403a894726887971106f","goodsId":"12019050916160052589506788774405","goodsPrice":0,"specificName":"中国大陆","shoppingCartId":"b8effb04756011e9a45200163e0204d5","goodsName":"iPhone 8全面屏手机","goodsNum":1,"goodsPicture":"b5a01db8388786a36734a1a963794101.jpg","status":"1"}]
         * storeName : 官方自营
         * storeId : myself
         */

        private String storeName;
        private String storeId;
        private List<GoodsListBean> goodsList;

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

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }


    }

    public static class GoodsListBean extends SelectParent {
        /**
         * postage : 0
         * specificId : 78a05dcae6d847c0a6bb44e0ad62083f
         * goodsId : 12019050916160052589506788774405
         * goodsPrice : 0
         * specificName : 港澳台
         * shoppingCartId : 88845a32761911e9a45200163e0204d5
         * goodsName : iPhone 8全面屏手机
         * goodsNum : 1
         * goodsPicture : b5a01db8388786a36734a1a963794101.jpg
         * status : 1
         */

        private float postage;
        private String specificId;
        private String goodsId;
        private float goodsPrice;
        private String specificName;
        private String shoppingCartId;
        private String goodsName;
        private int goodsNum;
        private int goodsNumberCount;
        private String goodsPicture;
        @SerializedName("status")
        private String statusX;

        public int getGoodsNumberCount() {
            return goodsNumberCount;
        }

        public void setGoodsNumberCount(int goodsNumberCount) {
            this.goodsNumberCount = goodsNumberCount;
        }

        @Override
        public void setSelect(boolean select) {
            if (goodsNumberCount > 0) {
                super.setSelect(select);
            } else {
                super.setSelect(false);
            }
        }


        public float getPostage() {
            return postage;
        }

        public void setPostage(float postage) {
            this.postage = postage;
        }

        public String getSpecificId() {
            return specificId;
        }

        public void setSpecificId(String specificId) {
            this.specificId = specificId;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public float getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(float goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public String getSpecificName() {
            return specificName;
        }

        public void setSpecificName(String specificName) {
            this.specificName = specificName;
        }

        public String getShoppingCartId() {
            return shoppingCartId;
        }

        public void setShoppingCartId(String shoppingCartId) {
            this.shoppingCartId = shoppingCartId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getGoodsNum() {
            return goodsNum;
        }

        public void setGoodsNum(int goodsNum) {
            this.goodsNum = goodsNum;
        }

        public String getGoodsPicture() {
            return goodsPicture;
        }

        public void setGoodsPicture(String goodsPicture) {
            this.goodsPicture = goodsPicture;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }
    }
}
