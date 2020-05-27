package com.pjj.utils;

import android.annotation.SuppressLint;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.TextView;

import com.pjj.module.AZItemEntity;
import com.pjj.module.BuildingBean;
import com.pjj.module.CityBean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by xinheng on 2018/10/13。
 * describe：文本工具
 */
public class TextUtils {
    public static boolean isEmpty(String s) {
        return android.text.TextUtils.isEmpty(s);
    }

    public static boolean isNotEmptyList(List s) {
        return null != s && s.size() > 0;
    }

    /**
     * 清除null
     *
     * @param s 数据
     * @return 字符串 不为null
     */
    public static String clean(String s) {
        if (null == s) {
            return "";
        }
        return s;
    }

    @SuppressLint("DefaultLocale")
    public static String format(Object i) {
        return String.format("%02d", i);
    }

    public static void compareSmallToMore(List<Integer> list) {
        Collections.sort(list);//默认排序(从小到大)
    }

    /**
     * 排序
     *
     * @param list
     * @param comparator
     * @param <T>
     */
    public static <T> void sort(List<T> list, Comparator<T> comparator) {
        Collections.sort(list, comparator);
    }

    /**
     * 排序数据转换
     *
     * @param dates
     * @return
     */
    public static List<AZItemEntity<BuildingBean.CommunityListBean>> fillData(List<BuildingBean.CommunityListBean> dates) {
        if (null == dates) {
            return null;
        }
        List<AZItemEntity<BuildingBean.CommunityListBean>> sortList = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            BuildingBean.CommunityListBean aDate = dates.get(i);
            AZItemEntity<BuildingBean.CommunityListBean> item = new AZItemEntity<>();
            item.setValue(aDate);
            //汉字转换成拼音
            String pinyin = ChineseCharacters2Spell.getPinYinFirstLetter(aDate.getCommunityName());
            //取第一个首字母
            String letters = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (letters.matches("[A-Z]")) {
                item.setSortLetters(letters.toUpperCase());
            } else {
                item.setSortLetters("#");
            }
            sortList.add(item);
        }
        return sortList;
    }

    public static List<AZItemEntity<CityBean.CityListBean>> fillCityData(List<CityBean.CityListBean> dates) {
        if (null == dates) {
            return null;
        }
        List<AZItemEntity<CityBean.CityListBean>> sortList = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            CityBean.CityListBean aDate = dates.get(i);
            AZItemEntity<CityBean.CityListBean> item = new AZItemEntity<>();
            item.setValue(aDate);
            //汉字转换成拼音
            String pinyin = ChineseCharacters2Spell.getPinYinFirstLetter(aDate.getAreaName());
            //取第一个首字母
            String letters = pinyin.substring(0, 1).toUpperCase();
            // 正则表达式，判断首字母是否是英文字母
            if (letters.matches("[A-Z]")) {
                item.setSortLetters(letters.toUpperCase());
            } else {
                item.setSortLetters("#");
            }
            sortList.add(item);
        }
        return sortList;
    }

    private static <T> void addIndexForAZItemEntity(List<AZItemEntity<T>> dates) {
        for (int i = 0; i < dates.size(); i++) {
            dates.get(i).setIndex(i);
        }
    }

    public static <T> List<AZItemEntity<T>> addFirstLetter(List<AZItemEntity<T>> dates) {
        if (null == dates) {
            return null;
        }
        addIndexForAZItemEntity(dates);
        List<AZItemEntity<T>> firstList = new ArrayList<>();
        String s = null;
        for (int i = 0; i < dates.size(); i++) {
            AZItemEntity<T> communityListBeanAZItemEntity = dates.get(i);
            String sortLetters = communityListBeanAZItemEntity.getSortLetters();
            if (!sortLetters.equals(s)) {
                AZItemEntity<T> e = new AZItemEntity<>();
                e.setSortLetters(sortLetters);
                firstList.add(e);
                s = sortLetters;
            }
            firstList.add(communityListBeanAZItemEntity);
        }
        return firstList;
    }

    public static Object[] calculateElevatovrXspCount(List<BuildingBean.CommunityListBean> list, String hours) {
        int elevator = 0;
        int xsp = 0;
        int hour = 0;
        double price = 0;
        StringBuffer buffer = new StringBuffer();
        try {
            hour = Integer.parseInt(hours);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++) {
            BuildingBean.CommunityListBean communityListBean = list.get(i);
            int elevator_num = 0;
            try {
                elevator_num = Integer.parseInt(communityListBean.getElevatorNum());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            int xsp_num = 0;
            try {
                xsp_num = Integer.parseInt(communityListBean.getScreenNum());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            elevator += elevator_num;
            xsp += xsp_num;
            price += (communityListBean.getPrice() * hour);
            buffer.append(communityListBean.getCommunityId());
            if (i != list.size() - 1) {
                buffer.append(",");
            }
        }
        return new Object[]{elevator, xsp, price, buffer.toString()};
    }

    /**
     * 禁止表情输入
     *
     * @param editText
     */
    public static void banFaceForEditText(EditText editText) {
        banFaceForEditText(editText, -1);
    }

    public static void banFaceForEditText(EditText editText, int maxLength) {
        InputFilter inputFilter = new InputFilter() {
            //Pattern pattern = Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]");
            //Pattern pattern1 = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#¥%……&*（）——+|{}【】‘；：”“’。，、？《》\\[-]");
            Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher = pattern.matcher(charSequence);
                if (!matcher.find()) {
                    return null;
                } else {
                    return "";
                }

            }
        };
        int size = 1;
        if (maxLength > 0) {
            size = 2;
        }
        InputFilter[] filters = new InputFilter[size];
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                filters[i] = inputFilter;
            } else {
                filters[i] = new InputFilter.LengthFilter(maxLength);
            }
        }
        editText.setFilters(filters);
    }

    /**
     * 计算字符串MD5值
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String changeListToString(String tag, List list) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            buffer.append(list.get(i));
            buffer.append(tag);
        }
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }

    public static void setMaxEms(TextView tv, String text, int max) {
        String s = clean(text);
        if (s.length() > max) {
            s = s.substring(0, max - 1) + "...";
        } else {
            s += " ";
        }
        tv.setText(s);
    }
}
