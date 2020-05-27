package com.pjj.module;

import com.google.gson.annotations.SerializedName;
import com.pjj.PjjApplication;
import com.pjj.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import static com.pjj.module.ElevatorTimeBean.continuityHours;

/**
 * Created by XinHeng on 2018/12/07.
 * describe：
 */
public class OrderResultBean extends ResultBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderType : 1
         * canPlayElevator : [{"register":"adfads","eleName":"adff"}]
         * amount : 242.8
         * orderId : 20181206161404154408404458260769
         * communityList : [{"imgUrl":"http://47.92.254.65:8088/img/communityBig.png","elevatorNum":"4","blankDiscount":0.25,"communityName":"朗琴国际B座","position":" 北京 北京市 西城区","communityId":"95","blankPeopleDiscount":0.125,"screenNum":"4"}]
         * screenList : [{"screenId":"84_20_96_2d_9c_fb","peoplePrice":29,"price":99,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":0.225,"screenName":"朗琴国际二号梯A屏","position":" 北京,北京市,西城区","villageName":"朗琴国际B座","peopleDiscount":0.075,"eleName":"朗琴国际B座二号梯","screenCode":"11010200007"}]
         * playType : 0
         * createTime : 1544084044000
         * playDate : 2018-12-06,2018-12-07,2018-12-08
         * isPlay : 0
         * playTime : 1,3,4,6,8,9,10,11,18,19,20,21,22,23
         * authType : 1
         * zhiboUrl : 监播地址
         * status : 1
         */
        /**
         * 订单类型 orderType ：1 DIY类型 2便民信息 3填空diy  4填空便民
         */
        private String orderType;
        private double amount;
        private String orderId;
        private String playType;
        private long createTime;
        private String playDate;
        private String isPlay;
        private int communityNum;
        private String playTime;
        private String authType;
        @SerializedName("status")
        private String statusX;
        private String isTop;
        private List<CanPlayElevatorBean> canPlayElevator;
        private List<CommunityListBean> communityList;
        private  UserTempletBean.DataBean templet;
        private List<ScreenListBean> screenList;
        private int size = 0;
        private String zhiboUrl;
        private String name = "暂无";
        private String releaseName;
        private String screenType = "电梯";
        private String dateName = "发布日期";
        private String pieceType;//拼屏类型 pieceType : 0 不拼  1 拼屏幕  2拼时间段

        public String getIsTop() {
            return isTop;
        }

        public void setIsTop(String isTop) {
            this.isTop = isTop;
        }

        public int getCommunityNum() {
            return communityNum;
        }

        public void setCommunityNum(int communityNum) {
            this.communityNum = communityNum;
        }

        public UserTempletBean.DataBean getTemplet() {
            return templet;
        }

        public void setTemplet(UserTempletBean.DataBean templet) {
            this.templet = templet;
        }

        public String getPieceType() {
            return pieceType;
        }

        public void setPieceType(String pieceType) {
            this.pieceType = pieceType;
        }

        public int getSize() {
            return size;
        }

        public String getName() {
            return name;
        }

        public String getZhiboUrl() {
            return zhiboUrl;
        }

        public void setZhiboUrl(String zhiboUrl) {
            this.zhiboUrl = zhiboUrl;
        }

        public String getImgSrc() {
            if ("3".equals(orderType)) {
                if (TextUtils.isNotEmptyList(communityList)) {
                    size = communityList.size();
                    name = communityList.get(0).getCommunityName();
                    dateName = "起始日期";
                    releaseName = "随机DIY整版发布";
                    return PjjApplication.filePath + communityList.get(0).getImgName();
                }
            } else if ("5".equals(orderType)) {
                if (TextUtils.isNotEmptyList(communityList)) {
                    size = communityList.size();
                    name = communityList.get(0).getCommunityName();
                    dateName = "起始日期";
                    releaseName = "拼屏发布";
                    return PjjApplication.filePath + communityList.get(0).getImgName();
                }
            } else if ("4".equals(orderType)) {
                if (TextUtils.isNotEmptyList(communityList)) {
                    size = communityList.size();
                    name = communityList.get(0).getCommunityName();
                    dateName = "起始日期";
                    if ("1".equals(pieceType)) {
                        releaseName = "拼屏信息发布";
                    } else {
                        releaseName = "随机便民整版发布";
                    }
                    return PjjApplication.filePath + communityList.get(0).getImgName();
                }
            } else {
                if (TextUtils.isNotEmptyList(screenList)) {
                    size = screenList.size();
                    name = screenList.get(0).getVillageName();
                    releaseName = "1".equals(orderType) ? "DIY整版发布" : "便民整版发布";
                    if ("7".equals(orderType)) {
                        releaseName = "广告传媒发布";
                        screenType = "广告屏";
                    } else if ("9".equals(orderType)) {
                        releaseName = "电梯传媒发布";
                    }
                    return PjjApplication.filePath + screenList.get(0).getScreenUrlName();
                }
            }
            return null;
        }

        public String getReleaseName() {
            return releaseName;
        }

        public String getScreenType() {
            return screenType;
        }

        public String getReleaseTime() {
            if ("7".equals(orderType)) {
                return null;
            }
            if ("3".equals(orderType) || "4".equals(orderType)) {
                return playTime + "小时";
            }
            if (null != playTime) {
                if (TextUtils.isEmpty(playType)) {
                    return playTime + "小时";
                } else if ("2".equals(playType)) {
                    return playTime + "个月";
                } else if ("1".equals(playType)) {
                    return playTime + "周";
                }
                if (playTime.contains(":00")) {
                    return playTime;
                }
                String[] split = playTime.split(",");
                List<Integer> strings = new ArrayList<>();
                for (int i = 0; i < split.length; i++) {
                    int hour = -1;
                    try {
                        hour = Integer.parseInt(split[i]);
                        strings.add(hour);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
                return continuityHours(strings);
            }
            return "";
        }

        public String getDateName() {
            return dateName;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPlayType() {
            return playType;
        }

        public void setPlayType(String playType) {
            this.playType = playType;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getPlayDate() {
            if (null != playDate && playDate.contains("至")) {
                return playDate.replace("-", ".").replace("至", "-");
            }
            return playDate;
        }

        public void setPlayDate(String playDate) {
            this.playDate = playDate;
        }

        public String getIsPlay() {
            return isPlay;
        }

        public void setIsPlay(String isPlay) {
            this.isPlay = isPlay;
        }

        public String getPlayTime() {
            return playTime;
        }

        public void setPlayTime(String playTime) {
            this.playTime = playTime;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public List<CanPlayElevatorBean> getCanPlayElevator() {
            return canPlayElevator;
        }

        public void setCanPlayElevator(List<CanPlayElevatorBean> canPlayElevator) {
            this.canPlayElevator = canPlayElevator;
        }

        public List<CommunityListBean> getCommunityList() {
            return communityList;
        }

        public void setCommunityList(List<CommunityListBean> communityList) {
            this.communityList = communityList;
        }

        public List<ScreenListBean> getScreenList() {
            return screenList;
        }

        public void setScreenList(List<ScreenListBean> screenList) {
            this.screenList = screenList;
        }

        public static class CanPlayElevatorBean {
            /**
             * register : adfads
             * eleName : adff
             */

            private String register;
            @SerializedName(value = "eleName", alternate = {"screenName"})
            private String eleName;
            private String zhiboUrl;
            private String screenId;

            public String getScreenId() {
                return screenId;
            }

            public void setScreenId(String screenId) {
                this.screenId = screenId;
            }

            public String getRegister() {
                return register;
            }

            public void setRegister(String register) {
                this.register = register;
            }

            public String getEleName() {
                return eleName;
            }

            public void setEleName(String eleName) {
                this.eleName = eleName;
            }

            public String getZhiboUrl() {
                return zhiboUrl;
            }

            public void setZhiboUrl(String zhiboUrl) {
                this.zhiboUrl = zhiboUrl;
            }

            @Override
            public String toString() {
                return "CanPlayElevatorBean{" +
                        "register='" + register + '\'' +
                        ", eleName='" + eleName + '\'' +
                        ", zhiboUrl='" + zhiboUrl + '\'' +
                        '}';
            }
        }

        public static class CommunityListBean {
            /**
             * imgUrl : http://47.92.254.65:8088/img/communityBig.png
             * elevatorNum : 4
             * blankDiscount : 0.25
             * communityName : 朗琴国际B座
             * position :  北京 北京市 西城区
             * communityId : 95
             * blankPeopleDiscount : 0.125
             * screenNum : 4
             */

            private String imgUrl;
            private String imgName;
            private String elevatorNum;
            private double blankDiscount;
            private String communityName;
            private String position;
            private String communityId;
            private double blankPeopleDiscount;
            private String screenNum;

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getImgName() {
                return imgName;
            }

            public void setImgName(String imgName) {
                this.imgName = imgName;
            }

            public String getElevatorNum() {
                return elevatorNum;
            }

            public void setElevatorNum(String elevatorNum) {
                this.elevatorNum = elevatorNum;
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

            public String getCommunityId() {
                return communityId;
            }

            public void setCommunityId(String communityId) {
                this.communityId = communityId;
            }

            public double getBlankPeopleDiscount() {
                return blankPeopleDiscount;
            }

            public void setBlankPeopleDiscount(double blankPeopleDiscount) {
                this.blankPeopleDiscount = blankPeopleDiscount;
            }

            public String getScreenNum() {
                return screenNum;
            }

            public void setScreenNum(String screenNum) {
                this.screenNum = screenNum;
            }
        }

        public static class ScreenListBean {
            /**
             * screenId : 84_20_96_2d_9c_fb
             * peoplePrice : 29.0
             * price : 99.0
             * screenUrl : http://47.92.50.83:8080/showPic/screenSmall.png
             * screenType : 1
             * discount : 0.225
             * screenName : 朗琴国际二号梯A屏
             * position :  北京,北京市,西城区
             * villageName : 朗琴国际B座
             * peopleDiscount : 0.075
             * eleName : 朗琴国际B座二号梯
             * screenCode : 11010200007
             */

            private String screenId;
            private double peoplePrice;
            private double price;
            private String screenUrl;
            private String screenUrlName;
            private String screenType;
            private double discount;
            private String screenName;
            private String position;
            @SerializedName(value = "villageName", alternate = {"communityName"})
            private String villageName;
            private double peopleDiscount;
            private String eleName;
            private String screenCode;

            public String getScreenId() {
                return screenId;
            }

            public void setScreenId(String screenId) {
                this.screenId = screenId;
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

            public String getScreenUrl() {
                return screenUrl;
            }

            public void setScreenUrl(String screenUrl) {
                this.screenUrl = screenUrl;
            }

            public String getScreenUrlName() {
                return screenUrlName;
            }

            public void setScreenUrlName(String screenUrlName) {
                this.screenUrlName = screenUrlName;
            }

            public String getScreenType() {
                return screenType;
            }

            public void setScreenType(String screenType) {
                this.screenType = screenType;
            }

            public double getDiscount() {
                return discount;
            }

            public void setDiscount(double discount) {
                this.discount = discount;
            }

            public String getScreenName() {
                return screenName;
            }

            public void setScreenName(String screenName) {
                this.screenName = screenName;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getVillageName() {
                return villageName;
            }

            public void setVillageName(String villageName) {
                this.villageName = villageName;
            }

            public double getPeopleDiscount() {
                return peopleDiscount;
            }

            public void setPeopleDiscount(double peopleDiscount) {
                this.peopleDiscount = peopleDiscount;
            }

            public String getEleName() {
                return eleName;
            }

            public void setEleName(String eleName) {
                this.eleName = eleName;
            }

            public String getScreenCode() {
                return screenCode;
            }

            public void setScreenCode(String screenCode) {
                this.screenCode = screenCode;
            }
        }
    }
}
