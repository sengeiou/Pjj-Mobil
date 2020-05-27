package com.pjj.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pjj.module.xspad.NewMediaData;
import com.pjj.module.xspad.XspManage;
import com.pjj.utils.JsonUtils;
import com.pjj.utils.Log;
import com.pjj.utils.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by XinHeng on 2019/03/29.
 * describe：
 */
public class TimeObject {
    private String flag;
    private String msg;
    private JsonObject jsonObject;

    public TimeObject(String json) {
        JsonObject jsonObject = JsonUtils.toJsonObject(json);
        flag = jsonObject.get("flag").getAsString();
        if (isSuccess()) {
            this.jsonObject = jsonObject.get("data").getAsJsonObject();
        } else {
            msg = jsonObject.get("msg").getAsString();
        }
    }

    public int[] filter(String dates) {
        int sumDates = 0;
        int sumScreen = 0;
        float allPrice = 0f;
        List<NewMediaData.ScreenBean> screenIdList = XspManage.getInstance().getNewMediaData().getScreenIdList();
        Iterator<NewMediaData.ScreenBean> screenIt = screenIdList.iterator();
        String[] split = dates.split(",");
        List<String> dateList = Arrays.asList(split);
        List<String> canUseDate = new ArrayList<>();
        List<MakeOrderScreenBean.ScreenBean> unUse = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        while (screenIt.hasNext()) {
            NewMediaData.ScreenBean screenBean = screenIt.next();
            int maxCount = screenBean.getMaxAdCount();
            ArrayList<String> days = new ArrayList<>();
            screenBean.setDays(days);
            String screenId = screenBean.getSrceenId();
            canUseDate.clear();
            //screenBean.setPriceDay(0f);
            Iterator<String> dateIt = dateList.iterator();
            while (dateIt.hasNext()) {
                String date = dateIt.next();
                if (canUse(screenId, date, maxCount)) {
                    canUseDate.add(date);
                    ++sumDates;
                    allPrice += screenBean.getPrice();
                    //Log.e("TAG", "time: price=" + screenBean.getPriceDay());
                    screenBean.setPriceDay(screenBean.getPrice());
                    days.add(date);
                } else {
                    MakeOrderScreenBean.ScreenBean bean = new MakeOrderScreenBean.ScreenBean();
                    bean.setScreenId(screenId);
                    bean.setScreenName(screenBean.getScreenName());
                    bean.setOrderTime(date);
                    unUse.add(bean);
                }
            }
            /*if (null != bean.getOrderTime()) {
                String orderTime = bean.getOrderTime();
                orderTime = orderTime.substring(0, orderTime.length() - 1);
                bean.setOrderTime(orderTime);
                unUse.add(bean);
            }*/
            if (canUseDate.size() > 0) {
                //sumDates = +canUseDate.size();
                ++sumScreen;
                map.put(screenId, changeString(canUseDate));
            } else {
                //TODO 可在此删除屏幕
            }
        }
        if (unUse.size() > 0) {
            XspManage.getInstance().getNewMediaData().setUnUseScreenList(unUse);
        } else {
            XspManage.getInstance().getNewMediaData().setUnUseScreenList(null);
        }
        if (map.size() > 0) {
            XspManage.getInstance().getNewMediaData().setDates(JsonUtils.toJsonString(map));
            XspManage.getInstance().getNewMediaData().setPrice(allPrice);
        } else {
            XspManage.getInstance().getNewMediaData().setDates(null);
        }
        return new int[]{sumDates, sumScreen};
    }

    private String getTimes24() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 24; i++) {
            buffer.append(",");
            buffer.append(i);
        }
        buffer.deleteCharAt(0);
        return buffer.toString();
    }

    public String changeString(List<String> list) {
        StringBuffer buffer = new StringBuffer();
        String times24 = getTimes24();
        if (TextUtils.isNotEmptyList(list)) {
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                buffer.append("&");
                buffer.append(s);
                buffer.append("#");
                buffer.append(times24);
            }
            buffer.deleteCharAt(0);
        }
        return buffer.toString();
    }

    public boolean canUse(String screenId, String date, int count) {
        JsonElement jsonElement1 = jsonObject.get(screenId);
        if (null == jsonElement1) {
            return false;
        }
        JsonObject screenJsonObject = jsonElement1.getAsJsonObject();
        JsonElement jsonElement = screenJsonObject.get(date);
        if (null != jsonElement) {
            JsonObject dateJsonObject = jsonElement.getAsJsonObject();
            _$20190329Bean parse = JsonUtils.parse(dateJsonObject.toString(), _$20190329Bean.class);
            List<Integer> userNum = parse.userNum;
            int sum = 0;
            if (TextUtils.isNotEmptyList(userNum)) {
                sum = userNum.get(0);
            }
            //if (sum < 100) {
            if (sum < count) {
                return true;
            }
        }
        return false;
    }

    public boolean canUse(String screenId, String date) {
        return canUse(screenId, date, 100);
    }

    public boolean isSuccess() {
        return ResultBean.SUCCESS_CODE1.equals(flag);
    }

    public String getMsg() {
        return msg;
    }

    public static class _$20190329Bean {
        private List<Integer> userNum;
        private List<String> userTime;

        public List<Integer> getUserNum() {
            return userNum;
        }

        public void setUserNum(List<Integer> userNum) {
            this.userNum = userNum;
        }

        public List<String> getUserTime() {
            return userTime;
        }

        public void setUserTime(List<String> userTime) {
            this.userTime = userTime;
        }
    }
}
