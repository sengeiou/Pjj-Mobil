package com.pjj.module;

import java.util.List;

public class GoodsDetailBean extends ResultBean {
    /**
     * data : {"goodsDescribe":"[{\"title\":\"品牌\",\"content\":\"苹果8\"},{\"title\":\"电池容量\",\"content\":\"88888\"}]","goodsNumberCount":4000,"goodsBannerList":["9ea440be3f5bd7491056bf233409315b.jpg","f2f3477a45f957ff08cf3a039072ba77.jpg","9ea440be3f5bd7491056bf233409315b.jpg"],"goodsDescribeList":["f2f3477a45f957ff08cf3a039072ba77.jpg","9ea440be3f5bd7491056bf233409315b.jpg"],"minGoodsPrice":3888,"maxGoodsPrice":3888,"goodsId":"12019050916160052589506788774405","goodSpecificInfoAll":[{"specificId":"a840ef8755d64414852208a4a5e2b302","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"测试","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0","specificPath":"测试"},{"specificId":"30d90e2565ce4d1f846c247e85d6040b","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"128g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4f493a337cae41a49acb4d260dd21a05","specificPath":"银色,128g"},{"specificId":"4051d3447dbb438a89a3802fc518cfe6","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":1000,"specificName":"黑色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0","specificPath":"黑色"},{"specificId":"6283bf2aa1e8469791aa17fa286217c8","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"64g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4f493a337cae41a49acb4d260dd21a05","specificPath":"银色,64g"},{"specificId":"78a05dcae6d847c0a6bb44e0ad62083f","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"港澳台","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"43f544bbe6324b9ca142f72d6cb3c49a","specificPath":"黑色,64g,港澳台"},{"specificId":"5a5c8e68e2a3403a894726887971106f","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"中国大陆","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"30d90e2565ce4d1f846c247e85d6040b","specificPath":"银色,128g,中国大陆"},{"specificId":"29215d8e69aa4c089f50ff46f69627e1","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"美国","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"43f544bbe6324b9ca142f72d6cb3c49a","specificPath":"黑色,64g,美国"},{"specificId":"4f493a337cae41a49acb4d260dd21a05","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"银色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0","specificPath":"银色"},{"specificId":"43f544bbe6324b9ca142f72d6cb3c49a","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"64g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4051d3447dbb438a89a3802fc518cfe6","specificPath":"黑色,64g"},{"specificId":"a5bb9cb490654af680cd6f244846cd35","goodsId":"12019050916160052589506788774405","goodsPrice":10000,"goodsNumber":10000,"specificName":"256g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4051d3447dbb438a89a3802fc518cfe6","specificPath":"黑色,256g"}],"postCost":1,"goodSpecificType":[{"showPicture":"1","typeName":"外观","typeLevel":"1","specificInfoList":[{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"a840ef8755d64414852208a4a5e2b302","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"测试","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"},{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"4f493a337cae41a49acb4d260dd21a05","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"银色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"},{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"4051d3447dbb438a89a3802fc518cfe6","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":1000,"specificName":"黑色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"}],"goodTypeId":"9805bcf1a1dc4890994203bd25d13142"},{"showPicture":"0","typeName":"机身存储","typeLevel":"2","specificInfoList":[{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"30d90e2565ce4d1f846c247e85d6040b","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"128g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4f493a337cae41a49acb4d260dd21a05"},{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"a5bb9cb490654af680cd6f244846cd35","goodsId":"12019050916160052589506788774405","goodsPrice":10000,"goodsNumber":10000,"specificName":"256g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4051d3447dbb438a89a3802fc518cfe6"},{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"43f544bbe6324b9ca142f72d6cb3c49a","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"64g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4051d3447dbb438a89a3802fc518cfe6"}],"goodTypeId":"934e4acf8a824bbd9a156a357f215026"},{"showPicture":"0","typeName":"版本类型","typeLevel":"3","specificInfoList":[{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"5a5c8e68e2a3403a894726887971106f","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"中国大陆","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"30d90e2565ce4d1f846c247e85d6040b"},{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"78a05dcae6d847c0a6bb44e0ad62083f","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"港澳台","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"43f544bbe6324b9ca142f72d6cb3c49a"},{"specificId":"29215d8e69aa4c089f50ff46f69627e1","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"美国","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"43f544bbe6324b9ca142f72d6cb3c49a"}],"goodTypeId":"c33c85c355704a7da7d1924cd3e02977"}],"goodsName":"iPhone 8全面屏手机","goodsPicture":"9ea440be3f5bd7491056bf233409315b.jpg"}
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
         * goodsInfo : [{"title":"品牌","content":"苹果8"},{"title":"电池容量","content":"88888"}]
         * goodsNumberCount : 4000
         * goodsBannerList : ["9ea440be3f5bd7491056bf233409315b.jpg","f2f3477a45f957ff08cf3a039072ba77.jpg","9ea440be3f5bd7491056bf233409315b.jpg"]
         * goodsDescribeList : ["f2f3477a45f957ff08cf3a039072ba77.jpg","9ea440be3f5bd7491056bf233409315b.jpg"]
         * minGoodsPrice : 3888
         * maxGoodsPrice : 3888
         * goodsId : 12019050916160052589506788774405
         * goodSpecificInfoAll : [{"specificId":"a840ef8755d64414852208a4a5e2b302","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"测试","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0","specificPath":"测试"},{"specificId":"30d90e2565ce4d1f846c247e85d6040b","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"128g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4f493a337cae41a49acb4d260dd21a05","specificPath":"银色,128g"},{"specificId":"4051d3447dbb438a89a3802fc518cfe6","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":1000,"specificName":"黑色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0","specificPath":"黑色"},{"specificId":"6283bf2aa1e8469791aa17fa286217c8","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"64g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4f493a337cae41a49acb4d260dd21a05","specificPath":"银色,64g"},{"specificId":"78a05dcae6d847c0a6bb44e0ad62083f","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"港澳台","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"43f544bbe6324b9ca142f72d6cb3c49a","specificPath":"黑色,64g,港澳台"},{"specificId":"5a5c8e68e2a3403a894726887971106f","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"中国大陆","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"30d90e2565ce4d1f846c247e85d6040b","specificPath":"银色,128g,中国大陆"},{"specificId":"29215d8e69aa4c089f50ff46f69627e1","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"美国","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"43f544bbe6324b9ca142f72d6cb3c49a","specificPath":"黑色,64g,美国"},{"specificId":"4f493a337cae41a49acb4d260dd21a05","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"银色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0","specificPath":"银色"},{"specificId":"43f544bbe6324b9ca142f72d6cb3c49a","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"64g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4051d3447dbb438a89a3802fc518cfe6","specificPath":"黑色,64g"},{"specificId":"a5bb9cb490654af680cd6f244846cd35","goodsId":"12019050916160052589506788774405","goodsPrice":10000,"goodsNumber":10000,"specificName":"256g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4051d3447dbb438a89a3802fc518cfe6","specificPath":"黑色,256g"}]
         * postCost : 1
         * goodSpecificType : [{"showPicture":"1","typeName":"外观","typeLevel":"1","specificInfoList":[{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"a840ef8755d64414852208a4a5e2b302","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"测试","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"},{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"4f493a337cae41a49acb4d260dd21a05","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"银色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"},{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"4051d3447dbb438a89a3802fc518cfe6","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":1000,"specificName":"黑色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"}],"goodTypeId":"9805bcf1a1dc4890994203bd25d13142"},{"showPicture":"0","typeName":"机身存储","typeLevel":"2","specificInfoList":[{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"30d90e2565ce4d1f846c247e85d6040b","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"128g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4f493a337cae41a49acb4d260dd21a05"},{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"a5bb9cb490654af680cd6f244846cd35","goodsId":"12019050916160052589506788774405","goodsPrice":10000,"goodsNumber":10000,"specificName":"256g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4051d3447dbb438a89a3802fc518cfe6"},{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"43f544bbe6324b9ca142f72d6cb3c49a","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"64g","goodTypeId":"934e4acf8a824bbd9a156a357f215026","parentSpecificId":"4051d3447dbb438a89a3802fc518cfe6"}],"goodTypeId":"934e4acf8a824bbd9a156a357f215026"},{"showPicture":"0","typeName":"版本类型","typeLevel":"3","specificInfoList":[{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"5a5c8e68e2a3403a894726887971106f","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"中国大陆","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"30d90e2565ce4d1f846c247e85d6040b"},{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"78a05dcae6d847c0a6bb44e0ad62083f","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"港澳台","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"43f544bbe6324b9ca142f72d6cb3c49a"},{"specificId":"29215d8e69aa4c089f50ff46f69627e1","goodsId":"12019050916160052589506788774405","goodsPrice":3888,"goodsNumber":2000,"specificName":"美国","goodTypeId":"c33c85c355704a7da7d1924cd3e02977","parentSpecificId":"43f544bbe6324b9ca142f72d6cb3c49a"}],"goodTypeId":"c33c85c355704a7da7d1924cd3e02977"}]
         * goodsName : iPhone 8全面屏手机
         * goodsPicture : 9ea440be3f5bd7491056bf233409315b.jpg
         */

