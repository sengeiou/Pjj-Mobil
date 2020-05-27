package com.pjj.module;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScreenBean extends ResultBean {
    /**
     * profitCount : 0
     * data : [{"cooperationMode":"3","connectStatus":"1","operationName":"15207621357","screenName":"天虹商场自用屏","screenCode":"11010200009","mediaPrice":0,"screenLocation":"2","isShowGroup":"0","screenId":"6c_21_a2_7d_f9_bd_test","propertyInfo":"物业信息","screenSize":"13.74","areaName":"北京市","closeTime":0,"screenType":"1","communityName":"天虹商场（北京宣武店）","openTime":0,"isActivate":"1"}]
     */

    private int profitCount;
    private List<DataBean> data;

    public int getProfitCount() {
        return profitCount;
    }

    public void setProfitCount(int profitCount) {
        this.profitCount = profitCount;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean extends SelectParent {
        /**
         * cooperationMode : 3
         * connectStatus : 1
         * operationName : 15207621357
         * screenName : 天虹商场自用屏
         * screenCode : 11010200009
         * mediaPrice : 0
         * screenLocation : 2
         * isShowGroup : 0
         * screenId : 6c_21_a2_7d_f9_bd_test
         * propertyInfo : 物业信息
         * screenSize : 13.74
         * areaName : 北京市
         * closeTime : 0
         * screenType : 1
         * communityName : 天虹商场（北京宣武店）
         * openTime : 0
         * isActivate : 1
         */

        private String cooperationMode;
        private String connectStatus;
        private String operationName;
        private String screenName;
        private String screenCode;
        private float mediaPrice;
        private float traditionPrice;
        private String screenLocation;
        @SerializedName(value = "isShow")
        private String isShowWuye;//是否显示物业信息   0不显示 1显示
        private String screenId;
        private String propertyInfo;
        private String screenSize;
        private String areaName;
        private int closeTime;
        private String screenType;
        private String communityName;
        private int openTime;
        private String isActivate;
        private String screenUrlName;
        private String switchingDate;

        public float getRealPrice() {
            if ("1".equals(screenLocation)) {//1电梯   2室内
                return traditionPrice;
            } else {
                return mediaPrice;
            }
        }

        public String getSwitchingDate() {
            return switchingDate;
        }

        public void setSwitchingDate(String switchingDate) {
            this.switchingDate = switchingDate;
        }

        public String getScreenUrlName() {
            return screenUrlName;
        }

        public float getTraditionPrice() {
            return traditionPrice;
        }

        public void setTraditionPrice(float traditionPrice) {
            this.traditionPrice = traditionPrice;
        }

        public void setScreenUrlName(String screenUrlName) {
            this.screenUrlName = screenUrlName;
        }

        public String getCooperationMode() {
            return cooperationMode;
        }

        public void setCooperationMode(String cooperationMode) {
            this.cooperationMode = cooperationMode;
        }

        public String getConnectStatus() {
            return connectStatus;
        }

        public void setConnectStatus(String connectStatus) {
            this.connectStatus = connectStatus;
        }

        public String getOperationName() {
            return operationName;
        }

        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
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

        public String getScreenLocation() {
            return screenLocation;
        }

        public void setScreenLocation(String screenLocation) {
            this.screenLocation = screenLocation;
        }

        public String getIsShowWuye() {
            return isShowWuye;
        }

        public void setIsShowWuye(String isShowWuye) {
            this.isShowWuye = isShowWuye;
        }

        public String getScreenId() {
            return screenId;
        }

        public void setScreenId(String screenId) {
            this.screenId = screenId;
        }

        public String getPropertyInfo() {
            return propertyInfo;
        }

        public void setPropertyInfo(String propertyInfo) {
            this.propertyInfo = propertyInfo;
        }

        public String getScreenSize() {
            return screenSize;
        }

        public void setScreenSize(String screenSize) {
            this.screenSize = screenSize;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public int getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(int closeTime) {
            this.closeTime = closeTime;
        }

        public String getScreenType() {
            return screenType;
        }

        public void setScreenType(String screenType) {
            this.screenType = screenType;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public int getOpenTime() {
            return openTime;
        }

        public void setOpenTime(int openTime) {
            this.openTime = openTime;
        }

        public String getIsActivate() {
            return isActivate;
        }

        public void setIsActivate(String isActivate) {
            this.isActivate = isActivate;
        }
    }
}
