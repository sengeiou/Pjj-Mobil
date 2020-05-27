package com.pjj.db;

import com.pjj.utils.JsonUtils;
import com.pjj.utils.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

@Entity
public class WeatherBean {
    @Id(autoincrement = true)
    private Long id;
    @NotNull //24小时
    private int hour;
    @NotNull //yyyy-MM-dd
    private String date;
    @NotNull
    private String json;
    @NotNull
    private String area;


    @Generated(hash = 1230537668)
    public WeatherBean(Long id, int hour, @NotNull String date, @NotNull String json, @NotNull String area) {
        this.id = id;
        this.hour = hour;
        this.date = date;
        this.json = json;
        this.area = area;
    }

    @Generated(hash = 2015408157)
    public WeatherBean() {
    }


    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHour() {
        return this.hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 7 与17
     *
     * @param date
     * @param hour
     * @return
     */
    public static String getWeatherJson(String date, int hour, String area) {
        int hourCompare;
        if (hour >= 14) {
            hourCompare = 17;
        } else {
            hourCompare = 7;
        }
        QueryBuilder<WeatherBean> queryBuilder = DaoManager.getInstance().getDaoSession().queryBuilder(WeatherBean.class);
        List<WeatherBean> list = queryBuilder.where(WeatherBeanDao.Properties.Date.eq(date))
                .where(WeatherBeanDao.Properties.Hour.eq(hourCompare))
                .where(WeatherBeanDao.Properties.Area.eq(area))
                .list();
        if (TextUtils.isNotEmptyList(list)) {
            return list.get(0).json;
        }
        return null;
    }

    public static void insert(com.pjj.module.WeatherBean weatherBean, String date, int hour, String area) {
        List<WeatherBean> list = DaoManager.getInstance().getDaoSession().queryBuilder(WeatherBean.class)
                .where(WeatherBeanDao.Properties.Area.eq(area))
                .list();
        if (TextUtils.isNotEmptyList(list)) {
            for (int i = 0; i < list.size(); i++) {
                DaoManager.getInstance().getDaoSession().delete(list.get(i));
            }
        }
        int hourCompare;
        if (hour >= 14) {
            hourCompare = 17;
        } else {
            hourCompare = 7;
        }
        WeatherBean bean = new WeatherBean();
        bean.setHour(hourCompare);
        bean.setDate(date);
        bean.setArea(area);
        bean.setJson(JsonUtils.toJsonString(weatherBean));
        DaoManager.getInstance().getDaoSession().insert(bean);
    }
}
