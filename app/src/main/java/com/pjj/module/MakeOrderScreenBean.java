package com.pjj.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XinHeng on 2018/12/25.
 * describe：
 */
public class MakeOrderScreenBean extends ResultBean {
    /**
     * orderId : 20181225114428154570946836891269
     */
    @SerializedName(value = "orderId", alternate = {"goodOrderId"})
    private String orderId;

    private MakeOrderData data;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MakeOrderData getData() {
        return data;
    }

    public void setData(MakeOrderData data) {
        this.data = data;
    }

    public static class MakeOrderData {
        private List<ScreenBean> useFullScreen;
        private List<ScreenBean> offLineScreen;

        public List<ScreenBean> getUseFullScreen() {
            return useFullScreen;
        }

        public void setUseFullScreen(List<ScreenBean> useFullScreen) {
            this.useFullScreen = useFullScreen;
        }

        public List<ScreenBean> getOffLineScreen() {
            return offLineScreen;
        }

        public void setOffLineScreen(List<ScreenBean> offLineScreen) {
            this.offLineScreen = offLineScreen;
        }
    }

    public static class ScreenBean {
        private String screenId;
        private String screenName;
        private String orderTime;

        private List<String> dates;

        public ScreenBean clone() {
            ScreenBean bea = new ScreenBean();
            bea.setScreenId(screenId);
            bea.setScreenName(screenName);
            return bea;
        }

        public List<String> getDates() {
            return dates;
        }

        public void setDates(List<String> dates) {
            this.dates = dates;
        }

        public String getScreenId() {
            return screenId;
        }

        public void setScreenId(String screenId) {
            this.screenId = screenId;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }
    }
}
