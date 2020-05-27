package com.pjj.module;

import java.util.List;

/**
 * Create by xinheng on 2018/10/17。
 * describe：城市bean
 */
public class CityBean extends ResultBean{

    private List<CityListBean> cityList;

    public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityListBean {
        /**
         * isUse : 1
         * areaCode : 110100
         * areaName : 北京市
         */

        private String isUse;
        private String areaCode;
        private String areaName;
        private String cityName;
        /**
         * 首字母
         */
        private String firstWord;
        public String getIsUse() {
            return isUse;
        }

        public void setIsUse(String isUse) {
            this.isUse = isUse;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getFirstWord() {
            return firstWord;
        }

        public void setFirstWord(String firstWord) {
            this.firstWord = firstWord;
        }
    }
}
