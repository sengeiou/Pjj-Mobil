package com.pjj.module;

import java.util.List;

/**
 * Created by XinHeng on 2019/03/27.
 * describe：
 */
public class NewMediaScreenBean extends ResultBean {

    /**
     * data : {"mediaDiscount":1,"lng":"116.347534","assembleDiscount":1,"screenNum":"5","imgName":"communityBig.png","screenList":[{"assemblePrice":1,"screenName":"朗琴国际B座测试新媒体屏幕","finaNewlDiscount":1,"screenCode":"11010200001","mediaPrice":1,"screenId":"11111111111","price":1,"screenType":"3","isSplicing":"1","playTime":"1","finalMediaDiscount":1,"screenUrlName":"screenSmall.png","finalAssembleDiscount":1}],"areaCode":"110102","newMediaDiscount":1,"communityName":"朗琴国际B座","position":" 北京 北京市 西城区","communityId":"95","lat":"39.894571"}
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
         * mediaDiscount : 1
         * lng : 116.347534
         * assembleDiscount : 1
         * screenNum : 5
         * imgName : communityBig.png
         * screenList : [{"assemblePrice":1,"screenName":"朗琴国际B座测试新媒体屏幕","finaNewlDiscount":1,"screenCode":"11010200001","mediaPrice":1,"screenId":"11111111111","price":1,"screenType":"3","isSplicing":"1","playTime":"1","finalMediaDiscount":1,"screenUrlName":"screenSmall.png","finalAssembleDiscount":1}]
         * areaCode : 110102
         * newMediaDiscount : 1
         * communityName : 朗琴国际B座
         * position :  北京 北京市 西城区
         * communityId : 95
         * lat : 39.894571
         */

        private float mediaDiscount;
        private String lng;
        private float assembleDiscount;
        private String screenNum;
        private String imgName;
        private String areaCode;
        private float newMediaDiscount;
        private String communityName;
        private String position;
        private String communityId;
        private String lat;
        private List<ScreenListBean> screenList;

        public float getMediaDiscount() {
            return mediaDiscount;
        }

        public void setMediaDiscount(float mediaDiscount) {
            this.mediaDiscount = mediaDiscount;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public float getAssembleDiscount() {
            return assembleDiscount;
        }

        public void setAssembleDiscount(float assembleDiscount) {
            this.assembleDiscount = assembleDiscount;
        }

        public String getScreenNum() {
            return screenNum;
        }

        public void setScreenNum(String screenNum) {
            this.screenNum = screenNum;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public float getNewMediaDiscount() {
            return newMediaDiscount;
        }

        public void setNewMediaDiscount(float newMediaDiscount) {
            this.newMediaDiscount = newMediaDiscount;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public List<ScreenListBean> getScreenList() {
            return screenList;
        }

        public void setScreenList(List<ScreenListBean> screenList) {
            this.screenList = screenList;
        }

        public static class ScreenListBean {
            /**
             * assemblePrice : 1.0
             * screenName : 朗琴国际B座测试新媒体屏幕
             * finaNewlDiscount : 1.0
             * screenCode : 11010200001
             * mediaPrice : 1.0
             * screenId : 11111111111
             * price : 1.0
             * screenType : 3
             * isSplicing : 1
             * playTime : 1
             * finalMediaDiscount : 1.0
             * screenUrlName : screenSmall.png
             * finalAssembleDiscount : 1.0
             */

            private float assemblePrice;
            private String screenName;
            private float finaNewlDiscount;
            private String screenCode;
            private float mediaPrice;
            private float finalXspPrice;
            private String screenId;
            private float price;
            private String screenType;
            private String isSplicing;
            private String playTime;
            private float finalMediaDiscount;
            private String screenUrlName;
            private String cooperationMode;
            private String screenStatus;
            private int userCap;//最大数量
            private float finalAssembleDiscount;
            private boolean select;//对选需要

            public int getUserCap() {
                return userCap;
            }

            public void setUserCap(int userCap) {
                this.userCap = userCap;
            }

            public String getCooperationMode() {
                return cooperationMode;
            }

            public void setCooperationMode(String cooperationMode) {
                this.cooperationMode = cooperationMode;
            }

            public String getScreenStatus() {
                return screenStatus;
            }

            public void setScreenStatus(String screenStatus) {
                this.screenStatus = screenStatus;
            }

            public float getFinalXspPrice() {
                return finalXspPrice;
            }

            public void setFinalXspPrice(float finalXspPrice) {
                this.finalXspPrice = finalXspPrice;
            }

            public boolean isSelect() {
                return select;
            }

            public void setSelect(boolean select) {
                this.select = select;
            }

            public float getAssemblePrice() {
                return assemblePrice;
            }

            public void setAssemblePrice(float assemblePrice) {
                this.assemblePrice = assemblePrice;
            }

            public String getScreenName() {
                return screenName;
            }

            public void setScreenName(String screenName) {
                this.screenName = screenName;
            }

            public float getFinaNewlDiscount() {
                return finaNewlDiscount;
            }

            public void setFinaNewlDiscount(float finaNewlDiscount) {
                this.finaNewlDiscount = finaNewlDiscount;
            }

            public String getScreenCode() {
                return screenCode;
            }

            public void setScreenCode(String screenCode) {
                this.screenCode = screenCode;
            }

            public float getMediaPrice() {
                return mediaPrice;
            }

            public void setMediaPrice(float mediaPrice) {
                this.mediaPrice = mediaPrice;
            }

            public String getScreenId() {
                return screenId;
            }

            public void setScreenId(String screenId) {
                this.screenId = screenId;
            }

            public float getPrice() {
                return price;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public String getScreenType() {
                return screenType;
            }

            public void setScreenType(String screenType) {
                this.screenType = screenType;
            }

            public String getIsSplicing() {
                return isSplicing;
            }

            public void setIsSplicing(String isSplicing) {
                this.isSplicing = isSplicing;
            }

            public String getPlayTime() {
                return playTime;
            }

            public void setPlayTime(String playTime) {
                this.playTime = playTime;
            }

            public float getFinalMediaDiscount() {
                return finalMediaDiscount;
            }

            public void setFinalMediaDiscount(float finalMediaDiscount) {
                this.finalMediaDiscount = finalMediaDiscount;
            }

            public String getScreenUrlName() {
                return screenUrlName;
            }

            public void setScreenUrlName(String screenUrlName) {
                this.screenUrlName = screenUrlName;
            }

            public float getFinalAssembleDiscount() {
                return finalAssembleDiscount;
            }

            public void setFinalAssembleDiscount(float finalAssembleDiscount) {
                this.finalAssembleDiscount = finalAssembleDiscount;
            }
        }
    }
}
