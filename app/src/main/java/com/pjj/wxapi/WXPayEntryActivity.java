package com.pjj.wxapi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pjj.R;
import com.pjj.module.xspad.PayManage;
import com.pjj.utils.Log;
import com.pjj.view.activity.BaseActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by XinHeng on 2019/01/02.
 * describe：
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay_entry);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        api = WXAPIFactory.createWXAPI(this, "wxf294318db839d271");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("TAG微信", "BaseReq:" + baseReq.getType());
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Log.e("TAG微信", "ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Log.e("TAG微信", "ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX");
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("TAG", "onResp: ");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.e("TAG", "onPayFinish,errCode=" + resp.errCode);
            //errCode=-1
            if (resp.errCode == -2) {
                PayManage.getInstance().getPayDialogHelp().setPayResult(false, "用户取消支付");
            } else if (resp.errCode == -1) {
                PayManage.getInstance().getPayDialogHelp().setPayResult(false, "打开微信异常");
            } else {
                PayManage.getInstance().getPayDialogHelp().loadPayResult();
            }
        }
        finish();
    }

}
