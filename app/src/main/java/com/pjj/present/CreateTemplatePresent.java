package com.pjj.present;

import com.pjj.contract.CreateTemplateContract;

/**
 * Create by xinheng on 2018/12/06 13:40。
 * describe：P层
 */
public class CreateTemplatePresent extends BasePresent<CreateTemplateContract.View> implements CreateTemplateContract.Present {

    public CreateTemplatePresent(CreateTemplateContract.View view) {
        super(view, CreateTemplateContract.View.class);
    }

}
