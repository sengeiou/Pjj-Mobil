package com.pjj.module;

import com.google.gson.annotations.SerializedName;
import com.pjj.utils.TextUtils;

import java.util.List;

public class MyReleaseBean extends ResultBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderType : 1
         * templet_id : 071156ea247d4946a3c0526dd3816063
         * isScreen : 0
         * title : 测试发布
         * userId : 222
         * freeOrderId : 1000000590618635
         * createTime : 1565089312000
         * identityType : 1
         * isTop : 0
         * fileList : [{"fileName":"6041bc0861b64f1d8742785fdccd2a3b.jpg","type":"1","filePlace":"1"}]
         * status : 1
         */

        private String orderType;
        private String templet_id;
        private String isScreen;
        private String title;
        private String revokeMsg;
        private long revokeTime;
        private String userId;
        private String freeOrderId;
        private long createTime;
        private String identityType;
        private String isTop;
        private String content;
        @SerializedName("status")
        private String statusX;
        private String fileType = "1";
        private List<UserTempletBean.DataBean.FileListBean> fileList;

        public String getFilePath() {
            if (TextUtils.isNotEmptyList(fileList)) {
                fileType = fileList.get(0).getType();
                return fileList.get(0).getFileUrl();
            }
            return null;
        }

        public String getFileType() {
            return fileType;
        }

        public String getRevokeMsg() {
            return revokeMsg;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setRevokeMsg(String revokeMsg) {
            this.revokeMsg = revokeMsg;
        }

        public long getRevokeTime() {
            return revokeTime;
        }

        public void setRevokeTime(long revokeTime) {
            this.revokeTime = revokeTime;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getTemplet_id() {
            return templet_id;
        }

        public void setTemplet_id(String templet_id) {
            this.templet_id = templet_id;
        }

        public String getIsScreen() {
            return isScreen;
        }

        public void setIsScreen(String isScreen) {
            this.isScreen = isScreen;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFreeOrderId() {
            return freeOrderId;
        }

        public void setFreeOrderId(String freeOrderId) {
            this.freeOrderId = freeOrderId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getIdentityType() {
            return identityType;
        }

        public void setIdentityType(String identityType) {
            this.identityType = identityType;
        }

        public String getIsTop() {
            return isTop;
        }

        public void setIsTop(String isTop) {
            this.isTop = isTop;
        }

        public String getStatusX() {
            return statusX;
        }

        public void setStatusX(String statusX) {
            this.statusX = statusX;
        }

        public List<UserTempletBean.DataBean.FileListBean> getFileList() {
            return fileList;
        }

        public void setFileList(List<UserTempletBean.DataBean.FileListBean> fileList) {
            this.fileList = fileList;
        }

    }
}
