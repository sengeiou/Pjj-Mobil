package com.pjj.module;

import com.pjj.module.xspad.XspManage;

import java.util.List;

/**
 * Created by XinHeng on 2018/12/03.
 * describe：
 */
public class BuildingBean extends ResultBean {
    private List<CommunityListBean> communityList;
    private List<CommunityListBean> data;

    public List<CommunityListBean> getData() {
        return data;
    }

    public void setData(List<CommunityListBean> data) {
        this.data = data;
    }

    public List<CommunityListBean> getCommunityList() {
        return communityList;
    }

    public void setCommunityList(List<CommunityListBean> communityList) {
        this.communityList = communityList;
    }

    public static class CommunityListBean {
        /**
         * imgUrl : http://47.92.254.65:8088/img/communityBig.png
         * areaCode : 110102
         * lng : 116.347534
         * elevatorNum : 5
         * blankDiscount : 0.25
         * discount : 0.45
         * screenType : ["1"]
         * communityName : 朗琴国际B座
         * position :  北京 北京市 西城区
         * communityId : 95
         * peopleDiscount : 0.5
         * lat : 39.894571
         */
        private float price;
        private String imgUrl;
        private String imgName;
        private String areaCode;
        private String lng;
        private String elevatorNum;
        private double blankDiscount;
        private double blankPeopleDiscount;
        private double discount;
        private String screenNum;
        private String communityName;
        private String position;
        private String communityId;
        private double peopleDiscount;
        private String lat;
        private List<String> screenType;

        public String getImgUrl() {
            //String fileName = RetrofitService.getInstance().getFileName(imgUrl);
            return imgUrl;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public double getRealDiscount() {
            int adType = XspManage.getInstance().getAdType();
            // if (adType == 1) {//1 DIY类型 2便民信息 3填空传媒
            switch (adType) {
                case 1:
                    return discount;
                case 2:
                    return peopleDiscount;
                case 3:
                    return blankDiscount;
                default:
                    return blankPeopleDiscount;
            }
        }

        public String getScreenNum() {
            return screenNum;
        }

        public void setScreenNum(String screenNum) {
            this.screenNum = screenNum;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getElevatorNum() {
            return elevatorNum;
        }

        public void setElevatorNum(String elevatorNum) {
            this.elevatorNum = elevatorNum;
        }

        public double getBlankDiscount() {
            return blankDiscount;
        }

        public void setBlankDiscount(double blankDiscount) {
            this.blankDiscount = blankDiscount;
        }

        public double getBlankPeopleDiscount() {
            return blankPeopleDiscount;
        }

        public void setBlankPeopleDiscount(double blankPeopleDiscount) {
            this.blankPeopleDiscount = blankPeopleDiscount;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
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

        public double getPeopleDiscount() {
            return peopleDiscount;
        }

        public void setPeopleDiscount(double peopleDiscount) {
            this.peopleDiscount = peopleDiscount;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public List<String> getScreenType() {
            return screenType;
        }

        public void setScreenType(List<String> screenType) {
            this.screenType = screenType;
        }

        public double getdiyDiscountPrice() {
            return price * discount;
        }
    }
}
