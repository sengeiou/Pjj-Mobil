package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2018/12/11.
 * describe：
 */
public class IdentityInf {
    private String userId;
    /**
     * 认证类型:1个人 2法人 3代理人
     */
    private String authType = "1";
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司统一信用代码
     */
    private String companyNo;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 身份证号
     */
    private String idcart;
    ///////*************/////////////////////////////
    /**
     * 行业类型
     */
    private String professionType;
    /**
     * 职务
     */
    private String position;

    /**
     * 营业执照
     */
    private String businessLicence;
    private String businessLicence1;
    /**
     * 手持营业执照
     */
    private String businessLicenceHold;
    private String businessLicenceHold1;
    /**
     * 身份证正面照
     */
    private String idFace;
    private String idFace1;
    /**
     * 身份证背面
     */
    private String idBack;
    private String idBack1;
    /**
     * 手持身份证
     */
    private String idHold;
    private String idHold1;
    //代理人
    private String proxName;//代理人姓名
    private String proxidcart;//代理人身份证号

    private String proxidFaceFile;
    private String proxidFaceFile1;
    private String proxidBackFile;
    private String proxidBackFile1;
    private String proxidHoldFile;
    private String proxidHoldFile1;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcart() {
        return idcart;
    }

    public void setIdcart(String idcart) {
        this.idcart = idcart;
    }

    public String getProfessionType() {
        return professionType;
    }

    public void setProfessionType(String professionType) {
        this.professionType = professionType;
    }

    public String getBusinessLicence() {
        return businessLicence;
    }

    public void setBusinessLicence(String businessLicence) {
        this.businessLicence = businessLicence;
    }

    public String getIdFace() {
        return idFace;
    }

    public void setIdFace(String idFace) {
        this.idFace = idFace;
    }

    public String getIdBack() {
        return idBack;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

    public String getIdHold() {
        return idHold;
    }

    public void setIdHold(String idHold) {
        this.idHold = idHold;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBusinessLicenceHold() {
        return businessLicenceHold;
    }

    public void setBusinessLicenceHold(String businessLicenceHold) {
        this.businessLicenceHold = businessLicenceHold;
    }

    public String getBusinessLicence1() {
        return businessLicence1;
    }

    public void setBusinessLicence1(String businessLicence1) {
        this.businessLicence1 = businessLicence1;
    }

    public String getBusinessLicenceHold1() {
        return businessLicenceHold1;
    }

    public void setBusinessLicenceHold1(String businessLicenceHold1) {
        this.businessLicenceHold1 = businessLicenceHold1;
    }

    public String getIdFace1() {
        return idFace1;
    }

    public void setIdFace1(String idFace1) {
        this.idFace1 = idFace1;
    }

    public String getIdBack1() {
        return idBack1;
    }

    public void setIdBack1(String idBack1) {
        this.idBack1 = idBack1;
    }

    public String getIdHold1() {
        return idHold1;
    }

    public void setIdHold1(String idHold1) {
        this.idHold1 = idHold1;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getProxidFaceFile() {
        return proxidFaceFile;
    }

    public void setProxidFaceFile(String proxidFaceFile) {
        this.proxidFaceFile = proxidFaceFile;
    }

    public String getProxidFaceFile1() {
        return proxidFaceFile1;
    }

    public void setProxidFaceFile1(String proxidFaceFile1) {
        this.proxidFaceFile1 = proxidFaceFile1;
    }

    public String getProxidBackFile() {
        return proxidBackFile;
    }

    public void setProxidBackFile(String proxidBackFile) {
        this.proxidBackFile = proxidBackFile;
    }

    public String getProxidBackFile1() {
        return proxidBackFile1;
    }

    public void setProxidBackFile1(String proxidBackFile1) {
        this.proxidBackFile1 = proxidBackFile1;
    }

    public String getProxidHoldFile() {
        return proxidHoldFile;
    }

    public void setProxidHoldFile(String proxidHoldFile) {
        this.proxidHoldFile = proxidHoldFile;
    }

    public String getProxidHoldFile1() {
        return proxidHoldFile1;
    }

    public void setProxidHoldFile1(String proxidHoldFile1) {
        this.proxidHoldFile1 = proxidHoldFile1;
    }

    public String getProxName() {
        return proxName;
    }

    public void setProxName(String proxName) {
        this.proxName = proxName;
    }

    public String getProxidcart() {
        return proxidcart;
    }

    public void setProxidcart(String proxidcart) {
        this.proxidcart = proxidcart;
    }
}
