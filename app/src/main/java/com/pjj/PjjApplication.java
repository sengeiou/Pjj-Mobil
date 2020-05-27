package com.pjj;

import android.app.Application;
import android.os.Environment;

import com.pjj.crash.CrashHandlerUtil;
import com.pjj.db.DaoManager;
import com.pjj.utils.FileUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.utils.UMUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Create by xinheng on 2018/10/17。
 * describe：Application
 */
public class PjjApplication extends Application {
    /**
     * Application 唯一对象
     */
    public static PjjApplication application;
    public static final String App_Path = Environment.getExternalStorageDirectory().toString() + "/pingjiajia/";
    //全国区域独家代理
    public static final String AD_JOIN = "http://pjjzs.com";
    public static final String Un_Show = "该功能暂未开放\n敬请期待";
    /**
     * 文件下载路径头
     */
    public static final String filePath = BuildConfig.APP_TYPE ? "http://pjj-liftapp.oss-cn-beijing.aliyuncs.com/" : "http://pjj-liftapp-test.oss-cn-beijing.aliyuncs.com/";//测试
    public static final String integralFilePath = BuildConfig.APP_TYPE ? "http://pjj-businessapp.oss-cn-beijing.aliyuncs.com/" : "http://pjj-businessapp-test.oss-cn-beijing.aliyuncs.com/";//测试
    //    public static final String filePath = "http://pjj-liftapp.oss-cn-beijing.aliyuncs.com/";//正式
    public static final String apkPath = "http://pjj-apk-file.oss-cn-beijing.aliyuncs.com/";//安装包下载
    private String userId;
    public static final String VIDOE_PATH = "rtmp://47.92.254.65/live/31110520100578531993";
    private String renZheng;
    /**
     * 状态栏高度
     */
    private int statueHeight;
    private int screenHeight;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        FileUtils.createFolder(App_Path);
        FileUtils.createFolder(App_Path + "photo/");
        FileUtils.createFolder(App_Path + "file/");
        FileUtils.createFolder(App_Path + "speed/");
        FileUtils.createFolder(App_Path + "module/");
        if (!BuildConfig.DEBUG) {
            CrashHandlerUtil.getInstance().init(this);
        }
        initJPush();
        if (BuildConfig.APP_TYPE)
            initUmeng();
    }

    /**
     * 友盟统计
     */
    private void initUmeng() {
        //UMConfigure.init(this, String appkey, String channel, int deviceType, String pushSecret);
        //Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.init(this, "5c3ea956b465f5f3df00112f", "Pjj_Umeng_Android", UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    /**
     * 极光推送初始化
     */
    private void initJPush() {
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);  // 初始化 JPush
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRenZheng() {
        return renZheng;
    }

    public void setRenZheng(String renZheng) {
        this.renZheng = renZheng;
    }

    public int getStatueHeight() {
        return statueHeight;
    }

    public void setStatueHeight(int statueHeight) {
        this.statueHeight = statueHeight;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
