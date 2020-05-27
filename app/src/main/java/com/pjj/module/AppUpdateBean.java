package com.pjj.module;

import java.util.List;

/**
 * Created by XinHeng on 2019/01/04.
 * describe：
 */
public class AppUpdateBean extends ResultBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * downName : 无
         * lowestVersionInt : 1
         * creatDate : 1536058117000
         * lowestVersion : 1.0.1
         * version : 1.0.1
         * info : 安卓更新内容
         * versionInt : 1
         */

        private String downName;
        private int lowestVersionInt;
        private long creatDate;
        private String lowestVersion;
        private String version;
        private String info;
        private int versionInt;

        public String getDownName() {
            return downName;
        }

        public void setDownName(String downName) {
            this.downName = downName;
        }

        public int getLowestVersionInt() {
            return lowestVersionInt;
        }

        public void setLowestVersionInt(int lowestVersionInt) {
            this.lowestVersionInt = lowestVersionInt;
        }

        public long getCreatDate() {
            return creatDate;
        }

        public void setCreatDate(long creatDate) {
            this.creatDate = creatDate;
        }

        public String getLowestVersion() {
            return lowestVersion;
        }

        public void setLowestVersion(String lowestVersion) {
            this.lowestVersion = lowestVersion;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getVersionInt() {
            return versionInt;
        }

        public void setVersionInt(int versionInt) {
            this.versionInt = versionInt;
        }
    }
}
