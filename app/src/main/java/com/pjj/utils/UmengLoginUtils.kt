package com.pjj.utils

import android.app.Activity
import com.pjj.PjjApplication
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import android.widget.Toast
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.UMAuthListener


/**
 * Created by XinHeng on 2019/02/20.
 * describe：友盟登录
 */
object UmengLoginUtils {


    fun initLogin() {
        PlatformConfig.setWeixin("wxf294318db839d271", "f2ede7799c1c02f5ad1bcef8fa16dc2e")
        //豆瓣RENREN平台目前只能在服务器端配置
        //PlatformConfig.setSinaWeibo("732268786", "33ac67b2440e04a80412eb39f815ddf3", "http://sns.whalecloud.com")
        PlatformConfig.setSinaWeibo("732268786", "33ac67b2440e04a80412eb39f815ddf3", "https://api.weibo.com/oauth2/default.html")
        PlatformConfig.setQQZone("101556135", "939dee41dc475968e0d176f32230e06e")
    }

    /**
     * 登录
     */
    fun login(activity: Activity, type: SHARE_MEDIA, authListener: UMAuthListener) {
        //UMShareAPI.init()
        var umShareAPI = UMShareAPI.get(PjjApplication.application)
        var install = umShareAPI.isInstall(activity, type)
        if (install) {
            umShareAPI.getPlatformInfo(activity, type, authListener)
        } else {
            //未安装
            Toast.makeText(PjjApplication.application, type.getName() + "未安装", Toast.LENGTH_SHORT).show()
        }
    }
}
