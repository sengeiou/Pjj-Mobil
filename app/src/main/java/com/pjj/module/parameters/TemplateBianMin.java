package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2018/12/10.
 * describe：便民
 */
public class TemplateBianMin {
    //{userId:123,info:个人便民信息,infoType:1个人 2商家,title:标题}
    private String userId;
    private String peopleInfoId;
    private String info;
    private String infoType;
    private String purposeType;
    private String title;

    public TemplateBianMin() {
    }

    public TemplateBianMin(String userId, String info, String infoType, String title) {
        this.userId = userId;
        this.info = info;
        this.infoType = infoType;
        this.title = title;
    }

    public String getPurposeType() {
        return purposeType;
    }

    public void setPurposeType(String purposeType) {
        this.purposeType = purposeType;
    }

    public String getPeopleInfoId() {
        return peopleInfoId;
    }

    public void setPeopleInfoId(String peopleInfoId) {
        this.peopleInfoId = peopleInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
