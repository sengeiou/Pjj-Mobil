package com.pjj.module;

/**
 * Created by XinHeng on 2018/12/14.
 * describe：
 */
public class LoginResult extends ResultBean {
    /**
     * data : {"head":"bd6c2d7e4dde4453bd2a205a6cd6e926","headName":"afd5292d3e7d430a829a74710f8ff0df.jpg","password":"EFG@AB","phoneNumber":"18201538182","creatTime":1539396582000,"loginName":"18201538182","sex":"1","nickname":"18201538182","userId":"1e7ea4a8f4c947f1a4b816f085af3734","userNumber":"6f484a0340a94b0ab0cfe4c64d59ccf9","token":"MWU3ZWE0YThmNGM5NDdmMWE0YjgxNmYwODVhZjM3MzQyMDE4LTEyLTE0IDExOjMwOjU1"}
     * token : MWU3ZWE0YThmNGM5NDdmMWE0YjgxNmYwODVhZjM3MzQyMDE4LTEyLTE0IDExOjMwOjU1
     */

    private DataBean data;
    private String token;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class DataBean {
        /**
         * head : bd6c2d7e4dde4453bd2a205a6cd6e926
         * headName : afd5292d3e7d430a829a74710f8ff0df.jpg
         * password : EFG@AB
         * phoneNumber : 18201538182
         * creatTime : 1539396582000
         * loginName : 18201538182
         * sex : 1
         * nickname : 18201538182
         * userId : 1e7ea4a8f4c947f1a4b816f085af3734
         * userNumber : 6f484a0340a94b0ab0cfe4c64d59ccf9
         * token : MWU3ZWE0YThmNGM5NDdmMWE0YjgxNmYwODVhZjM3MzQyMDE4LTEyLTE0IDExOjMwOjU1
         */

        private String head;
        private String headName;
        private String password;
        private String phoneNumber;
        private long creatTime;
        private String loginName;
        private String sex;
        private String nickname;
        private String userId;
        private String userNumber;
        private String token;
        private String userType;

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getHeadName() {
            return headName;
        }

        public void setHeadName(String headName) {
            this.headName = headName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public long getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(long creatTime) {
            this.creatTime = creatTime;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserNumber() {
            return userNumber;
        }

        public void setUserNumber(String userNumber) {
            this.userNumber = userNumber;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
