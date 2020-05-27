package com.pjj.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XinHeng on 2019/04/19.
 * describe：
 */
public class MallClassificationBean extends ResultBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * integraGoodsTypeId : 1
         * categoryName : 礼品
         */
        @SerializedName(value = "integraGoodsTypeId", alternate = {"goods_category_id"})
        private String integraGoodsTypeId;
        private String categoryName;

        public String getIntegraGoodsTypeId() {
            return integraGoodsTypeId;
        }

        public void setIntegraGoodsTypeId(String integraGoodsTypeId) {
            this.integraGoodsTypeId = integraGoodsTypeId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }
}
