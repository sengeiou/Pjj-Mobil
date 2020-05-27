package com.pjj.present;

import com.pjj.contract.ExchangeSuccessContract;

/**
 * Create by xinheng on 2019/04/12 17:09。
 * describe：P层
 */
public class ExchangeSuccessPresent extends BasePresent<ExchangeSuccessContract.View> implements ExchangeSuccessContract.Present {

    public ExchangeSuccessPresent(ExchangeSuccessContract.View view) {
        super(view, ExchangeSuccessContract.View.class);
    }
}
