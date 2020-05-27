package com.pjj.module.parameters;

import java.util.Iterator;
import java.util.List;

/**
 * Created by XinHeng on 2018/11/22.
 * describe：获取电梯不可用时间
 */
public class ElevatorTime {
    /*{
        orderType = 1;
        screentIds = "000000087,test02,test03";
        selectDate = "2018-11-22,2018-11-23,2018-11-24,2018-11-25,2018-11-26,2018-11-27,2018-11-28";
    }*/
    private int orderType;
    private String screentIds;
    private String selectDate;

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getScreentIds() {
        return screentIds;
    }

    public void setScreentIds(String screentIds) {
        this.screentIds = screentIds;
    }

    public String getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }
    public String listToString(List<String> list){
        Iterator<String> iterator = list.iterator();
        StringBuilder builder=new StringBuilder();
        while (iterator.hasNext()){
            String next = iterator.next();
            builder.append(next);
            builder.append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}
