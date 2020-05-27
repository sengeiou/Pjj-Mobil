package com.pjj.module;

import java.util.List;

public class CancelWhyBean extends ResultBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cancelInfoId : dcd6ce4a788a11e9a45200163e0204d5
         * content : 不想买了
         */

        private String cancelInfoId;
        private String content;

        public String getCancelInfoId() {
            return cancelInfoId;
        }

        public void setCancelInfoId(String cancelInfoId) {
            this.cancelInfoId = cancelInfoId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
