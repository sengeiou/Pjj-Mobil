package com.pjj.module;

import java.util.List;

public class AreaTypeBean extends ResultBean {
    /**
     * data : {"typeList":[{"typeName":"住宅","typeId":"9d8e201ba4fe426eb8c3e49c85a28e30"},{"typeName":"写字楼","typeId":"419014e518f14eb0adeaa180ceea448f"},{"typeName":"商业","typeId":"2a3675125b524f0597e8cfaf1a81bae1"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<TypeListBean> typeList;

        public List<TypeListBean> getTypeList() {
            return typeList;
        }

        public void setTypeList(List<TypeListBean> typeList) {
            this.typeList = typeList;
        }

        public static class TypeListBean {
            /**
             * typeName : 住宅
             * typeId : 9d8e201ba4fe426eb8c3e49c85a28e30
             */

            private String typeName;
            private String typeId;

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getTypeId() {
                return typeId;
            }

            public void setTypeId(String typeId) {
                this.typeId = typeId;
            }
        }
    }
}
