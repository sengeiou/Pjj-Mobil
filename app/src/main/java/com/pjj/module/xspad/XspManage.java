package com.pjj.module.xspad;

import android.graphics.Color;

import com.pjj.R;
import com.pjj.module.BuildingBean;
import com.pjj.module.ElevatorBean;
import com.pjj.module.ElevatorTimeBean;
import com.pjj.module.UserTempletBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by XinHeng on 2018/11/21.
 * describe：广告屏选择模块信息暂存储
 */
public class XspManage {
    private static XspManage INSTANCE;
    /**
     * 再次发布，
     * 已选订单
     */
    private String templateId;

    public static XspManage getInstance() {
        if (null == INSTANCE) {
            synchronized (XspManage.class) {
                if (null == INSTANCE) {
                    INSTANCE = new XspManage();
                }
            }
        }
        return INSTANCE;
    }

    private XspManage() {
    }

    private RefreshTag refreshTag;

    public RefreshTag getRefreshTag() {
        if (null == refreshTag) {
            refreshTag = new RefreshTag();
        }
        return refreshTag;
    }

    private List<String> mGroupLocation;
    private List<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> mChildXsp;
    /**
     * 1 DIY类型 2便民信息 3diy填空传媒/随机 4便民随机
     * 5 拼屏信息（2-12） 6 新模式 7 传媒模式 8 拼屏模式（20）
     */
    private int adType = 1;
    private int identityType = 1;  //身份类型:1个人 2商家 3仅一个通过即可
    /**
     * 是否为便民拼屏
     */
    private int bianMinPing = 0;//1 是
    private int tagColorPing = -1;
    private String nameTypePing;
    /**
     * 各个屏幕的使用时间
     */
    private HashMap<String, List<ElevatorTimeBean.DayHours>> listHashMap;
    /**
     * 提交订单所需数据
     * "1111#b":"2018-09-04#1,13&2018-09-05#1,13,12"
     */
    private HashMap<String, String> xspHashMap;
    /**
     * 当前所选城市
     */
    private String cityName;
    private String cityNameWeather;
    private String lng;
    private String lat;
    private String aotuLocalCity;

    /**
     * 起始日期
     */
    private String startDate;
    /**
     * 发布时间
     */
    private String releaseTime;
    private String releaseTime_;
    /**
     * 播放类型 0 普通 1 包周 2包月
     */
    private String playType;
    /**
     * 订单内容
     * 便民具体内容或者diy的模板id(templetIds)
     */
    private String orderContent;
    private List<BuildingBean.CommunityListBean> buildList;

    private SpeedScreenData speedScreenData;
    private NewMediaData newMediaData;
    private IntegralGoods integralGoods;

    public NewMediaData getNewMediaData() {
        if (null == newMediaData) {
            newMediaData = new NewMediaData();
        }
        return newMediaData;
    }

    public void clearNewMediaData() {
        this.newMediaData = null;
    }

    public SpeedScreenData getSpeedScreenData() {
        if (null == speedScreenData) {
            speedScreenData = new SpeedScreenData();
        }
        return speedScreenData;
    }

    public IntegralGoods getIntegralGoods() {
        if (null == integralGoods) {
            integralGoods = new IntegralGoods();
        }
        return integralGoods;
    }

    public void cleanSpeedData() {
        speedScreenData = null;
    }

    public void cleanIntegralGoods() {
        integralGoods = null;
    }

    public List<BuildingBean.CommunityListBean> getBuildList() {
        return buildList;
    }

