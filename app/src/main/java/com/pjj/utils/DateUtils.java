package com.pjj.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by XinHeng on 2018/11/24.
 * describe：日期
 */
public class DateUtils {
    public static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static final SimpleDateFormat sfPoint = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
    public static final SimpleDateFormat sf1 = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA);
    public static final SimpleDateFormat sfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    public static final SimpleDateFormat sfs1 = new SimpleDateFormat("yyyy年MM月dd日HH:mm", Locale.CHINA);

    public static String getNowDay() {
        return sf.format(new Date());
    }

    public static String getSf(Date date) {
        return sf.format(date);
    }

    public static String getSfs(Date date) {
        return sfs.format(date);
    }

    public static String getSfPoint(Date date) {
        return sfPoint.format(date);
    }

    public static String getSm(Date date) {
        return sfm.format(date);
    }

    public static String getSfs1(Date date) {
        return sfs1.format(date);
    }

    public static Date getDate(String s) {
        Date parse = null;
        try {
            parse = sf.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static int compare(Date date1, Date date2) {
        if (null != date1 && null != date2) {
            if (date1.after(date2)) {
                return 1;
            }
            return -1;
        } else
            return 0;
    }

    /**
     * cal2比cal1多的天数
     *
     * @param cal1 《 cal2
     * @param cal2
     * @return
     */
    public static int dayOffset(Calendar cal1, Calendar cal2) {
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {  //不是同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {  //闰年
                    timeDistance += 366;
                } else {  //不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else { //同年
            return day2 - day1;
        }
    }

    /**
     * @param cOld
     * @param cNew
     * @param tag  日期cNew 的小时大于00:00
     * @return
     */
    public static String addDays(Calendar cOld, Calendar cNew, boolean tag) {
        if (cNew.getTimeInMillis() - cOld.getTimeInMillis() <= 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        Calendar calendar = (Calendar) cOld.clone();
        while (true) {
            builder.append(getSf(calendar));
            builder.append(",");
            calendar.add(Calendar.DATE, 1);
            if (compare(calendar, cNew)) {
                if (tag) {
                    builder.append(getSf(calendar));
                }
                break;
            }
        }
        /*int length = builder.length();
        if (length > 0) {
            builder.deleteCharAt(length - 1);
        }*/
        return builder.toString();
    }


    public static boolean compare(Calendar c1, Calendar c2) {
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DATE) == c2.get(Calendar.DATE);
    }

    public static String getSf(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "-" + TextUtils.format(calendar.get(Calendar.MONTH) + 1) + "-" + TextUtils.format(calendar.get(Calendar.DATE));
    }

    public static String getSf1(Date times) {
        return sf1.format(times);
    }

    public static String getSf1(String times) {
        Long timel;
        try {
            timel = Long.parseLong(times);
            return sf1.format(new Date(timel));
        } catch (Exception e) {
            e.printStackTrace();
            return times;
        }
    }
}
