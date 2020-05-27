package com.pjj.utils;

import com.pjj.BuildConfig;

/**
 * Created by xinheng on 2018/10/17.
 * describe：log 打印
 */

public class Log {

    //设为false关闭日志
    private static final boolean LOG_ENABLE = BuildConfig.DEBUG;

    public static void i(String tag, String msg){
        if (LOG_ENABLE){
            android.util.Log.i(tag, msg);
        }
    }
    public static void v(String tag, String msg){
        if (LOG_ENABLE){
            android.util.Log.v(tag, msg);
        }
    }
    public static void d(String tag, String msg){
        if (LOG_ENABLE){
            android.util.Log.e(tag, msg);
        }
    }
    public static void w(String tag, String msg){
        if (LOG_ENABLE){
            android.util.Log.w(tag, msg);
        }
    }
    public static void e(String tag, String msg){
        if (LOG_ENABLE){
            android.util.Log.e(tag, msg);
        }
    }

}
