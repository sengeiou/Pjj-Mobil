package com.pjj.module;

import com.pjj.utils.TextUtils;

import java.util.List;

/**
 * Created by XinHeng on 2019/03/15.
 * describe：
 */
public class SpeedBean extends ResultBean {
    /**
     * data : {"datas9":[{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":0,"width":1,"y":2,"height":1},{"x":1,"width":1,"y":0,"height":1},{"x":1,"width":1,"y":1,"height":1},{"x":1,"width":1,"y":2,"height":1},{"x":2,"width":1,"y":0,"height":1},{"x":2,"width":1,"y":1,"height":1},{"x":2,"width":1,"y":2,"height":1}],"IdentificationId":"e50cc36df39c45088f913dcaadee5a3e","size":9,"proportionX":3,"proportionY":3},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"type":1,"height":1},{"x":0,"width":1,"y":1,"type":1,"height":1},{"x":0,"width":1,"y":2,"type":1,"height":1},{"x":0,"width":1,"y":3,"type":1,"height":1},{"x":0,"width":1,"y":4,"type":1,"height":1},{"x":0,"width":1,"y":5,"type":1,"height":1},{"x":0,"width":1,"y":6,"type":1,"height":1},{"x":0,"width":1,"y":7,"type":1,"height":1},{"x":0,"width":1,"y":8,"type":1,"height":1}],"IdentificationId":"dca76d0b59d04a17aeb6e40ab93b6d30","size":9,"proportionX":1,"proportionY":9}],"datas2":[{"viewSizeBeanList":[{"randomFlag":"1","x":0,"width":1,"y":0,"type":2,"height":1},{"x":0,"width":1,"y":1,"height":1}],"IdentificationId":"658448e350b646c7a45edca8a1dc6910","size":2,"proportionX":1,"proportionY":2},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"randomFlag":"1","x":0,"width":1,"y":1,"type":2,"height":1}],"IdentificationId":"39d6cb23a5764decafd1b09ecdf0780e","size":2,"proportionX":1,"proportionY":2},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"type":1,"height":1}],"IdentificationId":"d68caf33e68f4833beca06722ce719a6","size":2,"proportionX":1,"proportionY":2}],"datas4":[{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":0,"width":1,"y":2,"height":1},{"x":0,"width":1,"y":3,"height":1}],"IdentificationId":"da9b1188ee2945048031ec5c33494250","size":4,"proportionX":1,"proportionY":4},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":1,"width":1,"y":0,"height":1},{"x":1,"width":1,"y":1,"height":1}],"IdentificationId":"eb60e7bea07948b880b6c8164635f85c","size":4,"proportionX":2,"proportionY":2}],"datas3":[{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1,"randomFlag":"1","type":2},{"randomFlag":"1","x":0,"width":1,"y":1,"type":2,"height":1},{"x":0,"width":1,"y":2,"height":1}],"IdentificationId":"ec22b6de93b9440fb4c46fa72ea08da6","size":3,"proportionX":1,"proportionY":3},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"type":1,"height":1},{"x":0,"width":1,"y":2,"height":1}],"IdentificationId":"13f2730d3d7a465899c2e0bdfccb627c","size":3,"proportionX":1,"proportionY":3},{"viewSizeBeanList":[{"randomFlag":"1","x":0,"width":1,"y":0,"type":2,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":0,"width":1,"y":2,"height":1}],"IdentificationId":"f7ed8317d4344fb7b2e4e766f393abe6","size":3,"proportionX":1,"proportionY":3},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"height":1},{"randomFlag":"1","x":0,"width":1,"y":2,"type":2,"height":1}],"IdentificationId":"fe42f225c2fa429292f20a6b7e7ee22d","size":3,"proportionX":1,"proportionY":3}],"datas11":[{"viewSizeBeanList":[{"x":0,"width":3,"y":0,"type":1,"height":1},{"x":3,"width":3,"y":0,"type":1,"height":1},{"x":0,"width":2,"y":1,"type":1,"height":1},{"x":0,"width":2,"y":2,"type":1,"height":1},{"x":0,"width":2,"y":3,"type":1,"height":1},{"x":2,"width":2,"y":1,"type":1,"height":1},{"x":2,"width":2,"y":2,"type":1,"height":1},{"x":2,"width":2,"y":3,"type":1,"height":1},{"x":4,"width":2,"y":1,"type":1,"height":1},{"x":4,"width":2,"y":2,"type":1,"height":1},{"x":4,"width":2,"y":3,"type":1,"height":1}],"IdentificationId":"2bd9f969e4f747799bf6d9cb9b182848","size":11,"proportionX":6,"proportionY":4},{"viewSizeBeanList":[{"x":0,"width":2,"y":0,"type":1,"height":1},{"x":0,"width":2,"y":1,"type":1,"height":1},{"x":0,"width":2,"y":2,"type":1,"height":1},{"x":2,"width":2,"y":0,"type":1,"height":1},{"x":2,"width":2,"y":1,"type":1,"height":1},{"x":2,"width":2,"y":2,"type":1,"height":1},{"x":4,"width":2,"y":0,"type":1,"height":1},{"x":4,"width":2,"y":1,"type":1,"height":1},{"x":4,"width":2,"y":2,"type":1,"height":1},{"x":0,"width":3,"y":3,"type":1,"height":1},{"x":3,"width":3,"y":3,"type":1,"height":1}],"IdentificationId":"ca0bdc41cb324d8c8ccbfe7b1eb42029","size":11,"proportionX":6,"proportionY":4}],"datas6":[{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"type":1,"height":1},{"x":0,"width":1,"y":1,"type":1,"height":1},{"x":0,"width":1,"y":2,"type":1,"height":1},{"x":0,"width":1,"y":3,"type":1,"height":1},{"x":0,"width":1,"y":4,"type":1,"height":1},{"x":0,"width":1,"y":5,"type":1,"height":1}],"IdentificationId":"074a51deb8814388b81ee2f992afc13f","size":6,"proportionX":1,"proportionY":6},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":0,"width":1,"y":2,"height":1},{"x":1,"width":1,"y":0,"height":1},{"x":1,"width":1,"y":1,"height":1},{"x":1,"width":1,"y":2,"height":1}],"IdentificationId":"3dddd644e3bc42619549d33c2bfbadb2","size":6,"proportionX":2,"proportionY":3}],"datas5":[{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"type":1,"height":1},{"x":0,"width":1,"y":1,"type":1,"height":1},{"x":0,"width":1,"y":2,"type":1,"height":1},{"x":0,"width":1,"y":3,"type":1,"height":1},{"x":0,"width":1,"y":4,"type":1,"height":1}],"IdentificationId":"39b1681ea7e94b2d938646bf19cd1ada","size":5,"proportionX":1,"proportionY":5},{"viewSizeBeanList":[{"randomFlag":"1","x":0,"width":2,"y":0,"type":2,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":0,"width":1,"y":2,"height":1},{"x":1,"width":1,"y":1,"height":1},{"x":1,"width":1,"y":2,"height":1}],"IdentificationId":"8780ba7c49c2432db1860b8a481f9057","size":5,"proportionX":2,"proportionY":3},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":1,"width":1,"y":0,"height":1},{"x":1,"width":1,"y":1,"height":1},{"randomFlag":"1","x":0,"width":2,"y":2,"type":2,"height":1}],"IdentificationId":"e907073480374e71a83564ffa6a70793","size":5,"proportionX":2,"proportionY":3},{"viewSizeBeanList":[{"randomFlag":"1","x":0,"width":2,"y":0,"type":1,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":0,"width":1,"y":2,"height":1},{"x":1,"width":1,"y":1,"height":1},{"x":1,"width":1,"y":2,"height":1}],"IdentificationId":"daa369ace8ae408187e48a9dc0ef70f8","size":5,"proportionX":2,"proportionY":3},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":1,"width":1,"y":0,"height":1},{"x":1,"width":1,"y":1,"height":1},{"randomFlag":"1","x":0,"width":2,"y":2,"type":1,"height":1}],"IdentificationId":"2a0c5e3adc99450bbf5440c25b5bb83a","size":5,"proportionX":2,"proportionY":3}],"datas10":[{"viewSizeBeanList":[{"randomFlag":"1","x":0,"width":3,"y":0,"type":1,"height":3},{"x":0,"width":1,"y":3,"type":1,"height":2},{"x":0,"width":1,"y":5,"type":1,"height":2},{"x":0,"width":1,"y":7,"type":1,"height":2},{"x":1,"width":1,"y":3,"type":1,"height":2},{"x":1,"width":1,"y":5,"type":1,"height":2},{"x":1,"width":1,"y":7,"type":1,"height":2},{"x":2,"width":1,"y":3,"type":1,"height":2},{"x":2,"width":1,"y":5,"type":1,"height":2},{"x":2,"width":1,"y":7,"type":1,"height":2}],"IdentificationId":"b10db6ec4a1d4551be43de31330f3607","size":9,"proportionX":3,"proportionY":9},{"viewSizeBeanList":[{"randomFlag":"1","x":0,"width":3,"y":0,"type":2,"height":3},{"x":0,"width":1,"y":3,"type":1,"height":2},{"x":0,"width":1,"y":5,"type":1,"height":2},{"x":0,"width":1,"y":7,"type":1,"height":2},{"x":1,"width":1,"y":3,"type":1,"height":2},{"x":1,"width":1,"y":5,"type":1,"height":2},{"x":1,"width":1,"y":7,"type":1,"height":2},{"x":2,"width":1,"y":3,"type":1,"height":2},{"x":2,"width":1,"y":5,"type":1,"height":2},{"x":2,"width":1,"y":7,"type":1,"height":2}],"IdentificationId":"fc56ee7f65424907b4d6bac9eb9924a3","size":9,"proportionX":3,"proportionY":9},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":2},{"x":0,"width":1,"y":2,"height":2},{"x":0,"width":1,"y":4,"height":2},{"x":1,"width":1,"y":0,"height":2},{"x":1,"width":1,"y":2,"height":2},{"x":1,"width":1,"y":4,"height":2},{"x":2,"width":1,"y":0,"height":2},{"x":2,"width":1,"y":2,"height":2},{"x":2,"width":1,"y":4,"height":2},{"randomFlag":"1","x":0,"width":3,"y":6,"type":1,"height":3}],"IdentificationId":"d6c2172001bd4518aea16460f495ccfd","size":9,"proportionX":3,"proportionY":9},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":2},{"x":0,"width":1,"y":2,"height":2},{"x":0,"width":1,"y":4,"height":2},{"x":1,"width":1,"y":0,"height":2},{"x":1,"width":1,"y":2,"height":2},{"x":1,"width":1,"y":4,"height":2},{"x":2,"width":1,"y":0,"height":2},{"x":2,"width":1,"y":2,"height":2},{"x":2,"width":1,"y":4,"height":2},{"randomFlag":"1","x":0,"width":3,"y":6,"type":2,"height":3}],"IdentificationId":"7d2c50a1515b40c2860eed2988c20916","size":9,"proportionX":3,"proportionY":9},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"type":3,"height":1},{"x":0,"width":1,"y":1,"type":3,"height":1},{"x":0,"width":1,"y":2,"type":3,"height":1},{"x":0,"width":1,"y":3,"type":3,"height":1},{"x":0,"width":1,"y":4,"type":3,"height":1},{"x":0,"width":1,"y":5,"type":3,"height":1},{"x":0,"width":1,"y":6,"type":3,"height":1},{"x":0,"width":1,"y":7,"type":3,"height":1},{"x":0,"width":1,"y":8,"type":3,"height":1},{"x":0,"width":1,"y":9,"type":3,"height":1}],"IdentificationId":"248a1400e13e4b30ac1adf3b7abababc","size":10,"proportionX":1,"proportionY":10}],"datas8":[{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":1},{"x":0,"width":1,"y":1,"height":1},{"x":0,"width":1,"y":2,"height":1},{"x":0,"width":1,"y":3,"height":1},{"x":1,"width":1,"y":0,"height":1},{"x":1,"width":1,"y":1,"height":1},{"x":1,"width":1,"y":2,"height":1},{"x":1,"width":1,"y":3,"height":1}],"IdentificationId":"af5be6aae4aa4a568854f6bb995f6fd9","size":8,"proportionX":2,"proportionY":4},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"type":1,"height":1},{"x":0,"width":1,"y":1,"type":1,"height":1},{"x":0,"width":1,"y":2,"type":1,"height":1},{"x":0,"width":1,"y":3,"type":1,"height":1},{"x":0,"width":1,"y":4,"type":1,"height":1},{"x":0,"width":1,"y":5,"type":1,"height":1},{"x":0,"width":1,"y":6,"type":1,"height":1},{"x":0,"width":1,"y":7,"type":1,"height":1}],"IdentificationId":"330a839bcc2946218c4d322b27052b9a","size":8,"proportionX":1,"proportionY":8}],"datas12":[{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"type":1,"height":1},{"x":0,"width":1,"y":1,"type":1,"height":1},{"x":0,"width":1,"y":2,"type":1,"height":1},{"x":0,"width":1,"y":3,"type":1,"height":1},{"x":1,"width":1,"y":0,"type":1,"height":1},{"x":1,"width":1,"y":1,"type":1,"height":1},{"x":1,"width":1,"y":2,"type":1,"height":1},{"x":1,"width":1,"y":3,"type":1,"height":1},{"x":2,"width":1,"y":0,"type":1,"height":1},{"x":2,"width":1,"y":1,"type":1,"height":1},{"x":2,"width":1,"y":2,"type":1,"height":1},{"x":2,"width":1,"y":3,"type":1,"height":1}],"IdentificationId":"1224b33d28c546afbf73c8b952aeb6ca","size":12,"proportionX":3,"proportionY":4}],"datas7":[{"viewSizeBeanList":[{"randomFlag":"1","x":0,"width":2,"y":0,"type":2,"height":3},{"x":0,"width":1,"y":3,"height":2},{"x":0,"width":1,"y":5,"height":2},{"x":0,"width":1,"y":7,"height":2},{"x":1,"width":1,"y":3,"height":2},{"x":1,"width":1,"y":5,"height":2},{"x":1,"width":1,"y":7,"height":2}],"IdentificationId":"d4e70e144ce242b8ad18c2dc3da9e758","size":7,"proportionX":2,"proportionY":9},{"viewSizeBeanList":[{"randomFlag":"1","x":0,"width":2,"y":0,"type":1,"height":3},{"x":0,"width":1,"y":3,"height":2},{"x":0,"width":1,"y":5,"height":2},{"x":0,"width":1,"y":7,"height":2},{"x":1,"width":1,"y":3,"height":2},{"x":1,"width":1,"y":5,"height":2},{"x":1,"width":1,"y":7,"height":2}],"IdentificationId":"5f59204fdaaa48548ea93c6e9dcaf6e3","size":7,"proportionX":2,"proportionY":9},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":2},{"x":0,"width":1,"y":2,"height":2},{"x":0,"width":1,"y":4,"height":2},{"x":1,"width":1,"y":0,"height":2},{"x":1,"width":1,"y":2,"height":2},{"x":1,"width":1,"y":4,"height":2},{"randomFlag":"1","x":0,"width":2,"y":6,"type":2,"height":3}],"IdentificationId":"270f0ab6b08b431b84a7b0ffb2b77a98","size":7,"proportionX":2,"proportionY":9},{"viewSizeBeanList":[{"x":0,"width":1,"y":0,"height":2},{"x":0,"width":1,"y":2,"height":2},{"x":0,"width":1,"y":4,"height":2},{"x":1,"width":1,"y":0,"height":2},{"x":1,"width":1,"y":2,"height":2},{"x":1,"width":1,"y":4,"height":2},{"randomFlag":"1","x":0,"width":2,"y":6,"type":1,"height":3}],"IdentificationId":"17b404b672a34c3aa9fb65dd1211f93a","size":7,"proportionX":2,"proportionY":9}]}
     */

