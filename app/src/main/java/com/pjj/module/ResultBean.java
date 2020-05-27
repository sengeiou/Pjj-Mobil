package com.pjj.module;

/**
 * Create by xinheng on 2018/10/17。
 * describe：
 */
public class ResultBean {
    public static String SUCCESS_CODE="ok";
    public static String SUCCESS_CODE1="1";
    private String msg;
    private String status;
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isSuccess(){
        return SUCCESS_CODE.equals(getStatus())||SUCCESS_CODE1.equals(getFlag());
    }

}
