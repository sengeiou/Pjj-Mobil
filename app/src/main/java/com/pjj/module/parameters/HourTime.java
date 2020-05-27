package com.pjj.module.parameters;

import com.pjj.utils.TextUtils;

/**
 * Created by XinHeng on 2018/11/22.
 * describe：
 */
public class HourTime {
    /**
     * 是否可用
     */
    private boolean enable;
    /**
     * 是否选中
     */
    private boolean select;
    private int hour;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        if (enable)
            select = false;
        this.enable = enable;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        if (enable)
            this.select = select;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return getNextInteger();
    }

    private String getNextInteger() {
        return getHourTime(hour);
    }

    public static String getHourTime(int hour) {
        if (hour == 23) {
            return hour + ":00-00:00";
        } else {
            return TextUtils.format(hour) + ":00-" + TextUtils.format(hour + 1) + ":00";
        }
    }
}
