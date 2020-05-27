package com.pjj.module;

/**
 * Created by XinHeng on 2019/04/12.
 * describe：
 */
public class GoodsExplainBean extends ResultBean {

    /**
     * data : {"phone":"400-8888888","content":"\u2022金币商城所有上架商品，用户可使用自己所获金币进行相应兑换；\n\u2022所兑换的商品不允许做任何形式变更，一经兑换成功，不支持退换操作；\n\u2022请认真填写收货地址，若由于地址没有或不明造成配送问题，平台不承担任何责任；\n\u2022如您在金币商城的体验中所遇到任何疑问，请咨询平台客服400-125-1818"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * phone : 400-8888888
         * content : •金币商城所有上架商品，用户可使用自己所获金币进行相应兑换；
         •所兑换的商品不允许做任何形式变更，一经兑换成功，不支持退换操作；
         •请认真填写收货地址，若由于地址没有或不明造成配送问题，平台不承担任何责任；
         •如您在金币商城的体验中所遇到任何疑问，请咨询平台客服400-125-1818
         */

        private String phone;
        private String content;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
