package com.pjj.utils;

import java.math.BigDecimal;

/**
 * 计算
 */
public class CalculateUtils {
    public static String mToRealInt(float f) {
        int fi = (int) f;
        if (f - fi == 0f) {
            return String.valueOf(fi);
        }
        return String.valueOf(f);
    }

    public static String m1(float f) {
        BigDecimal bg = new BigDecimal(f);
        //Log.e("TAG", "m1: =" + f);
        float f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        return df.format(f1);
    }

    public static float m1f(float f) {
        BigDecimal bg = new BigDecimal(f);
        //Log.e("TAG", "m1: =" + f);
        float f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }
}
