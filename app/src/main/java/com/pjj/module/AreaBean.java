package com.pjj.module;

import java.util.List;

/**
 * Created by XinHeng on 2018/12/01.
 * describe：区
 */
public class AreaBean extends ResultBean {
    private List<CountyListBean> countyList;

    public List<CountyListBean> getCountyList() {
        return countyList;
    }

    public void setCountyList(List<CountyListBean> countyList) {
        this.countyList = countyList;
    }

    public static class CountyListBean {
        /**
         * isUse : 1
         * areaCode : 110101
         * areaName : 东城区
         */

        private String isUse;
        private String areaCode;
        private String areaName;

        public String getIsUse() {
            return isUse;
        }

        public void setIsUse(String isUse) {
            this.isUse = isUse;
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
    }
}
