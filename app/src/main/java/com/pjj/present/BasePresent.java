package com.pjj.present;

import com.pjj.contract.BaseView;
import com.pjj.intent.RetrofitService;
import com.pjj.module.ResultBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import retrofit2.Call;

/**
 * Create by xinheng on 2018/10/29 15:16。
 * describe：
 */
public class BasePresent<V extends BaseView> {
    protected final String TAG = "TAG";
    protected V mView;
    protected Call call;
    private ViewInvocationHandler invocationHandler;

    public BasePresent(V view, Class<V> vClass) {
        invocationHandler = new ViewInvocationHandler(view);
        mView = (V) Proxy.newProxyInstance(vClass.getClassLoader(), new Class[]{vClass}, invocationHandler);
    }

    public void recycle() {
        if (null != invocationHandler) {
            invocationHandler.recycle();
            invocationHandler = null;
        }
        if (null != call) {
            call.cancel();
            call = null;
        }
        //mView = null;
    }

    public void cancelInternet() {
        if (null != call) {
            call.cancel();
        }
    }

    public void internetFail(String msg) {
        mView.cancelWaiteStatue();
        mView.showNotice(msg);
    }

    private class ViewInvocationHandler implements InvocationHandler {
        private V view;

        public ViewInvocationHandler(V view) {
            this.view = view;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (null != view) {
                try {
                    return method.invoke(view, args);
                } catch (InvocationTargetException e) {
                    throw e.getCause();
                }
            }
            return null;
        }

        public void recycle() {
            view = null;
        }
    }

    RetrofitService getRetrofitService() {
        return RetrofitService.getInstance();
    }

    protected abstract class PresentCallBack<T extends ResultBean> extends RetrofitService.CallbackClassResult<T> {
        public PresentCallBack(Class<T> aClass) {
            super(aClass);
        }

        @Override
        protected void fail(String error) {
            super.fail(error);
            mView.showNotice(error);
        }
    }
}