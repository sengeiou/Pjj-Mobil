package com.pjj.module.parameters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XinHeng on 2018/11/29.
 * describe：
 */
public class Template implements Parcelable {
    private String userId;
    private String identityType;// 身份类型:1个人 2商家
    private String infoType;// 身份类型:1个人 2商家
    private String purposeType = "1";//  用途类型:1 DIY类型 2便民信息 3填空传媒
    private String selfUseTag;
    private String comeFromTemplate;

    public String getSelfUseTag() {
        return selfUseTag;
    }

    public void setSelfUseTag(String selfUseTag) {
        this.selfUseTag = selfUseTag;
    }

    public String getComeFromTemplate() {
        return comeFromTemplate;
    }

    public void setComeFromTemplate(String comeFromTemplate) {
        this.comeFromTemplate = comeFromTemplate;
    }

    public Template clone() {
        Template template = new Template();
        template.setUserId(userId);
        template.setIdentityType(identityType);
        template.setInfoType(infoType);
        template.setPurposeType(purposeType);
        template.setSelfUseTag(selfUseTag);
        template.setComeFromTemplate(comeFromTemplate);
        return template;
    }

    public Template() {
    }

    public Template(String userId, String identityType, String purposeType) {
        this.userId = userId;
        this.identityType = identityType;
        this.purposeType = purposeType;
    }

    protected Template(Parcel in) {
        userId = in.readString();
        identityType = in.readString();
        infoType = in.readString();
        purposeType = in.readString();
        selfUseTag = in.readString();
        comeFromTemplate = in.readString();
    }

    public static final Creator<Template> CREATOR = new Creator<Template>() {
        @Override
        public Template createFromParcel(Parcel in) {
            return new Template(in);
        }

        @Override
        public Template[] newArray(int size) {
            return new Template[size];
        }
    };

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getPurposeType() {
        return purposeType;
    }

    public void setPurposeType(String purposeType) {
        this.purposeType = purposeType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(identityType);
        dest.writeString(infoType);
        dest.writeString(purposeType);
        dest.writeString(selfUseTag);
        dest.writeString(comeFromTemplate);
    }

    @Override
    public String toString() {
        return "Template{" +
                "userId='" + userId + '\'' +
                ", identityType='" + identityType + '\'' +
                ", infoType='" + infoType + '\'' +
                ", purposeType='" + purposeType + '\'' +
                '}';
    }
}
