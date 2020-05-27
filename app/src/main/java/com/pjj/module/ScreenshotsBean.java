package com.pjj.module;

import java.util.List;

/**
 * Created by XinHeng on 2019/04/01.
 * describe：
 */
public class ScreenshotsBean extends ResultBean {
    private String errInfo;
    private List<ImgsBean> imgs;

    public String getErrInfo() {
        return errInfo;
    }

    public void setErrInfo(String errInfo) {
        this.errInfo = errInfo;
    }

    public List<ImgsBean> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImgsBean> imgs) {
        this.imgs = imgs;
    }

    public static class ImgsBean {
        /**
         * screenId : 14_6b_9c_78_8b_54
         * cutTime : 1554105031000
         * orderId : 20190401112427155408906733630451
         * screenImgId : d3153ab9545211e9a45200163e0204d5
         * printScreen : 35b3b6bd4f7ecab748a093cc4c911b83.png
         */

        private String screenId;
        private long cutTime;
        private String orderId;
        private String screenImgId;
        private String printScreen;

        public String getScreenId() {
            return screenId;
        }

        public void setScreenId(String screenId) {
            this.screenId = screenId;
        }

        public long getCutTime() {
            return cutTime;
        }

        public void setCutTime(long cutTime) {
            this.cutTime = cutTime;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getScreenImgId() {
            return screenImgId;
        }

        public void setScreenImgId(String screenImgId) {
            this.screenImgId = screenImgId;
        }

        public String getPrintScreen() {
            return printScreen;
        }

        public void setPrintScreen(String printScreen) {
            this.printScreen = printScreen;
        }
    }
}
