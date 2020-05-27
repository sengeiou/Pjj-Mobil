package com.pjj.module;

import android.util.Log;

import com.pjj.utils.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by XinHeng on 2019/03/27.
 * describe：新传媒
 */
public class NewMediaBuildingBean extends ResultBean {
    private List<CommunityListBean> communityList;
    private List<CommunityListBean> hotCommunityList;

    public static List<CommunityListBean> filter(List<CommunityListBean> communityList, String name) {
        if (TextUtils.isNotEmptyList(communityList)) {
            List<CommunityListBean> communityNewList = new ArrayList();
            Iterator<CommunityListBean> iterator = communityList.iterator();
            while (iterator.hasNext()) {
                CommunityListBean next = iterator.next();
                if (next != null && next.communityName != null && next.communityName.contains(name)) {
                    communityNewList.add(next);
                }
            }
            return communityNewList;
        }
        return null;
    }

    public static List<CommunityListBean> filterAdd(List<CommunityListBean> communityList, List<CommunityListBean> listBeans, String name) {
        if (TextUtils.isNotEmptyList(communityList)) {
            List<CommunityListBean> communityListOther = new ArrayList<>();
            List<CommunityListBean> communityNewList = new ArrayList();
            Iterator<CommunityListBean> iterator = communityList.iterator();
            while (iterator.hasNext()) {
                CommunityListBean next = iterator.next();
                if (next != null && next.communityName != null && next.communityName.contains(name)) {
                    communityNewList.add(next);
                } else {
                    communityListOther.add(next);
                }
            }
            String tag = "暂无更多搜索结果，为您推荐热门大厦";
            if (communityNewList.size() == 0) {
                //communityNewList.add(new CommunityListBean());
                tag = "暂无资源，为您推荐热门大厦";
            }
            listBeans.addAll(communityNewList);
            if (communityListOther.size() > 0) {
                CommunityListBean e = new CommunityListBean();//猜你所想 文本
                e.setAreaCode(tag);
                listBeans.add(e);
                listBeans.addAll(communityListOther);
            }
            return communityNewList;
        }
        return null;
    }

    public static class CommunityListBean extends BuildingElevatorBean.CanClick {
        //传媒折扣
        private float mediaDiscount;
        //拼团折扣
        private float assembleDiscount;
        //新媒体折扣
        private float newDiscount;
        //新媒体最终折扣
        private float finaNewlDiscount;
        //新媒体屏幕数量
        private String screenNum;
        //小区图片
        private String imgName;
        //市区编码
        private String areaCode;
        private String communityName;
        private String position;
        private String street;
        private String communityId;
        //经度
        private String lng;
        //纬度
        private String lat;
        //合作运营模式 1自营 2联营 3公司运营 4自用
        private String cooperationMode;
        private float price;
        private List<String> screenType;
        private List<NewMediaScreenBean.DataBean.ScreenListBean> screenList;
        private boolean isShow;
        private boolean select;

        public MediaOrderInfBean.OrderScreenListBean cloneInf(int screenSize, float price) {
            MediaOrderInfBean.OrderScreenListBean communityListBean = new MediaOrderInfBean.OrderScreenListBean();
            communityListBean.setCommunityName(communityName);
            communityListBean.setScreenNum(screenSize);
            communityListBean.setImgName(imgName);
            Log.e("TAG", "cloneInf: price=" + price);
            communityListBean.setPrice(price);
            return communityListBean;
        }

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public boolean isShow() {
            return isShow;
        }

        public void setShow(boolean show) {
            isShow = show;
        }

        public List<NewMediaScreenBean.DataBean.ScreenListBean> getScreenList() {
            return screenList;
        }

        public void setScreenList(List<NewMediaScreenBean.DataBean.ScreenListBean> screenList) {
            this.screenList = screenList;
        }

        public String getCooperationMode() {
            return cooperationMode;
        }

        public void setCooperationMode(String cooperationMode) {
            this.cooperationMode = cooperationMode;
        }

        public List<String> getScreenType() {
            return screenType;
        }

        public void setScreenType(List<String> screenType) {
            this.screenType = screenType;
        }

        public float getMediaDiscount() {
            return mediaDiscount;
        }

        public void setMediaDiscount(float mediaDiscount) {
            this.mediaDiscount = mediaDiscount;
        }

        public float getAssembleDiscount() {
            return assembleDiscount;
        }

        public void setAssembleDiscount(float assembleDiscount) {
            this.assembleDiscount = assembleDiscount;
        }

        public float getNewDiscount() {
            return newDiscount;
        }

        public void setNewDiscount(float newDiscount) {
            this.newDiscount = newDiscount;
        }

        public float getFinaNewlDiscount() {
            return finaNewlDiscount;
        }

        public void setFinaNewlDiscount(float finaNewlDiscount) {
            this.finaNewlDiscount = finaNewlDiscount;
        }

        public String getScreenNum() {
            return screenNum;
        }

        public void setScreenNum(String screenNum) {
            this.screenNum = screenNum;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
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

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }
    }

    public List<CommunityListBean> getCommunityList() {
        return communityList;
    }

    public void setCommunityList(List<CommunityListBean> communityList) {
        this.communityList = communityList;
    }

    public List<CommunityListBean> getHotCommunityList() {
        return hotCommunityList;
    }

    public void setHotCommunityList(List<CommunityListBean> hotCommunityList) {
        this.hotCommunityList = hotCommunityList;
    }
}
