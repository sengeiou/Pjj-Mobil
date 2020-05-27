package com.pjj.module;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.pjj.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XinHeng on 2018/12/08.
 * describe：订单详情
 */
public class OrderInfBean extends ResultBean {
    /**
     * templetList : [{"templetName":"航拍","templet_id":"e1d7eb720dc94737ad414a6402a7d7fb","templetType":"2","fileList":[{"fileName":"27d493b2ac3b409793606095d0194236.mp4","type":"2","filePlace":"1"}]}]
     * orderInfo : {"orderType":"2","amount":11.31,"orderId":"20181208110652154423841216511082","peopleInfo":"某某品牌服装店开业啦，开业前三天之内，本店满180即可领取小礼品一份，机会难得，欲购从速。地址：西城区朗琴国际b座12层","verifyemark":"","peopleInfoTitle":"123","userId":"d22eafe692ec4321a8bb8ed7d539bfa9","isShowName":"1","createTime":1544238412000,"playType":"0","isShowPhone":"1","playDate":"2018-12-08","playTime":"14,16","authType":"1","status":"2"}
     * orderScreenList : [{"elevatorList":[{"screenList":[{"discount":0.45,"screenName":"朗琴国际二号梯B屏","villageName":"朗琴国际B座","screenCode":"11010200008","screenId":"10_a4_be_7f_50_ea","peoplePrice":29,"price":99,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","playTime":[{"orderTime":"2018-12-08","timeSection":",14,16,"}],"position":" 北京,北京市,西城区","peopleDiscount":0.25,"eleName":"朗琴国际B座二号梯"},{"discount":0.225,"screenName":"朗琴国际二号梯A屏","villageName":"朗琴国际B座","screenCode":"11010200007","screenId":"84_20_96_2d_9c_fb","peoplePrice":29,"price":99,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","playTime":[{"orderTime":"2018-12-08","timeSection":",14,16,"}],"position":" 北京,北京市,西城区","peopleDiscount":0.075,"eleName":"朗琴国际B座二号梯"}],"communityId":"95","eleName":"朗琴国际B座二号梯","register":"06594541637159225409"}],"communityName":"朗琴国际B座","position":" 北京 北京市 西城区","communityId":"95"}]
     */

    private OrderInfoBean orderInfo;
    private OrderInfoBean OrderData;
    private SpeedDataBean speliPicture;
    private List<DataBean> data;
    private List<UserTempletBean.DataBean> templetList;
    private List<OrderScreenListBean> orderScreenList;
    private int hours = -1;

    public int getHours() {
        return hours;
    }

    public List<OrderElevatorBean> getOrderElevatorBeanRandomList() {
        List<OrderElevatorBean> list = new ArrayList<>();
        if (TextUtils.isNotEmptyList(data)) {
            for (int i = 0; i < data.size(); i++) {
                DataBean dataBean = data.get(i);
                if (null != dataBean) {
                    List<DataBean.CommunityListBean> communityList = dataBean.getCommunityList();
                    if (TextUtils.isNotEmptyList(communityList)) {
                        for (int j = 0; j < communityList.size(); j++) {
                            DataBean.CommunityListBean communityListBean = communityList.get(j);
                            if (null != communityListBean) {
                                OrderElevatorBean bean = new OrderElevatorBean();
                                bean.setLevel(1);
                                bean.setTitle(communityListBean.getCommunityName());
                                int size = 0;
                                try {
                                    size = Integer.parseInt(communityListBean.getScreenNum());
                                } catch (NumberFormatException e) {
                                    // e.printStackTrace();
                                }
                                bean.setChildSize(size);
                                list.add(bean);
                            }
                        }
                    }
                }
            }

        }
        return list;
    }

