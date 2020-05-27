package com.pjj.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.pjj.module.xspad.NewMediaData;
import com.pjj.utils.TextUtils;
import com.pjj.view.adapter.OrderElevatorInfAdapter;
import com.pjj.view.adapter.SelectReleaseAreaAllAdapter;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XinHeng on 2019/04/19.
 * describe：
 */
public class MediaOrderInfBean extends ResultBean {

    /**
     * templetList : [{"templetName":"新媒体测试模板","templet_id":"4a8ad2025d814dcdac0da60e556d9220","templetType":"1","fileList":[{"fileName":"6041bc0861b64f1d8742785fdccd2a3b.jpg","type":"1","filePlace":"1"}]}]
     * orderInfo : {"pieceType":"0","orderType":"7","amount":10,"orderId":"20190418110313155555659319678439","userId":"111","content":"多选新媒体","isShowName":"0","createTime":1555556593000,"playType":"0","isShowPhone":"0","pieceNum":1,"playDate":"2019-03-28","playTime":"19,20","authType":"1","status":"7"}
     * orderScreenList : [{"price":10,"screenType":"1","communityName":"朗琴国际B座","position":" 北京 北京市 西城区","communityId":"95","screenNum":1,"sourcePrice":10}]
     */

    private OrderInfoBean orderInfo;
    private List<UserTempletBean.DataBean> templetList;
    private List<OrderScreenListBean> orderScreenList;
    private ArrayList<ScreenPriceDetailBean> screenPriceDetail;
    private float sumPrice;//总价格
    private int longTime;//播放总时长

    public float getSumPrice() {
        if (TextUtils.isNotEmptyList(orderScreenList)) {

        }
        return sumPrice;
    }

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
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

    public ArrayList<ScreenPriceDetailBean> getScreenPriceDetail() {
        return screenPriceDetail;
    }

    public void setScreenPriceDetail(ArrayList<ScreenPriceDetailBean> screenPriceDetail) {
        this.screenPriceDetail = screenPriceDetail;
    }

    public static class OrderInfoBean {
        /**
         * pieceType : 0
         * orderType : 7
         * amount : 10
         * orderId : 20190418110313155555659319678439
         * userId : 111
         * content : 多选新媒体
         * isShowName : 0
         * createTime : 1555556593000
         * playType : 0
         * isShowPhone : 0
         * pieceNum : 1
         * playDate : 2019-03-28
         * playTime : 19,20
         * authType : 1
         * status : 7
         */

        private String pieceType;
        private String orderType;
        private float amount;
        private String orderId;
        private String userId;
        private String content;
        private String isShowName;
        private long createTime;
        private String playType;
        private String isShowPhone;
        private int pieceNum;
        private int showTime;
        private String playDate;
        private String playTime;
        private String revokeMsg;//订单撤销原因
        private String authType;
        @SerializedName("status")
        private String statusX;
        private String playCount;//播放次数
        private String playTimeLong;//播放时长
        private int screenCount;// 屏幕总数量
        private int elevCount;// 电梯总数量

        public String getRevokeMsg() {
            return revokeMsg;
        }

        public void setRevokeMsg(String revokeMsg) {
            this.revokeMsg = revokeMsg;
        }

        public int getElevCount() {
            return elevCount;
        }

        public int getShowTime() {
            return showTime;
        }

        public void setShowTime(int showTime) {
            this.showTime = showTime;
        }

        public void setElevCount(int elevCount) {
            this.elevCount = elevCount;
        }

        public int getScreenCount() {
            return screenCount;
        }

        public void setScreenCount(int screenCount) {
            this.screenCount = screenCount;
        }

        public String getPlayTimeLong() {
            return playTimeLong;
        }

