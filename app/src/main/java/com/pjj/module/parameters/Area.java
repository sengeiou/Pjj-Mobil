package com.pjj.module.parameters;

/**
 * Created by XinHeng on 2018/12/01.
 * describe：
 */
public class Area {
    //{ "lng":"经度", "lat":"维度", "range":"范围（米）", }
    private String lng;
    private String lat;
    private String range;

    public Area(String lng, String lat, String range) {
        this.lng = lng;
        this.lat = lat;
        this.range = range;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }
}
