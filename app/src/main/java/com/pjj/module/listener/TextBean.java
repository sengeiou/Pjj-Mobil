package com.pjj.module.listener;

import com.pjj.module.ResultBean;

import java.util.List;

/**
 * Created by XinHeng on 2019/04/17.
 * describe：
 */
public class TextBean extends ResultBean {

    /**
     * code : 1
     * message : 成功
     * data : [{"shop_id":2,"uid":2,"store_name":"物美","excerpt":"北京著名连锁超市","img":"/upload/shop_entry/3/20190331/e6fe231ce5e79145372762aa98277103.jpg","addtime":"2019-03-27 14:44","management":"便利超市","mobile":"15110165271","lat":"39.8649020766","lng":"116.2925655060","follow_count":10,"headsmall":"/upload/time.jpg","address":"北京北京市丰台区北京市丰台区"}]
     */

    private int code;
    private String message;
    private List<HomeCommon> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        setStatus(code + "");
        this.code = code;
    }

    @Override
    public String getStatus() {
        setStatus(ResultBean.SUCCESS_CODE);
        return super.getStatus();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<HomeCommon> getData() {
        return data;
    }

    public void setData(List<HomeCommon> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * shop_id : 2
         * uid : 2
         * store_name : 物美
         * excerpt : 北京著名连锁超市
         * img : /upload/shop_entry/3/20190331/e6fe231ce5e79145372762aa98277103.jpg
         * addtime : 2019-03-27 14:44
         * management : 便利超市
         * mobile : 15110165271
         * lat : 39.8649020766
         * lng : 116.2925655060
         * follow_count : 10
         * headsmall : /upload/time.jpg
         * address : 北京北京市丰台区北京市丰台区
         */

        private int shop_id;
        private int uid;
        private String store_name;
        private String excerpt;
        private String img;
        private String addtime;
        private String management;
        private String mobile;
        private String lat;
        private String lng;
        private int follow_count;
        private String headsmall;
        private String address;

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getExcerpt() {
            return excerpt;
        }

        public void setExcerpt(String excerpt) {
            this.excerpt = excerpt;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getManagement() {
            return management;
        }

        public void setManagement(String management) {
            this.management = management;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public int getFollow_count() {
            return follow_count;
        }

        public void setFollow_count(int follow_count) {
            this.follow_count = follow_count;
        }

        public String getHeadsmall() {
            return headsmall;
        }

        public void setHeadsmall(String headsmall) {
            this.headsmall = headsmall;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
