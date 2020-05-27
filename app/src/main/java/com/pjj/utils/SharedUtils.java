package com.pjj.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pjj.PjjApplication;

/**
 * Create by xinheng on 2018/10/16。
 * describe：xml存储
 */
public class SharedUtils {
    private static final String XML_NAME = "tlw_pjj";
    public static final String TOKEN = "token";
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String HEAD_IMG = "head_img";
    public static final String USER_NICKNAME = "user_nickname";
    public static final String USE_WELCOME = "use_welcome";
    public static final String JPUSH_ALIAS = "jpush_alias";
    public static final String NEW_GUIDE = "new_guide";
    public static final String USER_TYPE = "user_type";

    /**
     * 保存
     *
     * @param key
     * @return
     */
    public static boolean saveForXml(String key, String value) {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = tagCode.edit();
        edit.putString(key, value);
        return edit.commit();
    }

    public static String getXmlForKey(String key) {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        return tagCode.getString(key, null);
    }

    public static boolean isEffiect() {
        return !TextUtils.isEmpty(getXmlForKey(USER_ID)) && !TextUtils.isEmpty(getXmlForKey(TOKEN));
    }

    /**
     * 清除xml存储
     *
     * @return true-->成功
     */
    public static boolean cleanXml() {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = tagCode.edit();
        edit.clear();
        return edit.commit();
    }

    public static boolean cleanLoginXml() {
        SharedPreferences tagCode = PjjApplication.application.getSharedPreferences(XML_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = tagCode.edit();
        edit.remove(USER_ID);
        edit.remove(TOKEN);
        edit.remove(HEAD_IMG);
        edit.remove(USER_NICKNAME);
        edit.remove(USER_PHONE);
        return edit.commit();
    }

    public static boolean checkLogin() {
        String userId = SharedUtils.getXmlForKey(SharedUtils.USER_ID);
        String token = SharedUtils.getXmlForKey(SharedUtils.TOKEN);
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token)) {
            return true;
        }
        return false;
    }
}
