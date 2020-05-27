package com.pjj.utils;

import android.app.Activity;
import android.content.Intent;

import com.pjj.view.activity.LoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by xinheng on 2018/11/07。
 * describe：
 */
public class UserUtils {
    public static boolean verificaID(String idCardNo) {
        boolean flag = false;

        int length = idCardNo.length();
        if (length == 15) {
            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher m = p.matcher(idCardNo);
            return m.matches();
        } else if (length == 18) {
            String front_17 = idCardNo.substring(0, idCardNo.length() - 1);//号码前17位
            String verify = idCardNo.substring(17, 18);//校验位(最后一位)
            Pattern p = Pattern.compile("^[0-9]*$");
            Matcher m = p.matcher(front_17);
            if (!m.matches()) {
                return false;
            } else {
                flag = checkVerify(front_17, verify);
            }
        }
        return flag;
    }

    /**
     * @param front_17
     * @param verify
     * @return
     * @throws Exception
     */
    private static boolean checkVerify(String front_17, String verify) {
        int[] wi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
        String[] vi = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
        int s = 0;
        for (int i = 0; i < front_17.length(); i++) {
            int ai = Integer.parseInt(front_17.charAt(i) + "");
            s += wi[i] * ai;
        }
        int y = s % 11;
        String v = vi[y];
        if (!(verify.toUpperCase().equals(v))) {
            return false;
        }
        return true;
    }

    /**
     * 检测登录信息
     *
     * @param phone    手机号
     * @param password 密码
     * @return 结果 null->符合规则
     */
    public static String checkLoginInf(String phone, String password) {
        String result = isRightPhone(phone);
        if (null == result) {
            result = isRightPassWord(password);
        }
        return result;
    }

    /**
     * 检测注册信息或忘记密码
     *
     * @param phone    手机号
     * @param password 密码
     * @param code     验证码
     * @return 结果 null->符合规则
     */
    public static String checkRegisterInf(String phone, String password, String code) {
        String result = isRightPhone(phone);
        if (null == result) {
            result = isRightPassWord(password);
            if (null == result) {
                result = isRightCode(code);
            }
        }
        return result;
    }

    /**
     * 手机号验证
     *
     * @param mobil
     * @return
     */
    public static String isRightPhone(String mobil) {
        if (null == mobil || "".equalsIgnoreCase(mobil)) {
            return "手机号不能为空";
        } else {
            if (mobil.length() != 11) {
                return "手机号格式不正确";
            }
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9])|(16[6]))\\d{8}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mobil);
            boolean isMatch = m.matches();
            if (!isMatch) {
                return "手机号格式不正确";
            }
        }
        return null;
    }

    /**
     * 密码验证
     *
     * @param password
     * @return
     */
    private static String isRightPassWord(String password) {
        if (null == password || "".equalsIgnoreCase(password)) {
            return "密码不能为空";
        }
        return null;
    }

    private static String isRightCode(String code) {
        if (null == code || "".equalsIgnoreCase(code)) {
            return "验证码不能为空";
        }
        return null;
    }
}
