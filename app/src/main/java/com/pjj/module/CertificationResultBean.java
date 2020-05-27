package com.pjj.module;

/**
 * Created by XinHeng on 2018/12/21.
 * describe：
 */
public class CertificationResultBean extends ResultBean {

    /**
     * userAuthDetails : {"isAuth":"3","idcart":"123456543567876","idBackFile":"birthdayBG2.png","idHoldFile":"birthdayBG3.png","name":"开发测试","authType":"1","userId":"40","idFaceFile":"birthdayBG.png","opinion":""}
     * userBusinessAuthDetails : {"professionType":"1","proxidcart":"代理人身份证","proxName":"代理人名称","companyName":"开发测试公司","idcart":"983726483719274","idBackFile":"idBack.png","idHoldFile":"idHold.png","businessLicenceHold":"businessLicenceHold.png","userId":"40","proxidHoldFile":"proxidHoldFile","opinion":"","businessLicence":"businessLicence.png","proxHoldIdFile":"proxHoldIdFile","isAuth":"2","professionName":"餐饮","name":"法人名称","proxidFaceFile":"proxidFaceFile","authType":"2","proxidBackFile":"proxidBackFile","idFaceFile":"idFace.png"}
     */

    private UserAuthDetailsBean userAuthDetails;
    private UserBusinessAuthDetailsBean userBusinessAuthDetails;

    public UserAuthDetailsBean getUserAuthDetails() {
        return userAuthDetails;
    }

    public void setUserAuthDetails(UserAuthDetailsBean userAuthDetails) {
        this.userAuthDetails = userAuthDetails;
    }

    public UserBusinessAuthDetailsBean getUserBusinessAuthDetails() {
        return userBusinessAuthDetails;
    }

    public void setUserBusinessAuthDetails(UserBusinessAuthDetailsBean userBusinessAuthDetails) {
        this.userBusinessAuthDetails = userBusinessAuthDetails;
    }

    public static class UserAuthDetailsBean {
        /**
         * isAuth : 3
         * idcart : 123456543567876
         * idBackFile : birthdayBG2.png
         * idHoldFile : birthdayBG3.png
         * name : 开发测试
         * authType : 1
         * userId : 40
         * idFaceFile : birthdayBG.png
         * opinion :
         */

        private String isAuth;
        private String idcart;
        private String idBackFile;
        private String idHoldFile;
        private String name;
        private String authType;
        private String userId;
        private String idFaceFile;
        private String opinion;

        public String getIsAuth() {
            return isAuth;
        }

        public void setIsAuth(String isAuth) {
            this.isAuth = isAuth;
        }

        public String getIdcart() {
            return idcart;
        }

        public void setIdcart(String idcart) {
            this.idcart = idcart;
        }

        public String getIdBackFile() {
            return idBackFile;
        }

        public void setIdBackFile(String idBackFile) {
            this.idBackFile = idBackFile;
        }

        public String getIdHoldFile() {
            return idHoldFile;
        }

        public void setIdHoldFile(String idHoldFile) {
            this.idHoldFile = idHoldFile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getIdFaceFile() {
            return idFaceFile;
        }

        public void setIdFaceFile(String idFaceFile) {
            this.idFaceFile = idFaceFile;
        }

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }
    }

    public static class UserBusinessAuthDetailsBean {
        /**
         * professionType : 1
         * proxidcart : 代理人身份证
         * proxName : 代理人名称
         * companyName : 开发测试公司
         * idcart : 983726483719274
         * idBackFile : idBack.png
         * idHoldFile : idHold.png
         * businessLicenceHold : businessLicenceHold.png
         * userId : 40
         * proxidHoldFile : proxidHoldFile
         * opinion :
         * businessLicence : businessLicence.png
         * proxHoldIdFile : proxHoldIdFile
         * isAuth : 2
         * professionName : 餐饮
         * name : 法人名称
         * proxidFaceFile : proxidFaceFile
         * authType : 2
         * proxidBackFile : proxidBackFile
         * idFaceFile : idFace.png
         */

        private String professionType;
        private String proxidcart;
        private String proxName;
        private String companyName;
        private String companyNo;
        private String idcart;
        private String idBackFile;
        private String idHoldFile;
        private String businessLicenceHold;
        private String userId;
        private String proxidHoldFile;
        private String opinion;
        private String businessLicence;
        private String proxHoldIdFile;
        private String isAuth;
        private String professionName;
        private String name;
        private String proxidFaceFile;
        private String authType;
        private String proxidBackFile;
        private String idFaceFile;

        public String getProfessionType() {
            return professionType;
        }

        public void setProfessionType(String professionType) {
            this.professionType = professionType;
        }

        public String getProxidcart() {
            return proxidcart;
        }

        public void setProxidcart(String proxidcart) {
            this.proxidcart = proxidcart;
        }

        public String getProxName() {
            return proxName;
        }

        public void setProxName(String proxName) {
            this.proxName = proxName;
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

        public String getIdcart() {
            return idcart;
        }

        public void setIdcart(String idcart) {
            this.idcart = idcart;
        }

        public String getIdBackFile() {
            return idBackFile;
        }

        public void setIdBackFile(String idBackFile) {
            this.idBackFile = idBackFile;
        }

        public String getIdHoldFile() {
            return idHoldFile;
        }

        public void setIdHoldFile(String idHoldFile) {
            this.idHoldFile = idHoldFile;
        }

        public String getBusinessLicenceHold() {
            return businessLicenceHold;
        }

        public void setBusinessLicenceHold(String businessLicenceHold) {
            this.businessLicenceHold = businessLicenceHold;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProxidHoldFile() {
            return proxidHoldFile;
        }

        public void setProxidHoldFile(String proxidHoldFile) {
            this.proxidHoldFile = proxidHoldFile;
        }

        public String getOpinion() {
            return opinion;
        }

        public void setOpinion(String opinion) {
            this.opinion = opinion;
        }

        public String getBusinessLicence() {
            return businessLicence;
        }

        public void setBusinessLicence(String businessLicence) {
            this.businessLicence = businessLicence;
        }

        public String getProxHoldIdFile() {
            return proxHoldIdFile;
        }

        public void setProxHoldIdFile(String proxHoldIdFile) {
            this.proxHoldIdFile = proxHoldIdFile;
        }

        public String getIsAuth() {
            return isAuth;
        }

        public void setIsAuth(String isAuth) {
            this.isAuth = isAuth;
        }

        public String getProfessionName() {
            return professionName;
        }

        public void setProfessionName(String professionName) {
            this.professionName = professionName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProxidFaceFile() {
            return proxidFaceFile;
        }

        public void setProxidFaceFile(String proxidFaceFile) {
            this.proxidFaceFile = proxidFaceFile;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        public String getProxidBackFile() {
            return proxidBackFile;
        }

        public void setProxidBackFile(String proxidBackFile) {
            this.proxidBackFile = proxidBackFile;
        }

        public String getIdFaceFile() {
            return idFaceFile;
        }

        public void setIdFaceFile(String idFaceFile) {
            this.idFaceFile = idFaceFile;
        }
    }
}
