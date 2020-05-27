package com.pjj.module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by XinHeng on 2018/12/04.
 * describe：便民信息
 */
public class BianMinBean extends ResultBean implements Parcelable {
    private List<DataBean> data;

    public BianMinBean() {
    }

    protected BianMinBean(Parcel in) {
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<BianMinBean> CREATOR = new Creator<BianMinBean>() {
        @Override
        public BianMinBean createFromParcel(Parcel in) {
            return new BianMinBean(in);
        }

        @Override
        public BianMinBean[] newArray(int size) {
            return new BianMinBean[size];
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
         * peopleInfoId : d89c943eec7611e890a600163e0204d5
         * info : 本店十一大酬宾，消费任一套餐均送价值100元餐具一套，买一送一，多买多送。
         消费满800元客户，可另外参与抽奖活动。
         */

        private String peopleInfoId;
        private String info;
        private String title;

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            peopleInfoId = in.readString();
            info = in.readString();
            title = in.readString();
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

        public String getPeopleInfoId() {
            return peopleInfoId;
        }

        public void setPeopleInfoId(String peopleInfoId) {
            this.peopleInfoId = peopleInfoId;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(peopleInfoId);
            dest.writeString(info);
            dest.writeString(title);
        }
    }
}
