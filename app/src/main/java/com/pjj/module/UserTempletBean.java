package com.pjj.module;

import com.pjj.PjjApplication;
import com.pjj.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XinHeng on 2018/11/29.
 * describe：
 */
public class UserTempletBean extends ResultBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static List<DataBean> filter(List<DataBean> data) {
        List<DataBean> list = new ArrayList<>();
        if (TextUtils.isNotEmptyList(data)) {
            for (int i = 0; i < data.size(); i++) {
                DataBean dataBean = data.get(i);
                if (null != dataBean && "1".equals(dataBean.status)) {
                    list.add(dataBean);
                }
            }
        }
        return list;
    }

    public static class DataBean {
        /**
         * templetName : 模板一
         * templet_id : 31fdb7afc6504050aeee37f8294e5764
         * templetType : 1
         * fileList : [{"fileName":"12449521a4294db3ac2064c881f5b65b.jpeg","type":"1","filePlace":"1"}]
         * status : 0 //模板状态 0审核中 1审核通过 2审核不通过
         */

        private String templetName;
        private String templet_id;
        private String templetType;  //1  全图片  2全视频 3上视频下图片 4轮播  5上图片下视频 6web链接 urlContent
        private String status;
        private String urlContent;
        private String isTop;
        private List<FileListBean> fileList;

        public String getUrlContent() {
            return urlContent;
        }

        public void setUrlContent(String urlContent) {
            this.urlContent = urlContent;
        }

        public String getIsTop() {
            return isTop;
        }

        public void setIsTop(String isTop) {
            this.isTop = isTop;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }


        public String getTempletName() {
            return templetName;
        }

        public void setTempletName(String templetName) {
            this.templetName = templetName;
        }

        public String getTemplet_id() {
            return templet_id;
        }

        public void setTemplet_id(String templet_id) {
            this.templet_id = templet_id;
        }

        public String getTempletType() {
            return templetType;
        }

        public void setTempletType(String templetType) {
            this.templetType = templetType;
        }

        public List<FileListBean> getFileList() {
            return fileList;
        }

        public SpeedDataBean getSpeedDataBean() {
            return UserTempletBean.DataBean.getSpeedDataBean(fileList);
        }

        public static SpeedDataBean getSpeedDataBean(List<FileListBean> fileList) {
            if (TextUtils.isNotEmptyList(fileList)) {
                ViewSizeBean video = new ViewSizeBean(0, 0, 1, 1);
                video.setType(2);
                ViewSizeBean img = new ViewSizeBean(0, 1, 1, 1);
                img.setType(1);
                ArrayList<ViewSizeBean> viewSizeBeans = new ArrayList<>(2);
                viewSizeBeans.add(video);
                viewSizeBeans.add(img);
                for (int i = 0; i < fileList.size(); i++) {
                    FileListBean bean = fileList.get(i);
                    if ("2".equals(bean.type)) {
                        video.setFileName(bean.fileName);
                    } else {
                        img.setFileName(bean.fileName);
                    }
                }
                SpeedDataBean speedDataBean = new SpeedDataBean();
                speedDataBean.setSize(2);
                speedDataBean.setViewSizeBeanList(viewSizeBeans);
                speedDataBean.setProportionX(1);
                speedDataBean.setProportionY(2);
                return speedDataBean;
            }
            return null;
        }

        public void setFileList(List<FileListBean> fileList) {
            this.fileList = fileList;
        }

        public static class FileListBean {
            /**
             * fileName : 12449521a4294db3ac2064c881f5b65b.jpeg
             * type : 1
             * filePlace : 1
             */

            private String fileName;
            private String type;
            private String filePlace;

            private String getFileType() {
                if ("1".equals(type)) {
                    return "img/";
                } else {
                    return "video/";
                }
            }

            public String getFileUrl() {
                return PjjApplication.filePath + fileName;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getFilePlace() {
                return filePlace;
            }

            public void setFilePlace(String filePlace) {
                this.filePlace = filePlace;
            }
        }
    }
}
