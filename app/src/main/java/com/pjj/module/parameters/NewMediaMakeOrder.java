package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2019/03/29.
 * describe：传媒生成订单
 */
public class NewMediaMakeOrder {
    /**
     * {“屏幕id”:”播放日期yyyy-MM-dd#播放时间6,1”}
     */
    private String screen_time;
    private String templetIds;
    private String userId;
    /**
     * 订单用途 1个人 2商家 3拼屏
     */
    private String authType;
    /**
     * 6新模式 7传媒模式 8拼屏模式
     */
    private String orderType;
    /**
     * 播放日期yyyy-MM-dd
     */
    private String playDate;
    /**
     * 播放时间:15,16,17,18,19,20,21,22,23
     */
    private String playTime;
    /**
     * 播放类型：0 普通 1 包周 2包月
     */
    private String playType;
    /**
     * 订单显示时间,传媒模式暂时默认15秒
     */
    private int showTime;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getShowTime() {
        return showTime;
    }

    public void setShowTime(int showTime) {
        this.showTime = showTime;
    }
}