    private DateBean data;

    public DateBean getData() {
        return data;
    }

    public void setData(DateBean data) {
        this.data = data;
    }

    public static class DateBean {
        private List<SpeedDataBean> datas9;
        private List<SpeedDataBean> datas2;
        private List<SpeedDataBean> datas4;
        private List<SpeedDataBean> datas3;
        private List<SpeedDataBean> datas11;
        private List<SpeedDataBean> datas6;
        private List<SpeedDataBean> datas5;
        private List<SpeedDataBean> datas10;
        private List<SpeedDataBean> datas8;
        private List<SpeedDataBean> datas12;
        private List<SpeedDataBean> datas7;

        public List<SpeedDataBean> getDatas9() {
            return datas9;
        }

        public void setDatas9(List<SpeedDataBean> datas9) {
            this.datas9 = datas9;
        }

        public List<SpeedDataBean> getDatas2() {
            return datas2;
        }

        public void setDatas2(List<SpeedDataBean> datas2) {
            this.datas2 = datas2;
        }

        public List<SpeedDataBean> getDatas4() {
            return datas4;
        }

        public void setDatas4(List<SpeedDataBean> datas4) {
            this.datas4 = datas4;
        }

        public List<SpeedDataBean> getDatas3() {
            return datas3;
        }

        public void setDatas3(List<SpeedDataBean> datas3) {
            this.datas3 = datas3;
        }

