package com.pjj.module;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XinHeng on 2019/01/03.
 * describe：首页轮播图
 */
public class PicBean extends ResultBean {

    /**
     * data : {"1":[{"fileName":"e65093e86c6d268e8941be24bbaaf5b2.jpg","pictrueType":"1","createTime":1547093242000,"appPictureId":"c02064a31f784770a277f4fa6b3e0ce1","pictrueSort":"1","linkUrl":"http://pjjzs.com","pictureName":"全国区域独家代理"},{"fileName":"0f54a60593b4c54482deb977bab15ca9.png","pictrueType":"1","createTime":1547092838000,"appPictureId":"68adc7d1c47e443c9c8c6711be4ca46d","pictrueSort":"2","linkUrl":"1","pictureName":"1"},{"fileName":"8e5d2c043d8c10c5917cdd4b387fa1f7.png","pictrueType":"1","createTime":1547092895000,"appPictureId":"52b752f137d847bc983ab4b69baedc74","pictrueSort":"3","linkUrl":"1","pictureName":"1"},{"fileName":"2875f8dfe56409c43801d004944b4820.png","pictrueType":"1","createTime":1547092901000,"appPictureId":"07f9d271f2e54874983438865e87e6a7","pictrueSort":"4","linkUrl":"1","pictureName":"1"}],"2":[{"fileName":"e65093e86c6d268e8941be24bbaaf5b2.jpg","pictrueType":"1","createTime":1547093242000,"appPictureId":"c02064a31f784770a277f4fa6b3e0ce1","pictrueSort":"1","linkUrl":"http://pjjzs.com","pictureName":"全国区域独家代理"},{"fileName":"0f54a60593b4c54482deb977bab15ca9.png","pictrueType":"1","createTime":1547092838000,"appPictureId":"68adc7d1c47e443c9c8c6711be4ca46d","pictrueSort":"2","linkUrl":"1","pictureName":"1"},{"fileName":"8e5d2c043d8c10c5917cdd4b387fa1f7.png","pictrueType":"1","createTime":1547092895000,"appPictureId":"52b752f137d847bc983ab4b69baedc74","pictrueSort":"3","linkUrl":"1","pictureName":"1"},{"fileName":"2875f8dfe56409c43801d004944b4820.png","pictrueType":"1","createTime":1547092901000,"appPictureId":"07f9d271f2e54874983438865e87e6a7","pictrueSort":"4","linkUrl":"1","pictureName":"1"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("1")
        private List<_$1Bean> _$1;
        @SerializedName("2")
        private List<_$1Bean> _$2;
        @SerializedName("3")
        private List<_$1Bean> _$3;

        public List<_$1Bean> get_$1() {
            return _$1;
        }

        public void set_$1(List<_$1Bean> _$1) {
            this._$1 = _$1;
        }

        public List<_$1Bean> get_$2() {
            return _$2;
        }

        public void set_$2(List<_$1Bean> _$2) {
            this._$2 = _$2;
        }

        public List<_$1Bean> get_$3() {
            return _$3;
        }

        public void set_$3(List<_$1Bean> _$3) {
            this._$3 = _$3;
        }

        public static class _$1Bean {
            /**
             * fileName : e65093e86c6d268e8941be24bbaaf5b2.jpg
             * pictrueType : 1
             * createTime : 1547093242000
             * appPictureId : c02064a31f784770a277f4fa6b3e0ce1
             * pictrueSort : 1
             * linkUrl : http://pjjzs.com
             * pictureName : 全国区域独家代理
             */

            private String fileName;
            private String pictrueType;
            private long createTime;
            private String appPictureId;
            private String pictrueSort;
            private String linkUrl;
            private String pictureName;

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getPictrueType() {
                return pictrueType;
            }

            public void setPictrueType(String pictrueType) {
                this.pictrueType = pictrueType;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getAppPictureId() {
                return appPictureId;
            }

            public void setAppPictureId(String appPictureId) {
                this.appPictureId = appPictureId;
            }

            public String getPictrueSort() {
                return pictrueSort;
            }

            public void setPictrueSort(String pictrueSort) {
                this.pictrueSort = pictrueSort;
            }

            public String getLinkUrl() {
                return linkUrl;
            }

            public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
            }

            public String getPictureName() {
                return pictureName;
            }

            public void setPictureName(String pictureName) {
                this.pictureName = pictureName;
            }
        }

    }
}
