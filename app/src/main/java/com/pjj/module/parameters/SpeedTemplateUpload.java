package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2019/03/14.
 * describe：
 */
public class SpeedTemplateUpload {
    /**
     * fileUpload : 文件名&文件类型&位置&文件显示x&文件显示y&文件显示width&文件显示height,
     * userId : 用户id
     * fileRelName : 模板名称
     * identityType : 身份类型:3拼单
     * purposeType : 用途类型:  5拼屏信息
     * spellType : 拼单类型：1无  2两人拼单  3三人拼单  4四人拼单  5五人拼单  6六人拼单  7七人拼单  8八人拼单  9九人拼单  10十人拼单  11十一人拼单  12十二人拼单
     * spellFormat : 模板格式：0无  1第一种  2第二种  3第三种  4第四种  5第五种  6第六种
     * templetFormat : 模板格式：1竖放    2拼放
     */

    private String fileUpload;
    private String userId;
    private String fileRelName;
    private String identityType;
    private String purposeType;
    private String spellType;
    private String IdentificationId;
    private String templetFormat;

    public String getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(String fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getUserId() {
        return userId;
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

    public String getSpellType() {
        return spellType;
    }

    public void setSpellType(String spellType) {
        this.spellType = spellType;
    }

    public String getIdentificationId() {
        return IdentificationId;
    }

    public void setIdentificationId(String identificationId) {
        IdentificationId = identificationId;
    }

    public String getTempletFormat() {
        return templetFormat;
    }

    public void setTempletFormat(String templetFormat) {
        this.templetFormat = templetFormat;
    }
}