        private String goodsDescribe;
        private String postStatus;//邮费方式  1包邮 2收费  3运费到付
        private String goodsInfo;
        private int goodsNumberCount;
        private float minGoodsPrice;
        private float maxGoodsPrice;
        private String goodsId;
        private int postCost;
        private String goodsName;
        private String storeId;
        private String storeName;
        private String goodsPicture;
        private float goodsPrice;
        private List<String> goodsBannerList;
        private List<String> goodsDescribeList;
        private List<GoodSpecificInfoAllBean> goodSpecificInfoAll;
        private List<GoodSpecificTypeBean> goodSpecificType;

        public String getPostStatus() {
            return postStatus;
        }

        public float getFinalPrice() {
            float postPrice = ("2".equals(postStatus)) ? postCost : 0f;
            return goodsPrice + postPrice;
        }

        public void setPostStatus(String postStatus) {
            this.postStatus = postStatus;
        }

        public float getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(float goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public String getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(String goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getGoodsDescribe() {
            return goodsDescribe;
        }

        public void setGoodsDescribe(String goodsDescribe) {
            this.goodsDescribe = goodsDescribe;
        }

        public int getGoodsNumberCount() {
            return goodsNumberCount;
        }

        public void setGoodsNumberCount(int goodsNumberCount) {
            this.goodsNumberCount = goodsNumberCount;
        }

        public float getMinGoodsPrice() {
            return minGoodsPrice;
        }

        public void setMinGoodsPrice(float minGoodsPrice) {
            this.minGoodsPrice = minGoodsPrice;
        }

        public float getMaxGoodsPrice() {
            return maxGoodsPrice;
        }

        public void setMaxGoodsPrice(float maxGoodsPrice) {
            this.maxGoodsPrice = maxGoodsPrice;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public int getPostCost() {
            return postCost;
        }

        public void setPostCost(int postCost) {
            this.postCost = postCost;
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

        public List<String> getGoodsBannerList() {
            return goodsBannerList;
        }

        public void setGoodsBannerList(List<String> goodsBannerList) {
            this.goodsBannerList = goodsBannerList;
        }

        public List<String> getGoodsDescribeList() {
            return goodsDescribeList;
        }

        public void setGoodsDescribeList(List<String> goodsDescribeList) {
            this.goodsDescribeList = goodsDescribeList;
        }

        public List<GoodSpecificInfoAllBean> getGoodSpecificInfoAll() {
            return goodSpecificInfoAll;
        }

        public void setGoodSpecificInfoAll(List<GoodSpecificInfoAllBean> goodSpecificInfoAll) {
            this.goodSpecificInfoAll = goodSpecificInfoAll;
        }

        public List<GoodSpecificTypeBean> getGoodSpecificType() {
            return goodSpecificType;
        }

        public void setGoodSpecificType(List<GoodSpecificTypeBean> goodSpecificType) {
            this.goodSpecificType = goodSpecificType;
        }

        public static class GoodSpecificInfoAllBean {
            /**
             * specificId : a840ef8755d64414852208a4a5e2b302
             * goodsId : 12019050916160052589506788774405
             * goodsPrice : 200
             * goodsNumber : 200
             * specificName : 测试
             * goodTypeId : 9805bcf1a1dc4890994203bd25d13142
             * parentSpecificId : 0
             * specificPath : 测试
             */
            public ShopCarBean.GoodsListBean cloneShopCar() {
                ShopCarBean.GoodsListBean goodsListBean = new ShopCarBean.GoodsListBean();
                goodsListBean.setGoodsId(goodsId);
                //goodsListBean.setGoodsName(goodsName);
                goodsListBean.setGoodsNum(goodsNumber);
                //goodsListBean.setGoodsPicture(goodsPicture);
                goodsListBean.setGoodsPrice(goodsPrice);
                return goodsListBean;
            }

            private String specificId;
            private String goodsId;
            private float goodsPrice;
            private int goodsNumber;
            private String specificName;
            private String goodTypeId;
            private String parentSpecificId;
            private String specificPath;

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

            public int getGoodsNumber() {
                return goodsNumber;
            }

            public void setGoodsNumber(int goodsNumber) {
                this.goodsNumber = goodsNumber;
            }

            public String getSpecificName() {
                return specificName;
            }

            public void setSpecificName(String specificName) {
                this.specificName = specificName;
            }

            public String getGoodTypeId() {
                return goodTypeId;
            }

            public void setGoodTypeId(String goodTypeId) {
                this.goodTypeId = goodTypeId;
            }

            public String getParentSpecificId() {
                return parentSpecificId;
            }

            public void setParentSpecificId(String parentSpecificId) {
                this.parentSpecificId = parentSpecificId;
            }

            public String getSpecificPath() {
                return specificPath;
            }

            public void setSpecificPath(String specificPath) {
                this.specificPath = specificPath;
            }
        }

        public static class GoodSpecificTypeBean {
            /**
             * showPicture : 1
             * typeName : 外观
             * typeLevel : 1
             * specificInfoList : [{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"a840ef8755d64414852208a4a5e2b302","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":200,"specificName":"测试","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"},{"specificPicture":"9ea440be3f5bd7491056bf233409315b.jpg","specificId":"4f493a337cae41a49acb4d260dd21a05","goodsId":"12019050916160052589506788774405","goodsPrice":8888,"goodsNumber":10000,"specificName":"银色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"},{"specificPicture":"b5a01db8388786a36734a1a963794101.jpg","specificId":"4051d3447dbb438a89a3802fc518cfe6","goodsId":"12019050916160052589506788774405","goodsPrice":200,"goodsNumber":1000,"specificName":"黑色","goodTypeId":"9805bcf1a1dc4890994203bd25d13142","parentSpecificId":"0"}]
             * goodTypeId : 9805bcf1a1dc4890994203bd25d13142
             */

            private String showPicture;
            private String typeName;
            private String typeLevel;
            private String goodTypeId;
            private List<SpecificInfoListBean> specificInfoList;

            public String getShowPicture() {
                return showPicture;
            }

            public void setShowPicture(String showPicture) {
                this.showPicture = showPicture;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getTypeLevel() {
                return typeLevel;
            }

            public void setTypeLevel(String typeLevel) {
                this.typeLevel = typeLevel;
            }

            public String getGoodTypeId() {
                return goodTypeId;
            }

            public void setGoodTypeId(String goodTypeId) {
                this.goodTypeId = goodTypeId;
            }

            public List<SpecificInfoListBean> getSpecificInfoList() {
                return specificInfoList;
            }

            public void setSpecificInfoList(List<SpecificInfoListBean> specificInfoList) {
                this.specificInfoList = specificInfoList;
            }

            public static class SpecificInfoListBean {
                /**
                 * specificPicture : 9ea440be3f5bd7491056bf233409315b.jpg
                 * specificId : a840ef8755d64414852208a4a5e2b302
                 * goodsId : 12019050916160052589506788774405
                 * goodsPrice : 200
                 * goodsNumber : 200
                 * specificName : 测试
                 * goodTypeId : 9805bcf1a1dc4890994203bd25d13142
                 * parentSpecificId : 0
                 */

                private String specificPicture;
                private String specificId;
                private String goodsId;
                private float goodsPrice;
                private int goodsNumber;
                private String specificName;
                private String goodTypeId;
                private String parentSpecificId;

                public String getSpecificPicture() {
                    return specificPicture;
                }

                public void setSpecificPicture(String specificPicture) {
                    this.specificPicture = specificPicture;
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

                public int getGoodsNumber() {
                    return goodsNumber;
                }

                public void setGoodsNumber(int goodsNumber) {
                    this.goodsNumber = goodsNumber;
                }

                public String getSpecificName() {
                    return specificName;
                }

                public void setSpecificName(String specificName) {
                    this.specificName = specificName;
                }

                public String getGoodTypeId() {
                    return goodTypeId;
                }

                public void setGoodTypeId(String goodTypeId) {
                    this.goodTypeId = goodTypeId;
                }

                public String getParentSpecificId() {
                    return parentSpecificId;
                }

                public void setParentSpecificId(String parentSpecificId) {
                    this.parentSpecificId = parentSpecificId;
                }
            }
        }
    }
}
