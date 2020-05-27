package com.pjj.module;

import com.pjj.module.xspad.XspManage;
import com.pjj.utils.TextUtils;

import java.util.List;

public class ElevatorBean extends ResultBean {

    /**
     * data : {"elevatorList":[{"screenList":[{"screenId":"000000067","peoplePrice":20,"price":899,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"2","discount":1,"screenName":"朗琴国际二号梯A屏","peopleDiscount":0.9,"screenCode":"11010200006"}],"eleName":"朗琴国际B座二号梯","register":"06594541637159225409"},{"screenList":[],"eleName":"朗琴国际B座三号梯","register":"15425081254857257820"},{"screenList":[],"eleName":"朗琴国际B座货梯","register":"15425081768120970244"},{"screenList":[{"screenId":"000000087","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯A屏","peopleDiscount":0.9,"screenCode":"11010200001"},{"screenId":"test02","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯B屏","peopleDiscount":0.9,"screenCode":"11010200004"},{"screenId":"test03","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯C屏","peopleDiscount":0.9,"screenCode":"11010200005"}],"eleName":"朗琴国际B座一号梯","register":"31110520100578531993"}],"lng":"116.347534","discount":0.8,"imgUrl":"http://47.92.50.83:8080/showPic/communityBig.png","areaCode":"110102","peoplePrice":50,"price":200,"elevatorNum":"4","blankDiscount":0.6,"communityName":"朗琴国际B座","position":" 北京 北京市 西城区","blankPrice":80,"communityId":"95","peopleDiscount":0.7,"lat":"39.894571"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * elevatorList : [{"screenList":[{"screenId":"000000067","peoplePrice":20,"price":899,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"2","discount":1,"screenName":"朗琴国际二号梯A屏","peopleDiscount":0.9,"screenCode":"11010200006"}],"eleName":"朗琴国际B座二号梯","register":"06594541637159225409"},{"screenList":[],"eleName":"朗琴国际B座三号梯","register":"15425081254857257820"},{"screenList":[],"eleName":"朗琴国际B座货梯","register":"15425081768120970244"},{"screenList":[{"screenId":"000000087","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯A屏","peopleDiscount":0.9,"screenCode":"11010200001"},{"screenId":"test02","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯B屏","peopleDiscount":0.9,"screenCode":"11010200004"},{"screenId":"test03","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯C屏","peopleDiscount":0.9,"screenCode":"11010200005"}],"eleName":"朗琴国际B座一号梯","register":"31110520100578531993"}]
         * lng : 116.347534
         * discount : 0.8
         * imgUrl : http://47.92.50.83:8080/showPic/communityBig.png
         * areaCode : 110102
         * peoplePrice : 50.0
         * price : 200.0
         * elevatorNum : 4
         * blankDiscount : 0.6
         * communityName : 朗琴国际B座
         * position :  北京 北京市 西城区
         * blankPrice : 80.0
         * communityId : 95
         * peopleDiscount : 0.7
         * lat : 39.894571
         */

        private String lng;
        private double discount;
        private String imgUrl;
        private String imgName;
        private String areaCode;
        private double peoplePrice;
        private double price;
        private String elevatorNum;
        private String screenNum;

        private double blankDiscount;
        private String communityName;
        private String position;
        private double blankPrice;
        private String communityId;
        private double peopleDiscount;
        private String lat;
        private List<ElevatorListBean> elevatorList;

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
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

        public String getElevatorNum() {
            return elevatorNum;
        }

        public void setElevatorNum(String elevatorNum) {
            this.elevatorNum = elevatorNum;
        }

        public String getScreenNum() {
            return screenNum;
        }

        public void setScreenNum(String screenNum) {
            this.screenNum = screenNum;
        }

        public double getBlankDiscount() {
            return blankDiscount;
        }

