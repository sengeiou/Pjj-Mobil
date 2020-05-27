package com.pjj.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XinHeng on 2018/12/25.
 * describe：
 */
public class MakeOrderBean extends ResultBean {
    /**
     * orderId : 20181225114428154570946836891269
     */
    @SerializedName(value = "orderId", alternate = {"data", "goodOrderId"})
    private String orderId;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


}