        public void setPlayTimeLong(String playTimeLong) {
            this.playTimeLong = playTimeLong;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public String getPieceType() {
            return pieceType;
        }

        public void setPieceType(String pieceType) {
            this.pieceType = pieceType;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public int getPieceNum() {
            return pieceNum;
        }

        public void setPieceNum(int pieceNum) {
            this.pieceNum = pieceNum;
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
    }

    public static class OrderScreenListBean implements SelectReleaseAreaAllAdapter.OrderAllInfParent,OrderElevatorInfAdapter.OrderElevatorInfParent  {
        /**
         * price : 10
         * screenType : 1
         * communityName : 朗琴国际B座
         * position :  北京 北京市 西城区
         * communityId : 95
         * screenNum : 1
         * sourcePrice : 10
         */

        private float price;
        private String screenType;
        private String communityName;
        private String position;
        private String communityId;
        private int screenNum;
        private int elevNum;
        private float sourcePrice;
        private String imgName;

        public int getElevNum() {
            return elevNum;
        }

        public void setElevNum(int elevNum) {
            this.elevNum = elevNum;
        }

        public String getImgName() {
            return imgName;
        }

        public void setImgName(String imgName) {
            this.imgName = imgName;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getScreenType() {
            return screenType;
        }

        public void setScreenType(String screenType) {
            this.screenType = screenType;
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

        public int getScreenNum() {
            return screenNum;
        }

        public void setScreenNum(int screenNum) {
            this.screenNum = screenNum;
        }

        public float getSourcePrice() {
            return sourcePrice;
        }

        public void setSourcePrice(float sourcePrice) {
            this.sourcePrice = sourcePrice;
        }

        @Nullable
        @Override
        public String getBuildingImageName() {
            return imgName;
        }

        @Nullable
        @Override
        public String getBuildingName() {
            return communityName;
        }

        @Override
        public int getElevatorCount() {
            return elevNum;
        }

        @Override
        public int getScreenCount() {
            return screenNum;
        }
    }

    public static ArrayList<ScreenPriceDetailBean> chageScreenBeanList(List<NewMediaData.ScreenBean> screenBeans) {
        ArrayList<ScreenPriceDetailBean> screenPriceDetailBeans = new ArrayList<>();
        if (TextUtils.isNotEmptyList(screenBeans)) {
            for (int i = 0; i < screenBeans.size(); i++) {
                NewMediaData.ScreenBean screenBean = screenBeans.get(i);
                List<String> days = screenBean.getDays();
                if (TextUtils.isNotEmptyList(days)) {
                    ScreenPriceDetailBean screenPriceDetailBean = new ScreenPriceDetailBean();
                    screenPriceDetailBean.setScreenId(screenBean.getSrceenId());
                    screenPriceDetailBean.setScreenName(screenBean.getScreenName());
                    screenPriceDetailBean.setOrderTimeList(days);
                    screenPriceDetailBean.setPrice(screenBean.getPriceDay());
                    screenPriceDetailBeans.add(screenPriceDetailBean);
                }
            }
        }
        return screenPriceDetailBeans;
    }

    public static class ScreenPriceDetailBean implements Parcelable {
        private String screenId;
        private List<String> orderTimeList;
        private String orderId;
        private String screenName;
        private float price;

        public ScreenPriceDetailBean() {
        }

        protected ScreenPriceDetailBean(Parcel in) {
            screenId = in.readString();
            orderTimeList = in.createStringArrayList();
            orderId = in.readString();
            screenName = in.readString();
            price = in.readFloat();
        }

        public static final Creator<ScreenPriceDetailBean> CREATOR = new Creator<ScreenPriceDetailBean>() {
            @Override
            public ScreenPriceDetailBean createFromParcel(Parcel in) {
                return new ScreenPriceDetailBean(in);
            }

            @Override
            public ScreenPriceDetailBean[] newArray(int size) {
                return new ScreenPriceDetailBean[size];
            }
        };

        public String getScreenId() {
            return screenId;
        }

        public void setScreenId(String screenId) {
            this.screenId = screenId;
        }

        public List<String> getOrderTimeList() {
            return orderTimeList;
        }

        public void setOrderTimeList(List<String> orderTimeList) {
            this.orderTimeList = orderTimeList;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getScreenName() {
            return screenName;
        }

        public void setScreenName(String screenName) {
            this.screenName = screenName;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(screenId);
            dest.writeStringList(orderTimeList);
            dest.writeString(orderId);
            dest.writeString(screenName);
            dest.writeFloat(price);
        }
    }
}
