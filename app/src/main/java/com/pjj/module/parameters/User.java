package com.pjj.module.parameters;

/**
 * Create by xinheng on 2018/11/07。
 * describe：
 */
public class User {
    private String phone;
    private String password;
    /**
     * 手机验证码
     */
    private String checkCode;
    /**
     * 修改密码
     */
    private String reset;

    public User() {
    }

    public User(String phone, String password, String checkCode, String reset) {
        this.phone = phone;
        this.password = password;
        this.checkCode = checkCode;
        this.reset = reset;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append("\"phone\":");
        buffer.append(phone);
        buffer.append("\"password\":");
        buffer.append(password);
        return buffer.toString();
    }
}