    public void setBuildList(List<BuildingBean.CommunityListBean> buildList) {
        this.buildList = buildList;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityNameWeather() {
        return cityNameWeather;
    }

    public void setCityNameWeather(String cityNameWeather) {
        this.cityNameWeather = cityNameWeather;
    }

    public String getAotuLocalCity() {
        return aotuLocalCity;
    }

    public void setAotuLocalCity(String aotuLocalCity) {
        this.aotuLocalCity = aotuLocalCity;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public HashMap<String, List<ElevatorTimeBean.DayHours>> getListHashMap() {
        return listHashMap;
    }

    public void setListHashMap(HashMap<String, List<ElevatorTimeBean.DayHours>> listHashMap) {
        this.listHashMap = listHashMap;
    }

    public HashMap<String, String> getXspHashMap() {
        return xspHashMap;
    }

    public void setXspHashMap(HashMap<String, String> xspHashMap) {
        this.xspHashMap = xspHashMap;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getReleaseTime_() {
        return releaseTime_;
    }

    public void setReleaseTime_(String releaseTime_) {
        this.releaseTime_ = releaseTime_;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }

    public int getBianMinPing() {
        return bianMinPing;
    }

    public void setBianMinPing(int bianMinPing) {
        this.bianMinPing = bianMinPing;
    }

    public int getTagColorPing() {
        return tagColorPing;
    }

    public void setTagColorPing(int tagColorPing) {
        this.tagColorPing = tagColorPing;
    }

    public String getNameTypePing() {
        return nameTypePing;
    }

    public void setNameTypePing(String nameTypePing) {
        this.nameTypePing = nameTypePing;
    }

    public List<String> getGroupLocation() {
        if (null == mGroupLocation)
            mGroupLocation = new ArrayList<>();
        return mGroupLocation;
    }


    public List<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> getChildXsp() {
        if (null == mChildXsp)
            mChildXsp = new ArrayList<>();
        return mChildXsp;
    }

    public void clearXspData() {
        if (null != mChildXsp) {
            mChildXsp.clear();
        }
        if (null != mGroupLocation) {
            mGroupLocation.clear();
        }
        orderContent = null;
        if (null != buildList) {
            buildList.clear();
        }
        if (null != listHashMap)
            listHashMap.clear();
        if (null != xspHashMap)
            xspHashMap.clear();
    }

    public String getXSPIds() {
        StringBuilder builder = new StringBuilder();
        Iterator<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> iterator = mChildXsp.iterator();
        while (iterator.hasNext()) {
            List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> next = iterator.next();
            Iterator<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> it = next.iterator();
            while (it.hasNext()) {
                ElevatorBean.DataBean.ElevatorListBean.ScreenListBean next1 = it.next();
                String screenId = next1.getScreenId();
                builder.append(screenId);
                builder.append(",");
            }
        }
        int length = builder.length();
        if (length > 0) {
            builder.deleteCharAt(length - 1);
        }
        return builder.toString();
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public int getXspNum() {
        int num = 0;
        Iterator<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> iterator = mChildXsp.iterator();
        while (iterator.hasNext()) {
            List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> next = iterator.next();
            num += next.size();
        }
        return num;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }

    public static int getBgColor(int position) {
        switch (position) {
            case 1:
                return Color.parseColor("#EF3A3A");
            case 2:
                return Color.parseColor("#FB8B47");
            case 3:
                return Color.parseColor("#FBB805");
            case 4:
                return Color.parseColor("#CB1E5D");
            case 5:
                return Color.parseColor("#69B714");
            case 6:
                return Color.parseColor("#40BBF7");
            case 7:
                return Color.parseColor("#14C9BF");
            case 8:
                return Color.parseColor("#749DEC");
            case 9:
                return Color.parseColor("#7B78ED");
            case 10:
                return Color.parseColor("#1C9876");
            case 11:
                return Color.parseColor("#0172AE");
            default:
                return Color.parseColor("#6D43C9");
        }
    }

    public static int getLeftResource(String position) {
        switch (position) {
            case "1":
                return R.mipmap.left1;
            case "2":
                return R.mipmap.left2;
            case "3":
                return R.mipmap.left3;
            case "4":
                return R.mipmap.left4;
            case "5":
                return R.mipmap.left5;
            case "6":
                return R.mipmap.left6;
            case "7":
                return R.mipmap.left7;
            case "8":
                return R.mipmap.left8;
            case "9":
                return R.mipmap.left9;
            case "10":
                return R.mipmap.left10;
            case "11":
                return R.mipmap.left11;
            default:
                return R.mipmap.left12;
        }
    }

    public static int getRightResource(String position) {
        switch (position) {
            case "1":
                return R.mipmap.right1;
            case "2":
                return R.mipmap.right2;
            case "3":
                return R.mipmap.right3;
            case "4":
                return R.mipmap.right4;
            case "5":
                return R.mipmap.right5;
            case "6":
                return R.mipmap.right6;
            case "7":
                return R.mipmap.right7;
            case "8":
                return R.mipmap.right8;
            case "9":
                return R.mipmap.right9;
            case "10":
                return R.mipmap.right10;
            case "11":
                return R.mipmap.right11;
            default:
                return R.mipmap.right12;
        }
    }


}
