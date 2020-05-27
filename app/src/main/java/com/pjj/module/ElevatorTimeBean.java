package com.pjj.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pjj.module.xspad.XspManage;
import com.pjj.utils.JsonUtils;
import com.pjj.utils.TextUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by XinHeng on 2018/11/22.
 * describe：
 */
public class ElevatorTimeBean extends JsonResultBean {
    /**
     * 播放总长
     */
    private int hourAll = 0;

    /**
     * data : {"000000067":{"2018-11-23":[],"2018-11-24":[],"2018-11-22":["9","0","1","2","3","4","5","6","7","8","10","11","12","13","15","14"],"2018-11-27":[],"2018-11-28":[],"2018-11-25":[],"2018-11-26":[]}}
     */
    public ElevatorTimeBean(String json) {
        super(json);
    }

    public void resetHour(int startHour, String startDate, String endDate) {

        if (null != getData() && getData().size() > 0) {
            Set<Map.Entry<String, JsonElement>> entries = getData().entrySet();
            Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonElement> nextScreen = iterator.next();
                JsonElement valueScreen = nextScreen.getValue();//屏幕
                if (null != valueScreen && valueScreen.isJsonObject()) {
                    JsonObject asJsonObject = valueScreen.getAsJsonObject();
                    JsonElement jsonElement = asJsonObject.get(startDate);
                    if (null != jsonElement) {
                        asJsonObject.add(startDate, resetJsonArray(startHour, jsonElement.getAsJsonArray(), true));
                    } else {
                        asJsonObject.add(startDate, createJsonArray(startHour, true));
                    }
                    JsonElement jsonElement1 = asJsonObject.get(endDate);
                    if (null != jsonElement1) {
                        asJsonObject.add(endDate, resetJsonArray(startHour, jsonElement1.getAsJsonArray(), false));
                    } else {
                        asJsonObject.add(endDate, createJsonArray(startHour, false));
                    }
                }
            }
        }
    }

    private JsonArray createJsonArray(int hour, boolean tag) {
        JsonArray jsonArray = new JsonArray();
        if (tag) {
            for (int i = 0; i < hour; i++) {
                jsonArray.add("" + i);
            }
        } else {
            for (int i = hour; i < 24; i++) {
                jsonArray.add("" + i);
            }
        }
        return jsonArray;
    }

    private JsonArray resetJsonArray(int hour, JsonArray array, boolean tag) {
        List<String> listHour = new ArrayList<>(hour);
        if (tag) {
            for (int i = 0; i < hour; i++) {
                listHour.add(i + "");
            }
        } else {
            for (int i = hour; i < 24; i++) {
                listHour.add(i + "");
            }
        }
        Type type = new TypeToken<List<String>>() {
        }.getType();
        List<String> list = JsonUtils.gson.fromJson(array, type);
        List<String> listNew = new ArrayList<>();
        Set set = new HashSet();
        set.addAll(list);
        set.addAll(listHour);
        listNew.addAll(set);
        JsonElement jsonElement = JsonUtils.gson.toJsonTree(listNew, type);
        return jsonElement.getAsJsonArray();
    }

    /**
     * 此前屏幕、此天 单位1总和
     *
     * @param jsonObject       屏幕json
     * @param date             天 yyyy-MM-dd
     * @param selectHours
     * @param timeDiscountBean
     * @return
     */
    private float get$1PriceForDate(JsonObject jsonObject, String date, List<Integer> selectHours, TimeDiscountBean timeDiscountBean, ArrayList<Integer> arrayList) {
        //JsonObject jsonObject = getJsonObject(getData(), screenId);
        float hourSum = 0;
        if (null != jsonObject) {
            JsonArray jsonArray = getJsonArray(jsonObject, date);
            if (null != jsonArray) {
                for (int i = 0; i < selectHours.size(); i++) {
                    int integer = selectHours.get(i);
                    if (!contains(jsonArray, integer)) {
                        ++hourAll;
                        arrayList.add(integer);
                        hourSum += timeDiscountBean.getDiscount(integer);
                    }
                }
            }
        }
        return hourSum;
    }

    private boolean contains(JsonArray jsonArray, int integer) {
        for (int j = 0; j < jsonArray.size(); j++) {
            if (jsonArray.get(j).getAsString().equals(String.valueOf(integer))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param screenId         屏幕
     * @param dates            多天
     * @param selectHours      已选择小时集合
     * @param timeDiscountBean 折扣
     * @return
     */
    public float get$1PriceForDate(String screenId, String dates, List<Integer> selectHours, TimeDiscountBean timeDiscountBean) {
        JsonObject jsonObject = getJsonObject(getData(), screenId);
        float hourSum = 0;
        if (null != jsonObject) {
            String[] split = dates.split(",");
            List<DayHours> listDayHours = new ArrayList<DayHours>();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; null != split && i < split.length; i++) {
                ArrayList<Integer> arrayList = new ArrayList<>();
                hourSum += get$1PriceForDate(jsonObject, split[i], selectHours, timeDiscountBean, arrayList);
                if (arrayList.size() > 0) {
                    DayHours dayHours = new DayHours();
                    dayHours.date = split[i];
                    if (builder.length() > 0) {
                        builder.append("&");
                    }
                    builder.append(split[i]);
                    builder.append("#");
                    addBuilder(builder, arrayList);
                    dayHours.hours = continuityHours(arrayList);
                    listDayHours.add(dayHours);
                }
            }
            String value = builder.toString();
            if (value.length() > 0) {
                hashMapXsp.put(screenId, value);
            }
            hashMap.put(screenId, listDayHours);
        }
        return hourSum;
    }


    private void addBuilder(StringBuilder builder, List<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1)
                builder.append(",");
        }
    }

    public static String appendHours(List<Integer> list) {
        if (TextUtils.isNotEmptyList(list)) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < list.size(); i++) {
                buffer.append(list.get(i));
                if (i != list.size() - 1) {
                    buffer.append(",");
                }
            }
            return buffer.toString();
        }
        return "";
    }

    /**
     * 时间连续删除
     *
     * @param list 已排序好的时间
     * @return
     */
    public static String continuityHours(List<Integer> list) {
        //System.out.println(list);
        int start = -1;
        int end = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Integer next = list.get(i);
            if (start == -1) {
                start = next;
                builder.append(TextUtils.format(start) + ":00-");
                end = start + 1;
                if (i == list.size() - 1) {
                    builder.append(TextUtils.format(end) + ":00");
                }
            } else if (end == next) {
                ++end;
                if (i == list.size() - 1) {
                    builder.append(TextUtils.format(end) + ":00");
                }
            } else {
                builder.append(TextUtils.format(end) + ":00");
                start = next;
                end = start + 1;
                //builder.append(TextUtils.format(start) + ":00-" + change24(end) + ":00");
                builder.append(",");
                if (i != list.size() - 1) {
                    builder.append(TextUtils.format(start) + ":00-");
                } else {
                    builder.append(TextUtils.format(start) + ":00-" + change24(end) + ":00");
                }
            }
        }
        String s = builder.toString();
        //Log.e("TAG", "ss=" + s);
        return s;
    }

    private static String change24(int end) {
        /*if (end == 24) {
            return "00";
        } else */
        {
            return TextUtils.format(end);
        }
    }

    private HashMap<String, List<DayHours>> hashMap;
    /**
     * 提交订单所需数据
     */
    private HashMap<String, String> hashMapXsp;

    public HashMap<String, List<DayHours>> getHashMap() {
        return hashMap;
    }

    public HashMap<String, String> getHashMapXsp() {
        return hashMapXsp;
    }

    public float getAllXspPrice(String dates, List<Integer> selectHours, TimeDiscountBean timeDiscountBean) {
        float sum = 0;
        hourAll = 0;
        List<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> childXsp = XspManage.getInstance().getChildXsp();
        hashMap = new HashMap<>();
        hashMapXsp = new HashMap<>();
        if (TextUtils.isNotEmptyList(selectHours)) {
            for (int i = 0; i < childXsp.size(); i++) {
                List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> screenListBeans = childXsp.get(i);
                for (int j = 0; j < screenListBeans.size(); j++) {
                    ElevatorBean.DataBean.ElevatorListBean.ScreenListBean screenListBean = screenListBeans.get(j);
                    String screenId = screenListBean.getScreenId();
                    float $1PriceForDate = get$1PriceForDate(screenId, dates, selectHours, timeDiscountBean);
                    float discountPrice = screenListBean.getDiscountPrice();
                    sum += (discountPrice * $1PriceForDate);
                }
            }
        }
        return sum;
    }

    public int getHourAll() {
        XspManage.getInstance().setListHashMap(getHashMap());
        XspManage.getInstance().setXspHashMap(getHashMapXsp());
        return hourAll;
    }

    public List<Integer> getAllUseTime() {
        boolean all = false;
        if (null != list) {
            list.clear();
        }
        Set<Map.Entry<String, JsonElement>> entries = getData().entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> next = iterator.next();
            //String key = next.getKey();//000000067 一面屏
            JsonElement value = next.getValue();
            //Log.e("TAG", "getAllUseTime: " + key);
            if (value.isJsonObject()) {
                if (xspTimeData(value.getAsJsonObject())) {
                    all = true;
                    break;
                }
                //System.out.println(list);
            }
        }
        if (null != list && list.size() == 0) {
            all = true;
        }
        //System.out.println("不可用时间：" + all + " " + list);
        ArrayList<Integer> times = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (all) {
                times.add(i);
            } else {
                if (!list.contains(String.valueOf(i))) {
                    times.add(i);
                }
            }
        }
        return times;
    }

    private boolean xspTimeData(JsonObject jsonObject) {
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = entries.iterator();
        boolean all = false;
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> next = iterator.next();
            //String key = next.getKey();//2018-11-23
            JsonElement value = next.getValue();
            //Log.e("TAG", "getAllUseTime-Date: " + key);
            if (value.isJsonArray()) {
                JsonArray asJsonArray = value.getAsJsonArray();
                if (null != asJsonArray) {
                    if (asJsonArray.size() == 0) {
                        all = true;
                        break;
                    } else {
                        if (addTime(asJsonArray)) {
                            all = true;
                            break;
                        }
                    }
                }
            }
        }
        return all;
    }

    private ArrayList<String> list;

    private boolean addTime(JsonArray jsonArray) {
        if (null == list) {
            //System.out.println("null--");
            list = addListForJsonArray(jsonArray);
            return false;
        }
        if (list.size() == 0) {
            return true;
        }
        Iterator<JsonElement> iterator = jsonArray.iterator();
        ArrayList<String> stringList = new ArrayList<>();
        while (iterator.hasNext()) {
            JsonElement next = iterator.next();
            String asString = next.getAsString();
            if (null != asString && list.contains(asString)) {
                //list.remove(asString);
                stringList.add(asString);
            }
            /*if (list.size() == 0) {
                return true;
            }*/
        }
        list = stringList;
        //System.out.println("delete--" + list);
        if (list.size() == 0)
            return true;
        return false;
    }

    private ArrayList<String> addListForJsonArray(JsonArray jsonArray) {
        ArrayList<String> list = new ArrayList<>(jsonArray.size());
        Iterator<JsonElement> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JsonElement next = iterator.next();
            list.add(next.getAsString());
        }
        return list;
    }

    public static class DayHours {
        private String date;
        private String hours;

        public DayHours() {
        }

        public DayHours(String date, String hours) {
            this.date = date;
            this.hours = hours;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHours() {
            return hours;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }
    }
}
