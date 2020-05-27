package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2018/12/06.
 * describe：
 */
public class UploadTemplateNew {
    /**
     * 1&1 all图片 ；2&1 all视频
     * 文件名&文件类型&位置,文件名&文件类型&位置
     */
    private String fileUpload;
    /**
     * 模板类型:1 全图片 2全视频 3上视频下图片 4轮播
     */
    private String templetType;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 模板名称
     */
    private String fileRelName;
    /**
     * 身份类型:1个人 2商家
     */
    private String identityType;
    /**
     * 用途类型:1 DIY类型 2便民信息 3填空传媒
     */
    private String purposeType;


    public String getTempletType() {
        return templetType;
    }

    public void setTempletType(String templetType) {
        this.templetType = templetType;
    }

    public String getUserId() {
        return userId;
    }

    public String getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(String fileUpload) {
        this.fileUpload = fileUpload;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileRelName() {
        return fileRelName;
    }

    public void setFileRelName(String fileRelName) {
        this.fileRelName = fileRelName;
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
}
