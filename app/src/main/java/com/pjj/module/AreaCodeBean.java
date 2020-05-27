package com.pjj.module;

import com.pjj.utils.TextUtils;

import java.util.List;

/**
 * Created by XinHeng on 2018/12/01.
 * describe：城市编码
 */
public class AreaCodeBean extends ResultBean {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getCityCode() {
        if (TextUtils.isNotEmptyList(list)) {
            return list.get(0).areaCode;
        } else
            return null;
    }

    public static class ListBean {
        /**
         * isUse : 1
         * areaCode : 110100
         * areaName : 北京市
         * areaLevel : 2
         */

        private String isUse;
        private String areaCode;
        private String areaName;
        private String areaLevel;

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

        public String getAreaLevel() {
            return areaLevel;
        }

        public void setAreaLevel(String areaLevel) {
            this.areaLevel = areaLevel;
        }
    }
}
