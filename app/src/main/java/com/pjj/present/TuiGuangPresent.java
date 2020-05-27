package com.pjj.present;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pjj.contract.TuiGuangContract;
import com.pjj.intent.RetrofitService;
import com.pjj.module.CipherPayZhifubaoBean;
import com.pjj.module.ResultBean;
import com.pjj.module.TopPriceBean;

import org.jetbrains.annotations.NotNull;

/**
 * Create by xinheng on 2019/07/15 11:14。
 * describe：P层
 */
public class TuiGuangPresent<V extends TuiGuangContract.View> extends PayPresent<V> implements TuiGuangContract.Present {

    public TuiGuangPresent(V view, Class<V> aClass) {
        super(view, aClass);
    }

    @Override
    public void loadTopPriceTask() {
        getRetrofitService().getTopPriceList(new PresentCallBack<TopPriceBean>(TopPriceBean.class) {
            @Override
            protected void successResult(TopPriceBean topPriceBean) {
                mView.updatePrice(topPriceBean.getData());
            }
        });
    }

    @Override
    public void dealWithGoodsOrderResult(@NotNull String result) {
        Log.e(TAG, "dealWithGoodsOrderResult: " + result);
        JsonElement jsonElement = new JsonParser().parse(result);
        if (jsonElement != null && jsonElement.isJsonObject()) {
            JsonObject object = jsonElement.getAsJsonObject();
            String flag = object.get("flag").getAsString();
            if (ResultBean.SUCCESS_CODE1.equals(flag)) {
                String topOrderId = object.get("topOrderId").getAsString();
                mView.updateMakeOrderSuccess(topOrderId);
            } else {
                String msg = object.get("msg").getAsString();
                mView.showNotice(msg);
            }
        }

    }

    @Override
    public void loadPayOrderIdTask(@NotNull String json) {
        Log.e(TAG, "loadPayOrderIdTask: json=" + json);
        getRetrofitService().generateTopOrder(json, getPayOrderIdCallBack());
    }

    @Override
    public void loadAliPayTask(@NotNull String orderId) {
        getRetrofitService().goTopOrderAlipay(orderId, new PresentCallBack<CipherPayZhifubaoBean>(CipherPayZhifubaoBean.class) {
            @Override
            protected void successResult(CipherPayZhifubaoBean cipherPayZhifubaoBean) {
                mView.payCipherSuccess(cipherPayZhifubaoBean.getData(), orderId);
            }

            @Override
            protected void fail(String error) {
                //super.fail(error);
                mView.payCipherFai(error);
            }
        });
    }

    @Override
    public void loadWeiXinPayTask(@NotNull String orderId) {
        getRetrofitService().goTopOrderWxpay(orderId, new PresentCallBack<CipherPayZhifubaoBean>(CipherPayZhifubaoBean.class) {
            @Override
            protected void successResult(CipherPayZhifubaoBean cipherPayZhifubaoBean) {
                mView.payCipherSuccess(cipherPayZhifubaoBean.getData(), orderId);
            }

            @Override
            protected void fail(String error) {
                //super.fail(error);
                mView.payCipherFai(error);
            }
        });
    }
}
