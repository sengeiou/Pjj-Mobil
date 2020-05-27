package com.pjj.module.xspad;

import com.pjj.view.dialog.PayDialogHelp;

/**
 * Created by XinHeng on 2019/01/03.
 * describe：支付信息存储
 */
public class PayManage {
    private static PayManage INSTANCE;

    public static PayManage getInstance() {
        if (null == INSTANCE) {
            synchronized (PayManage.class) {
                if (null == INSTANCE) {
                    INSTANCE = new PayManage();
                }
            }
        }
        return INSTANCE;
    }

    private PayManage() {
    }

    private PayDialogHelp payDialogHelp;

    public PayDialogHelp getPayDialogHelp() {
        return payDialogHelp;
    }

    public void setPayDialogHelp(PayDialogHelp payDialogHelp) {
        this.payDialogHelp = payDialogHelp;
    }

    public void clear() {
        if (null != payDialogHelp) {
            payDialogHelp = null;
        }
    }
}
