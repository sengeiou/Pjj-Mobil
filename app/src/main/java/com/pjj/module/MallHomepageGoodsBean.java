package com.pjj.module;

import java.util.List;

public class MallHomepageGoodsBean extends ResultBean {

    /**
     * data : {"adverBanner":[{"goodsId":"12019051009224179852433265873461","bannerPicture":"b5a01db8388786a36734a1a963794101.jpg"},{"goodsId":"12019050916160052589506788774405","bannerPicture":"09b7808cc02733893068a50d2dac065a.jpg"}],"category":[{"goods_category_id":"9038a311723111e9a45200163e0204d5","categoryName":"手机"},{"goods_category_id":"94c8cc82041911e990a600163e0204d5","categoryName":"服饰鞋包"},{"goods_category_id":"a788df6a715111e9a45200163e0204d5","categoryName":"母婴"},{"goods_category_id":"2","categoryName":"生鲜水果"},{"goods_category_id":"1","categoryName":"食品饮料"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<AdverBannerBean> adverBanner;
        private List<MallClassificationBean.DataBean> category;

        public List<AdverBannerBean> getAdverBanner() {
            return adverBanner;
        }

        public void setAdverBanner(List<AdverBannerBean> adverBanner) {
            this.adverBanner = adverBanner;
        }

        public List<MallClassificationBean.DataBean> getCategory() {
            return category;
        }

        public void setCategory(List<MallClassificationBean.DataBean> category) {
            this.category = category;
        }

        public static class AdverBannerBean {
            /**
             * goodsId : 12019051009224179852433265873461
             * bannerPicture : b5a01db8388786a36734a1a963794101.jpg
             */

            private String goodsId;
            private String bannerPicture;

            public String getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(String goodsId) {
                this.goodsId = goodsId;
            }

            public String getBannerPicture() {
                return bannerPicture;
            }

            public void setBannerPicture(String bannerPicture) {
                this.bannerPicture = bannerPicture;
            }
        }
    }
}
