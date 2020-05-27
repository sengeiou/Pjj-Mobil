package com.pjj.module;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by XinHeng on 2018/12/15.
 * describe：
 */
public class VerificaBean extends ResultBean implements Parcelable {
    /**
     * userAuth : 3 // 个人---  0 未认证  1审核中  2认证失败  3认证成功
     * userBusinessAuth : 3 //商家---  0 未认证  1审核中  2认证失败  3认证成功
     */
//{"msg":"查询成功","flag":"1","userAuthOpinion":"考虑进来了","userBusinessAuthOpinion":"","userAuth":"2","userBusinessAuth":"0"}
    private String userAuth;
    private String userBusinessAuth;
    private String userBusinessAuthOpinion = "";
    private String userAuthOpinion = "";

    protected VerificaBean(Parcel in) {
        userAuth = in.readString();
        userBusinessAuth = in.readString();
        userBusinessAuthOpinion = in.readString();
        userAuthOpinion = in.readString();
    }

    public static final Creator<VerificaBean> CREATOR = new Creator<VerificaBean>() {
        @Override
        public VerificaBean createFromParcel(Parcel in) {
            return new VerificaBean(in);
        }

        @Override
        public VerificaBean[] newArray(int size) {
            return new VerificaBean[size];
        }
    };

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getUserBusinessAuth() {
        return userBusinessAuth;
    }

    public void setUserBusinessAuth(String userBusinessAuth) {
        this.userBusinessAuth = userBusinessAuth;
    }

    public String getUserBusinessAuthOpinion() {
        return userBusinessAuthOpinion;
    }

    public void setUserBusinessAuthOpinion(String userBusinessAuthOpinion) {
        this.userBusinessAuthOpinion = userBusinessAuthOpinion;
    }

    public String getUserAuthOpinion() {
        return userAuthOpinion;
    }

    public void setUserAuthOpinion(String userAuthOpinion) {
        this.userAuthOpinion = userAuthOpinion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userAuth);
        dest.writeString(userBusinessAuth);
        dest.writeString(userBusinessAuthOpinion);
        dest.writeString(userAuthOpinion);
    }
}
