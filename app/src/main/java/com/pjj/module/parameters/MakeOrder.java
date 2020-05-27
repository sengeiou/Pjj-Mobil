package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2018/12/05.
 * describe：
 */
public class MakeOrder {

    /**
     * templetIds : 123
     * screen_time :
     * allPrice : 1000 -
     * content : 备注 -
     * userId : 1234
     * orderId : sjfaksjfksajd -
     * peopleInfoId : 便民信息
     * authType : 订单用途 1个人 2商家
     * orderType : 订单类型：1 DIY类型 2便民信息 3填空传媒
     * playDate : 播放日期 2018-12-05
     * playTime : 播放时间
     * playType : 播放类型：0 普通 1 包周 2包月 null随机
     * isShowPhone : 是否显示手机号：0 不显示 1 显示
     * isShowName : 是否显示姓名：0 不显示 1 显示
     * startTime : 填空开始时间
     * playlongtime : 填空时长
     * dayplaytime : 填空时长
     */
    private String communityId;
    private String templetIds;
    private String screen_time;
    private String allPrice;
    private String content;
    private String userId;
    private String orderId;
    private String peopleInfoId;
    private String authType;
    private String orderType;
    private String playDate;
    private String playTime;
    private String playType;
    private String isShowPhone;
    private String isShowName;
    private String startTime;
    private String playlongtime;
    private String dayplaytime;

    private String pieceType;//拼屏类型 : 0 不拼 1 拼屏幕 2拼时间段
    private String pieceNum;//拼屏数
    private String pieceTitle;//便民信息分类
    private String pieceColour;//拼屏背景色


    private String file_id;//拼屏
    private String IdentificationId;//模板唯一标识



    public String getTempletIds() {
        return templetIds;
    }

    public void setTempletIds(String templetIds) {
        this.templetIds = templetIds;
    }

    public String getScreen_time() {
        return screen_time;
    }

    public void setScreen_time(String screen_time) {
        this.screen_time = screen_time;
    }

    public String getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(String allPrice) {
        this.allPrice = allPrice;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getPeopleInfoId() {
        return peopleInfoId;
    }

    public void setPeopleInfoId(String peopleInfoId) {
        this.peopleInfoId = peopleInfoId;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public String getIsShowName() {
        return isShowName;
    }

    public void setIsShowName(String isShowName) {
        this.isShowName = isShowName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

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

    public String getFile_id() {
        return file_id;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public String getIdentificationId() {
        return IdentificationId;
    }

    public void setIdentificationId(String identificationId) {
        IdentificationId = identificationId;
    }
}
