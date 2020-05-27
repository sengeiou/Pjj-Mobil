package com.pjj.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Create by xinheng on 2018/11/09。
 * describe：
 */
public class DiyDataBean extends ResultBean implements Parcelable {
    public DiyDataBean() {
    }

    private List<DataBean> data;

    protected DiyDataBean(Parcel in) {
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<DiyDataBean> CREATOR = new Creator<DiyDataBean>() {
        @Override
        public DiyDataBean createFromParcel(Parcel in) {
            return new DiyDataBean(in);
        }

        @Override
        public DiyDataBean[] newArray(int size) {
            return new DiyDataBean[size];
        }
    };

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }

    public static class DataBean implements Parcelable {
        /**
         * high : 110
         * isCircle : 1
         * wide : 110
         * adTempletId : 1
         * templetElementId : 1
         * x : 101
         * y : 221
         * elementType : 1
         * wordStyle : SimHei
         * wordAlign : 1
         * wordSize : 13
         * wordColour : #FEFEFE
         */

        private String high;
        private String isCircle;
        private String wide;
        private String adTempletId;
        private String templetElementId;
        private String x;
        private String y;
        private String elementType;//元素类型 1图片 2文字
        private String wordStyle;
        private String wordAlign;
        private String wordSize;
        private String wordColour;
        private int wordNumber;
        private boolean isHasSelect;
        private String hint;
        private String notice;

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            high = in.readString();
            isCircle = in.readString();
            wide = in.readString();
            adTempletId = in.readString();
            templetElementId = in.readString();
            x = in.readString();
            y = in.readString();
            elementType = in.readString();
            wordStyle = in.readString();
            wordAlign = in.readString();
            wordSize = in.readString();
            wordColour = in.readString();
            wordNumber = in.readInt();
            hint = in.readString();
            notice = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public int getWordNumber() {
            return wordNumber;
        }

        public void setWordNumber(int wordNumber) {
            this.wordNumber = wordNumber;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
        }

        public boolean isHasSelect() {
            return isHasSelect;
        }

        public void setHasSelect(boolean hasSelect) {
            isHasSelect = hasSelect;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getIsCircle() {
            return isCircle;
        }

        public void setIsCircle(String isCircle) {
            this.isCircle = isCircle;
        }

        public String getWide() {
            return wide;
        }

        public void setWide(String wide) {
            this.wide = wide;
        }

        public String getAdTempletId() {
            return adTempletId;
        }

        public void setAdTempletId(String adTempletId) {
            this.adTempletId = adTempletId;
        }

        public String getTempletElementId() {
            return templetElementId;
        }

        public void setTempletElementId(String templetElementId) {
            this.templetElementId = templetElementId;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getElementType() {
            return elementType;
        }

        public void setElementType(String elementType) {
            this.elementType = elementType;
        }

        public String getWordStyle() {
            return wordStyle;
        }

        public void setWordStyle(String wordStyle) {
            this.wordStyle = wordStyle;
        }

        public String getWordAlign() {
            return wordAlign;
        }

        public void setWordAlign(String wordAlign) {
            this.wordAlign = wordAlign;
        }

        public String getWordSize() {
            return wordSize;
        }

        public void setWordSize(String wordSize) {
            this.wordSize = wordSize;
        }

        public String getWordColour() {
            return wordColour;
        }

        public void setWordColour(String wordColour) {
            this.wordColour = wordColour;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(high);
            dest.writeString(isCircle);
            dest.writeString(wide);
            dest.writeString(adTempletId);
            dest.writeString(templetElementId);
            dest.writeString(x);
            dest.writeString(y);
            dest.writeString(elementType);
            dest.writeString(wordStyle);
            dest.writeString(wordAlign);
            dest.writeString(wordSize);
            dest.writeString(wordColour);
            dest.writeInt(wordNumber);
            dest.writeString(hint);
            dest.writeString(notice);
        }
    }
}
