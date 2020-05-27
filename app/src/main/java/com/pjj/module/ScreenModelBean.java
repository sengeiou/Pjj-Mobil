package com.pjj.module;

import java.util.List;

public class ScreenModelBean extends ResultBean {

    /**
     * data : [{"materialName":"测试素材文件","fileName":"9ea440be3f5bd7491056bf233409315b.jpg","createTime":1556241738000,"partnerFileId":"bba18f3c67c111e9a45200163e0204d5","userId":"becb2b62670811e9a45200163e0204d5","fileType":"1","sourceType":"素材类型 1自用  2地方自营"}]
     * dataNum : 1
     */

    private int dataNum;
    private List<DataBean> data;

    public int getDataNum() {
        return dataNum;
    }

    public void setDataNum(int dataNum) {
        this.dataNum = dataNum;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * materialName : 测试素材文件
         * fileName : 9ea440be3f5bd7491056bf233409315b.jpg
         * createTime : 1556241738000
         * partnerFileId : bba18f3c67c111e9a45200163e0204d5
         * userId : becb2b62670811e9a45200163e0204d5
         * fileType : 1
         * sourceType : 素材类型 1自用  2地方自营
         */

        private String materialName;
        private String fileName;
        private long createTime;
        private String partnerFileId;
        private String userId;
        private String fileType;
        private String sourceType;
        private String templetType;

        public String getTempletType() {
            return templetType;
        }

        public void setTempletType(String templetType) {
            this.templetType = templetType;
        }

        public String getMaterialName() {
            return materialName;
        }

        public void setMaterialName(String materialName) {
            this.materialName = materialName;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getPartnerFileId() {
            return partnerFileId;
        }

        public void setPartnerFileId(String partnerFileId) {
            this.partnerFileId = partnerFileId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getSourceType() {
            return sourceType;
        }

        public void setSourceType(String sourceType) {
            this.sourceType = sourceType;
        }
    }
}
