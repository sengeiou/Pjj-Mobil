package com.pjj.intent;

import android.util.Log;

import com.pjj.BuildConfig;
import com.pjj.PjjApplication;
import com.pjj.utils.SharedUtils;
import com.pjj.utils.TextUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by XinHeng on 2018/12/03.
 * describe：
 */
public class AppendUrlParamInterceptor implements Interceptor {
    private String token = "1b88b6204aab845b7819fcb86bc2af023";

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        if (TextUtils.isEmpty(token)) {
            token = SharedUtils.getXmlForKey(SharedUtils.TOKEN);
        }
        if (TextUtils.isEmpty(PjjApplication.application.getUserId())) {
            PjjApplication.application.setUserId(SharedUtils.getXmlForKey(SharedUtils.USER_ID));
        }
        if (BuildConfig.DEBUG) {
            Log.e("TAG", "intercept: " + bodyToString(oldRequest.body()));
            Log.e("TAG", "intercept: " + token + ", userId=" + PjjApplication.application.getUserId());
        }
        Request.Builder builder = oldRequest.newBuilder().header("token", this.token);
        if (!TextUtils.isEmpty(PjjApplication.application.getUserId())) {
            builder.header("userId", PjjApplication.application.getUserId());
        }
        Request build = builder.build();
        return chain.proceed(build);
    }

    private static String bodyToString(RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
