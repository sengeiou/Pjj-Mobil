package com.pjj.present;

import com.pjj.contract.OrderContract;

/**
 * Create by xinheng on 2018/12/07 10:26。
 * describe：P层
 */
public class OrderPresent extends BasePresent<OrderContract.View> implements OrderContract.Present {

    public OrderPresent(OrderContract.View view) {
        super(view, OrderContract.View.class);
    }
}
