package com.pjj.module;

import java.util.List;

public class ModuleMoreBean extends ResultBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bgPic : 80d4c31ee98085a8e6124c28e1fbd8b2.png
         * templetName : 生日祝福
         * high : 450
         * isFree : 1
         * wide : 270
         * adTempletId : daf06fdf947f44d095cc704c54a34e82
         * examplePic : 6c5638700273dc794dba3e01abb5e79d.png
         * theme : 生日祝福
         */
        private int res;//本地路径
        private String bgPic;//背景图
        private String templetName;
        private String high;
        private String isFree;//是否免费0 收费 1 免费
        private String wide;
        private String adTempletId;
        private String examplePic;//示范图
        private String theme;

        public int getRes() {
            return res;
        }

        public void setRes(int res) {
            this.res = res;
        }

        public String getBgPic() {
            return bgPic;
        }

        public void setBgPic(String bgPic) {
            this.bgPic = bgPic;
        }

        public String getTempletName() {
            return templetName;
        }

        public void setTempletName(String templetName) {
            this.templetName = templetName;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getIsFree() {
            return isFree;
        }

        public void setIsFree(String isFree) {
            this.isFree = isFree;
        }

        public String getWide() {
            return wide;
        }

        public void setWide(String wide) {
            this.wide = wide;
        }

        public String getAdTempletId() {
            return adTempletId;
        }

        public void setAdTempletId(String adTempletId) {
            this.adTempletId = adTempletId;
        }

        public String getExamplePic() {
            return examplePic;
        }

        public void setExamplePic(String examplePic) {
            this.examplePic = examplePic;
        }

        public String getTheme() {
            return theme;
        }

        public void setTheme(String theme) {
            this.theme = theme;
        }
    }
}