    public List<OrderElevatorBean> getOrderElevatorBeanList() {
        List<OrderElevatorBean> list = new ArrayList<>();
        hours = 0;
        if (null != orderInfo) {
            String orderType = orderInfo.orderType;
            if ("3".equals(orderType) || "4".equals(orderType)) {
                String playlongtime = orderInfo.playlongtime;
                try {
                    hours = Integer.parseInt(playlongtime);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        if (TextUtils.isNotEmptyList(orderScreenList)) {
            for (int i = 0; i < orderScreenList.size(); i++) {
                OrderScreenListBean orderScreenListBean = orderScreenList.get(i);
                OrderElevatorBean bean = new OrderElevatorBean();
                bean.setLevel(1);
                bean.setTitle(orderScreenListBean.getCommunityName());
                list.add(bean);

                List<OrderScreenListBean.ElevatorListBean> elevatorList = orderScreenListBean.getElevatorList();
                int size_ = 0;
                if (TextUtils.isNotEmptyList(elevatorList)) {
                    for (int j = 0; j < elevatorList.size(); j++) {
                        OrderScreenListBean.ElevatorListBean elevatorListBean = elevatorList.get(j);
                        List<OrderScreenListBean.ElevatorListBean.ScreenListBean> screenList = elevatorListBean.getScreenList();
                        if (TextUtils.isNotEmptyList(screenList)) {
                            for (int k = 0; k < screenList.size(); k++) {
                                ++size_;
                                OrderScreenListBean.ElevatorListBean.ScreenListBean screenListBean = screenList.get(k);
                                OrderElevatorBean b1 = new OrderElevatorBean();
                                b1.setTitle(screenListBean.getScreenName());
                                b1.setLevel(2);
                                hours += screenListBean.getHours();
                                b1.setObjects(screenListBean);
                                list.add(b1);
                            }
                        }
                    }
                }
                bean.setChildSize(size_);
            }
        }
        return list;
    }

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public OrderInfoBean getOrderData() {
        return OrderData;
    }

    public void setOrderData(OrderInfoBean OrderData) {
        this.OrderData = OrderData;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    public List<UserTempletBean.DataBean> getTempletList() {
        return templetList;
    }

    public void setTempletList(List<UserTempletBean.DataBean> templetList) {
        this.templetList = templetList;
    }

    public List<OrderScreenListBean> getOrderScreenList() {
        return orderScreenList;
    }

    public void setOrderScreenList(List<OrderScreenListBean> orderScreenList) {
        this.orderScreenList = orderScreenList;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public SpeedDataBean getSpeliPicture() {
        return speliPicture;
    }

    public void setSpeliPicture(SpeedDataBean speliPicture) {
        this.speliPicture = speliPicture;
    }

    public static class OrderInfoBean {
        /**
         * orderType : 2
         * amount : 11.31
         * orderId : 20181208110652154423841216511082
         * peopleInfo : 某某品牌服装店开业啦，开业前三天之内，本店满180即可领取小礼品一份，机会难得，欲购从速。地址：西城区朗琴国际b座12层
         * verifyemark :
         * peopleInfoTitle : 123
         * userId : d22eafe692ec4321a8bb8ed7d539bfa9
         * isShowName : 1
         * createTime : 1544238412000
         * playType : 0
         * isShowPhone : 1
         * playDate : 2018-12-08
         * playTime : 14,16
         * authType : 1
         * status : 2
         */

        private String orderType;
        private String amount;
        private String orderId;
        private String peopleInfo;
        private String peopleInfoId;
        private String verifyemark;
        private String peopleInfoTitle;
        private String userId;
        private String isShowName;
        private long createTime;
        private String playType;
        private String isShowPhone;
        private String playlongtime;
        private String dayplaytime;
        private String playDate;
        private String playTime;
        private String authType;
        @SerializedName("status")
        private String statusX;

        private String pieceType;//拼屏类型 : 0 不拼 1 拼屏幕 2拼时间段
        private String pieceNum;//拼屏数
        private String pieceTitle;//便民信息分类
        private String pieceColour;//拼屏背景色


        public String getPlaylongtime() {
            return playlongtime;
        }

        public void setPlaylongtime(String playlongtime) {
            this.playlongtime = playlongtime;
        }

        public String getDayplaytime() {
            return dayplaytime;
        }

        public void setDayplaytime(String dayplaytime) {
            this.dayplaytime = dayplaytime;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getAmount() {
            return amount;
        }

        public String getPeopleInfoId() {
            return peopleInfoId;
        }

        public void setPeopleInfoId(String peopleInfoId) {
            this.peopleInfoId = peopleInfoId;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getPeopleInfo() {
            return peopleInfo;
        }

        public void setPeopleInfo(String peopleInfo) {
            this.peopleInfo = peopleInfo;
        }

        public String getVerifyemark() {
            return verifyemark;
        }

        public void setVerifyemark(String verifyemark) {
            this.verifyemark = verifyemark;
        }

        public String getPeopleInfoTitle() {
            return peopleInfoTitle;
        }

        public void setPeopleInfoTitle(String peopleInfoTitle) {
            this.peopleInfoTitle = peopleInfoTitle;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getIsShowName() {
            return isShowName;
        }

        public void setIsShowName(String isShowName) {
            this.isShowName = isShowName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getPlayType() {
            return playType;
        }

        public void setPlayType(String playType) {
            this.playType = playType;
        }

        public String getIsShowPhone() {
            return isShowPhone;
        }

        public void setIsShowPhone(String isShowPhone) {
            this.isShowPhone = isShowPhone;
        }

        public String getPlayDate() {
            return playDate;
        }

        public void setPlayDate(String playDate) {
            this.playDate = playDate;
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

        public String getPieceType() {
            return pieceType;
        }

        public void setPieceType(String pieceType) {
            this.pieceType = pieceType;
        }

        public String getPieceNum() {
            return pieceNum;
        }

        public void setPieceNum(String pieceNum) {
            this.pieceNum = pieceNum;
        }

        public String getPieceTitle() {
            return pieceTitle;
        }

        public void setPieceTitle(String pieceTitle) {
            this.pieceTitle = pieceTitle;
        }

        public String getPieceColour() {
            return pieceColour;
        }

        public void setPieceColour(String pieceColour) {
            this.pieceColour = pieceColour;
        }
    }


    public static class OrderScreenListBean {
        /**
         * elevatorList : [{"screenList":[{"discount":0.45,"screenName":"朗琴国际二号梯B屏","villageName":"朗琴国际B座","screenCode":"11010200008","screenId":"10_a4_be_7f_50_ea","peoplePrice":29,"price":99,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","playTime":[{"orderTime":"2018-12-08","timeSection":",14,16,"}],"position":" 北京,北京市,西城区","peopleDiscount":0.25,"eleName":"朗琴国际B座二号梯"},{"discount":0.225,"screenName":"朗琴国际二号梯A屏","villageName":"朗琴国际B座","screenCode":"11010200007","screenId":"84_20_96_2d_9c_fb","peoplePrice":29,"price":99,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","playTime":[{"orderTime":"2018-12-08","timeSection":",14,16,"}],"position":" 北京,北京市,西城区","peopleDiscount":0.075,"eleName":"朗琴国际B座二号梯"}],"communityId":"95","eleName":"朗琴国际B座二号梯","register":"06594541637159225409"}]
         * communityName : 朗琴国际B座
         * position :  北京 北京市 西城区
         * communityId : 95
         */

        private String communityName;
        private String position;
        private String communityId;
        private List<ElevatorListBean> elevatorList;

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

        public List<ElevatorListBean> getElevatorList() {
            return elevatorList;
        }

        public void setElevatorList(List<ElevatorListBean> elevatorList) {
            this.elevatorList = elevatorList;
        }

        public static class ElevatorListBean {
            /**
             * screenList : [{"discount":0.45,"screenName":"朗琴国际二号梯B屏","villageName":"朗琴国际B座","screenCode":"11010200008","screenId":"10_a4_be_7f_50_ea","peoplePrice":29,"price":99,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","playTime":[{"orderTime":"2018-12-08","timeSection":",14,16,"}],"position":" 北京,北京市,西城区","peopleDiscount":0.25,"eleName":"朗琴国际B座二号梯"},{"discount":0.225,"screenName":"朗琴国际二号梯A屏","villageName":"朗琴国际B座","screenCode":"11010200007","screenId":"84_20_96_2d_9c_fb","peoplePrice":29,"price":99,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","playTime":[{"orderTime":"2018-12-08","timeSection":",14,16,"}],"position":" 北京,北京市,西城区","peopleDiscount":0.075,"eleName":"朗琴国际B座二号梯"}]
             * communityId : 95
             * eleName : 朗琴国际B座二号梯
             * register : 06594541637159225409
             */

            private String communityId;
            private String eleName;
            private String register;
            private List<ScreenListBean> screenList;

            public String getCommunityId() {
                return communityId;
            }

            public void setCommunityId(String communityId) {
                this.communityId = communityId;
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
                 * discount : 0.45
                 * screenName : 朗琴国际二号梯B屏
                 * villageName : 朗琴国际B座
                 * screenCode : 11010200008
                 * screenId : 10_a4_be_7f_50_ea
                 * peoplePrice : 29.0
                 * price : 99.0
                 * screenUrl : http://47.92.50.83:8080/showPic/screenSmall.png
                 * screenType : 1
                 * playTime : [{"orderTime":"2018-12-08","timeSection":",14,16,"}]
                 * position :  北京,北京市,西城区
                 * peopleDiscount : 0.25
                 * eleName : 朗琴国际B座二号梯
                 */

                private double discount;
                private String screenName;
                private String villageName;
                private String screenCode;
                private String screenId;
                private double peoplePrice;
                private double price;
                private String screenUrl;
                private String screenType;
                private String position;
                private double peopleDiscount;
                private String eleName;
                private List<PlayTimeBean> playTime;
                //public List<ElevatorTimeBean.DayHours> dayHours;
                private List<ElevatorTimeBean.DayHours> dayHours;
                private int hours = -1;

                public int getHours() {
                    if (hours == -1) {
                        dayHours = getDayHours1();
                    }
                    return hours;
                }

                public List<ElevatorTimeBean.DayHours> getDayHours() {
                    return dayHours;
                }

                private List<ElevatorTimeBean.DayHours> getDayHours1() {
                    hours = 0;
                    List<ElevatorTimeBean.DayHours> list = new ArrayList<>();
                    if (TextUtils.isNotEmptyList(playTime)) {
                        for (int i = 0; i < playTime.size(); i++) {
                            ElevatorTimeBean.DayHours dayHours = new ElevatorTimeBean.DayHours();
                            PlayTimeBean playTimeBean = playTime.get(i);
                            dayHours.setDate(playTimeBean.orderTime);
                            String timeSection = playTimeBean.timeSection;
                            if (",".equals(timeSection.charAt(0) + "")) {
                                timeSection = timeSection.substring(1, timeSection.length());
                            }
                            String[] split = timeSection.split(",");
                            Log.e("TAG", "getDayHours: " + split.length);
                            ArrayList<Integer> objects = new ArrayList<>();
                            for (int j = 0; j < split.length; j++) {
                                int anInt = -1;
                                try {
                                    anInt = Integer.parseInt(split[j]);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (anInt > -1) {
                                    ++hours;
                                    objects.add(anInt);
                                }
                            }
                            dayHours.setHours(ElevatorTimeBean.continuityHours(objects));
                            list.add(dayHours);
                        }
                    }
                    return list;
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

                public String getVillageName() {
                    return villageName;
                }

                public void setVillageName(String villageName) {
                    this.villageName = villageName;
                }

                public String getScreenCode() {
                    return screenCode;
                }

                public void setScreenCode(String screenCode) {
                    this.screenCode = screenCode;
                }

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

                public String getScreenType() {
                    return screenType;
                }

                public void setScreenType(String screenType) {
                    this.screenType = screenType;
                }

                public String getPosition() {
                    return position;
                }

                public void setPosition(String position) {
                    this.position = position;
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

                public List<PlayTimeBean> getPlayTime() {
                    return playTime;
                }

                public void setPlayTime(List<PlayTimeBean> playTime) {
                    this.playTime = playTime;
                }

                public static class PlayTimeBean {
                    /**
                     * orderTime : 2018-12-08
                     * timeSection : ,14,16,
                     */

                    private String orderTime;
                    private String timeSection;

                    public String getOrderTime() {
                        return orderTime;
                    }

                    public void setOrderTime(String orderTime) {
                        this.orderTime = orderTime;
                    }

                    public String getTimeSection() {
                        return timeSection;
                    }

                    public void setTimeSection(String timeSection) {
                        this.timeSection = timeSection;
                    }
                }
            }
        }
    }

    public static class DataBean {
        /**
         * communityList : [{"communityUrl":"http://47.92.254.65:8088/img/communityBig.png","elevatorNum":"4","blankDiscount":0.25,"communityName":"朗琴国际B座","communityId":"95","blankPeopleDiscount":0.125,"screenNum":"5"}]
         * position :  北京 北京市 西城区
         */

        private String position;
        private List<DataBean.CommunityListBean> communityList;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public List<DataBean.CommunityListBean> getCommunityList() {
            return communityList;
        }

        public void setCommunityList(List<DataBean.CommunityListBean> communityList) {
            this.communityList = communityList;
        }

        public static class CommunityListBean {
            /**
             * communityUrl : http://47.92.254.65:8088/img/communityBig.png
             * imgName : communityBig.png
             * elevatorNum : 4
             * blankDiscount : 0.25
             * communityName : 朗琴国际B座
             * communityId : 95
             * blankPeopleDiscount : 0.125
             * screenNum : 5
             */

            private String communityUrl;
            private String imgName;
            private String elevatorNum;
            private double blankDiscount;
            private String communityName;
            private String communityId;
            private double blankPeopleDiscount;
            private String screenNum;

            public String getCommunityUrl() {
                return communityUrl;
            }

            public void setCommunityUrl(String communityUrl) {
                this.communityUrl = communityUrl;
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
    }
}
