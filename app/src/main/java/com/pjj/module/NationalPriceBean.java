package com.pjj.module;

import com.pjj.module.xspad.XspManage;

import java.util.List;

/**
 * Created by XinHeng on 2018/12/03.
 * describe：全国统一价
 */
public class NationalPriceBean extends ResultBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * peoplePrice : 29.0
         * price : 99.0
         * screenType : 1
         * blankPrice : 60.0
         */

        private double peoplePrice;
        private double price;
        //屏幕尺寸类型
        private String screenType;
        private double blankPrice;
        private double blankPeoplePrice;
        private double mediaPrice;

        public double getBlankPeoplePrice() {
            return blankPeoplePrice;
        }

        public void setBlankPeoplePrice(double blankPeoplePrice) {
            this.blankPeoplePrice = blankPeoplePrice;
        }

        public double getPeoplePrice() {
            return peoplePrice;
        }

        public void setPeoplePrice(double peoplePrice) {
            this.peoplePrice = peoplePrice;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getScreenType() {
            return screenType;
        }

        public void setScreenType(String screenType) {
            this.screenType = screenType;
        }

        public double getBlankPrice() {
            return blankPrice;
        }

        public void setBlankPrice(double blankPrice) {
            this.blankPrice = blankPrice;
        }

        public double getMediaPrice() {
            return mediaPrice;
        }

        public void setMediaPrice(double mediaPrice) {
            this.mediaPrice = mediaPrice;
        }
    }


    public double getPrice(String screenType) {
        String orderType = XspManage.getInstance().getAdType() + "";
        for (int i = 0; i < data.size(); i++) {
            DataBean dataBean = data.get(i);
            if (screenType != null && screenType.contains(screenType)) {
                //1 DIY类型 2便民信息 3diy填空传媒/随机 4便民随机
                if ("1".equals(orderType)) {
                    return dataBean.price;
                } else if ("2".equals(orderType)) {
                    return dataBean.peoplePrice;
                } else if ("3".equals(orderType)) {
                    return dataBean.blankPrice;
                } else if ("7".equals(orderType)) {
                    return dataBean.mediaPrice;
                } else {
                    return dataBean.blankPeoplePrice;
                }
            }
        }
        return -1;
    }
}