        public void setBlankDiscount(double blankDiscount) {
            this.blankDiscount = blankDiscount;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public double getBlankPrice() {
            return blankPrice;
        }

        public void setBlankPrice(double blankPrice) {
            this.blankPrice = blankPrice;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public double getPeopleDiscount() {
            return peopleDiscount;
        }

        public void setPeopleDiscount(double peopleDiscount) {
            this.peopleDiscount = peopleDiscount;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public List<ElevatorListBean> getElevatorList() {
            return elevatorList;
        }

        public void setElevatorList(List<ElevatorListBean> elevatorList) {
            this.elevatorList = elevatorList;
        }

        public static class ElevatorListBean {
            /**
             * screenList : [{"screenId":"000000067","peoplePrice":20,"price":899,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"2","discount":1,"screenName":"朗琴国际二号梯A屏","peopleDiscount":0.9,"screenCode":"11010200006"}]
             * eleName : 朗琴国际B座二号梯
             * register : 06594541637159225409
             */

            private String eleName;
            private String register;
            private List<ScreenListBean> screenList;
            private int size;


            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getEleName() {
                return eleName;
            }

            public void setEleName(String eleName) {
                this.eleName = eleName;
            }

            public String getRegister() {
                return register;
            }

            public void setRegister(String register) {
                this.register = register;
            }

            public List<ScreenListBean> getScreenList() {
                return screenList;
            }

            public void setScreenList(List<ScreenListBean> screenList) {
                this.screenList = screenList;
            }

            public static class ScreenListBean {
                /**
                 * screenId : 000000067
                 * peoplePrice : 20.0
                 * price : 899.0
                 * screenUrl : http://47.92.50.83:8080/showPic/screenSmall.png
                 * screenType : 2
                 * discount : 1.0
                 * screenName : 朗琴国际二号梯A屏
                 * peopleDiscount : 0.9
                 * screenCode : 11010200006
                 */

                private String screenId;
                private float peoplePrice;
                private float price;
                private float traditionPrice;//传统模式价格
                private float finalTraditionDiscount;//传统模式最终折扣折扣
                private String cooperationMode;//合作运营模式 1地方自营 2联合运营 3 公司运营 4自用
                private String screenUrl;
                private String screenType;
                private float discount;
                private String screenName;
                private float peopleDiscount;
                private String screenCode;

                public float getFinalTraditionDiscount() {
                    return finalTraditionDiscount;
                }

                public void setFinalTraditionDiscount(float finalTraditionDiscount) {
                    this.finalTraditionDiscount = finalTraditionDiscount;
                }

                public String getCooperationMode() {
                    return cooperationMode;
                }

                public void setCooperationMode(String cooperationMode) {
                    this.cooperationMode = cooperationMode;
                }

                public float getTraditionPrice() {
                    return traditionPrice;
                }

                public void setTraditionPrice(float traditionPrice) {
                    this.traditionPrice = traditionPrice;
                }

                public String getScreenId() {
                    return screenId;
                }

                public void setScreenId(String screenId) {
                    this.screenId = screenId;
                }

                public float getPeoplePrice() {
                    return peoplePrice;
                }

                public void setPeoplePrice(float peoplePrice) {
                    this.peoplePrice = peoplePrice;
                }

                public float getPrice() {
                    return price;
                }

                public void setPrice(float price) {
                    this.price = price;
                }

                public String getScreenUrl() {
                    return screenUrl;
                }

                public void setScreenUrl(String screenUrl) {
                    this.screenUrl = screenUrl;
                }

                public String getScreenType() {
                    return screenType;
                }

                public void setScreenType(String screenType) {
                    this.screenType = screenType;
                }

                public float getDiscount() {
                    return discount;
                }

                public void setDiscount(float discount) {
                    this.discount = discount;
                }

                public String getScreenName() {
                    return screenName;
                }

                public void setScreenName(String screenName) {
                    this.screenName = screenName;
                }

                public float getPeopleDiscount() {
                    return peopleDiscount;
                }

                public void setPeopleDiscount(float peopleDiscount) {
                    this.peopleDiscount = peopleDiscount;
                }

                public String getScreenCode() {
                    return screenCode;
                }

                public void setScreenCode(String screenCode) {
                    this.screenCode = screenCode;
                }

                public boolean equals(ScreenListBean obj) {
                    return screenId.equals(obj.getScreenId());
                }

                public int beIndexOf(List<ScreenListBean> list) {
                    if (!TextUtils.isNotEmptyList(list)) {
                        return -1;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (equals(list.get(i))) {
                            return i;
                        }
                    }
                    return -1;
                }

                public float getdiyDiscountPrice() {
                    return price * discount;
                }

                public float getBmDiscountPrice() {
                    return peoplePrice * peopleDiscount;
                }

                /**
                 * 仅适用于 diy与便民
                 *
                 * @return
                 */
                public float getDiscountPrice() {
                    return XspManage.getInstance().getAdType() == 1 ? getdiyDiscountPrice() : getBmDiscountPrice();
                }
            }
        }
    }
}
