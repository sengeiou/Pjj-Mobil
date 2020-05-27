package com.pjj.module;

import com.google.gson.JsonElement;

import java.util.Iterator;
import java.util.List;

/**
 * Created by XinHeng on 2018/11/24.
 * describe：
 */
public class TimeDiscountBean extends JsonResultBean {

    public TimeDiscountBean(String json) {
        super(json);
    }

    /**
     * 当前时间的折扣
     * @param hour
     * @return
     */
    public float getDiscount(int hour) {
        JsonElement jsonElement = getData().get(String.valueOf(hour));
        if (null != jsonElement) {
            String asString = jsonElement.getAsString();
            float v = 1;
            try {
                v = Float.parseFloat(asString);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return v;
        }
        return 1;
    }

    /**
     * @param hourList 此时间区域内单位1的价格
     * @return
     */
    public float getXspHourDay$1Price(List<Integer> hourList) {
        float sum = 0;
        Iterator<Integer> iterator = hourList.iterator();
        while (iterator.hasNext()) {
            sum += getDiscount(iterator.next());
        }
        return sum;
    }



}