        public List<SpeedDataBean> getDatas11() {
            return datas11;
        }

        public void setDatas11(List<SpeedDataBean> datas11) {
            this.datas11 = datas11;
        }

        public List<SpeedDataBean> getDatas6() {
            return datas6;
        }

        public void setDatas6(List<SpeedDataBean> datas6) {
            this.datas6 = datas6;
        }

        public List<SpeedDataBean> getDatas5() {
            return datas5;
        }

        public void setDatas5(List<SpeedDataBean> datas5) {
            this.datas5 = datas5;
        }

        public List<SpeedDataBean> getDatas10() {
            return datas10;
        }

        public void setDatas10(List<SpeedDataBean> datas10) {
            this.datas10 = datas10;
        }

        public List<SpeedDataBean> getDatas8() {
            return datas8;
        }

        public void setDatas8(List<SpeedDataBean> datas8) {
            this.datas8 = datas8;
        }

        public List<SpeedDataBean> getDatas12() {
            return datas12;
        }

        public void setDatas12(List<SpeedDataBean> datas12) {
            this.datas12 = datas12;
        }

        public List<SpeedDataBean> getDatas7() {
            return datas7;
        }

        public void setDatas7(List<SpeedDataBean> datas7) {
            this.datas7 = datas7;
        }

        public boolean isNotEmpty() {
            for (int i = 0; i < 11; i++) {
                List<SpeedDataBean> data = getData(i);

                if (TextUtils.isNotEmptyList(data)) {
                    return true;
                }
            }
            return false;
        }

        public List<SpeedDataBean> getData(int tag) {
            switch (tag) {
                case 0:
                    return getDatas2();
                case 1:
                    return getDatas3();
                case 2:
                    return getDatas4();
                case 3:
                    return getDatas5();
                case 4:
                    return getDatas6();
                case 5:
                    return getDatas7();
                case 6:
                    return getDatas8();
                case 7:
                    return getDatas9();
                case 8:
                    return getDatas10();
                case 9:
                    return getDatas11();
                default:
                    return getDatas12();
            }
        }
    }
}
