package com.pjj.module;

import com.pjj.module.xspad.XspManage;
import com.pjj.utils.TextUtils;
import com.pjj.view.adapter.OrderElevatorInfAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BuildingElevatorBean extends ResultBean {

    /**
     * data : {"elevatorList":[{"screenList":[{"screenId":"000000067","peoplePrice":20,"price":899,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"2","discount":1,"screenName":"朗琴国际二号梯A屏","peopleDiscount":0.9,"screenCode":"11010200006"}],"eleName":"朗琴国际B座二号梯","register":"06594541637159225409"},{"screenList":[],"eleName":"朗琴国际B座三号梯","register":"15425081254857257820"},{"screenList":[],"eleName":"朗琴国际B座货梯","register":"15425081768120970244"},{"screenList":[{"screenId":"000000087","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯A屏","peopleDiscount":0.9,"screenCode":"11010200001"},{"screenId":"test02","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯B屏","peopleDiscount":0.9,"screenCode":"11010200004"},{"screenId":"test03","peoplePrice":20,"price":799,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"1","discount":1,"screenName":"朗琴国际一号梯C屏","peopleDiscount":0.9,"screenCode":"11010200005"}],"eleName":"朗琴国际B座一号梯","register":"31110520100578531993"}],"lng":"116.347534","discount":0.8,"imgUrl":"http://47.92.50.83:8080/showPic/communityBig.png","areaCode":"110102","peoplePrice":50,"price":200,"elevatorNum":"4","blankDiscount":0.6,"communityName":"朗琴国际B座","position":" 北京 北京市 西城区","blankPrice":80,"communityId":"95","peopleDiscount":0.7,"lat":"39.894571"}
     */

    private List<DataBean> data;
    private List<DataBean> hotCommunityList;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<DataBean> getHotCommunityList() {
        return hotCommunityList;
    }

    public void setHotCommunityList(List<DataBean> hotCommunityList) {
        this.hotCommunityList = hotCommunityList;
    }

    public void refresh(List<DataBean> data) {
        if (TextUtils.isNotEmptyList(data)) {
            for (int i = 0; i < data.size(); i++) {
                DataBean build = data.get(i);
                List<DataBean.ElevatorListBean> elevatorList = build.getElevatorList();
                boolean buildClickTag = false;
                if (TextUtils.isNotEmptyList(elevatorList)) {
                    for (int j = 0; j < elevatorList.size(); j++) {
                        DataBean.ElevatorListBean elevator = elevatorList.get(j);
                        List<DataBean.ElevatorListBean.ScreenListBean> screenList = elevator.getScreenList();
                        boolean elevatorClickTag = false;
                        if (TextUtils.isNotEmptyList(screenList)) {
                            for (int k = 0; k < screenList.size(); k++) {
                                DataBean.ElevatorListBean.ScreenListBean screen = screenList.get(k);
                                if (!screen.screenStatus.equals("2")) {
                                    elevatorClickTag = true;
                                    buildClickTag = true;
                                    screen.setCanClick(true);
                                } else {
                                    screen.setCanClick(false);
                                }
                            }
                        }
                        elevator.setCanClick(elevatorClickTag);
                    }
                }
                build.setCanClick(buildClickTag);
            }
        }
    }

    public static class DataBean extends CanClick {
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
        private float price;
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
        private boolean isShow;
        private boolean isSelect;


        public OrderElevatorInfAdapter.OrderElevatorInfParent cloneInf(int elevatorSize, int screenSize, float price) {
            return new OrderElevatorInfAdapter.OrderElevatorInfParent() {
                @Nullable
                @Override
                public String getBuildingImageName() {
                    return TextUtils.clean(imgName);
                }

                @Nullable
                @Override
                public String getBuildingName() {
                    return TextUtils.clean(communityName);
                }

                @Nullable
                @Override
                public int getElevatorCount() {
                    return elevatorSize;
                }

                @Nullable
                @Override
                public int getScreenCount() {
                    return screenSize;
                }

                @Override
                public float getPrice() {
                    return price;
                }
            };
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

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

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
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

        public static class ElevatorListBean extends CanClick {
            /**
             * screenList : [{"screenId":"000000067","peoplePrice":20,"price":899,"screenUrl":"http://47.92.50.83:8080/showPic/screenSmall.png","screenType":"2","discount":1,"screenName":"朗琴国际二号梯A屏","peopleDiscount":0.9,"screenCode":"11010200006"}]
             * eleName : 朗琴国际B座二号梯
             * register : 06594541637159225409
             */

            private String eleName;
            private String register;
            private List<ScreenListBean> screenList;
            private int size;
            private int screenNum;
            private int canUseCount;
            private int selectCount;
            private boolean isShow;
            private boolean isSelect;

            public int getCanUseCount() {
                return canUseCount;
            }

            @Override
            public String toString() {
                return canUseCount + "," + selectCount;
            }

            public void setCanUseCount(int canUseCount) {
                this.canUseCount = canUseCount;
            }

            public int getSelectCount() {
                return selectCount;
            }

            public void setSelectCount(int selectCount) {
                this.selectCount = selectCount;
            }

            public boolean isShow() {
                return isShow;
            }

            public void setShow(boolean show) {
                isShow = show;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public int getScreenNum() {
                return screenNum;
            }

            public void setScreenNum(int screenNum) {
                this.screenNum = screenNum;
            }

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

            public static class ScreenListBean extends CanClick {
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
                private boolean isSelect;
                private String screenId;
                private float peoplePrice;
                private float price;
                private float traditionPrice;//传统模式价格
                private float finalTraditionDiscount;//传统模式最终折扣折扣
                private String cooperationMode;//合作运营模式 1地方自营 2联合运营 3 公司运营 4自用
                private String screenStatus;
                private String screenUrl;
                private String screenType;
                private float discount;
                private float finalXspPrice;
                private String screenName;
                private float peopleDiscount;
                private String screenCode;
                private int userCap;//最大数量

                public int getUserCap() {
                    return userCap;
                }

                public void setUserCap(int userCap) {
                    this.userCap = userCap;
                }

                public boolean isSelect() {
                    return isSelect;
                }

                public void setSelect(boolean select) {
                    isSelect = select;
                }

                public String getScreenStatus() {
                    return screenStatus;
                }

                public void setScreenStatus(String screenStatus) {
                    this.screenStatus = screenStatus;
                }

                public float getFinalXspPrice() {
                    return finalXspPrice;
                }

                public void setFinalXspPrice(float finalXspPrice) {
                    this.finalXspPrice = finalXspPrice;
                }

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

    public static List<DataBean> filterAdd(List<DataBean> communityList, List<DataBean> listBeans, String name) {
        if (TextUtils.isNotEmptyList(communityList)) {
            List<DataBean> communityListOther = new ArrayList<>();
            List<DataBean> communityNewList = new ArrayList();
            Iterator<DataBean> iterator = communityList.iterator();
            while (iterator.hasNext()) {
                DataBean next = iterator.next();
                if (next != null && next.communityName != null && next.communityName.contains(name)) {
                    communityNewList.add(next);
                } else {
                    communityListOther.add(next);
                }
            }
            String tag = "暂无更多搜索结果，为您推荐热门大厦";
            if (communityNewList.size() == 0) {
                //communityNewList.add(new DataBean());
                tag = "暂无资源，为您推荐热门大厦";
            }
            listBeans.addAll(communityNewList);
            if (communityListOther.size() > 0) {
                DataBean e = new DataBean();
                e.setAreaCode(tag);
                listBeans.add(e);
                listBeans.addAll(communityListOther);
            }
            return communityNewList;
        }
        return null;
    }

    public static class CanClick {
        private boolean canClick;

        public boolean isCanClick() {
            return canClick;
        }

        public void setCanClick(boolean canClick) {
            this.canClick = canClick;
        }
    }
}
