package com.pjj.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by XinHeng on 2018/12/25.
 * describe：
 */
public class CipherPayZhifubaoBean extends ResultBean {
    /**
     * data : alipay_sdk=alipay-sdk-java-3.1.0&app_id=2018122162669221&biz_content=%7B%22out_trade_no%22%3A%2220181225153451154572329158994831%22%2C%22total_amount%22%3A%220.01%22%2C%22time_expire%22%3A%222018-12-25+15%3A37%22%2C%22subject%22%3A%22%E5%B1%8F%E5%8A%A0%E5%8A%A0%E5%B9%BF%E5%91%8A%E6%8A%95%E6%94%BE%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F47.92.50.83%3A8086%2FalipayNotifyNotice.action&sign=NbitVvVxjMYL6k3KmhEkri%2Baazopea5J5JicRofts29NS7%2B3PaB23shgqw74C7S%2Bo0MOtYZ3M%2F6WxFZXmGGDuQxVAw8KWaD76jRW0rKs9wy0g9w0aY6MS7gAULZnaMvFJHxN2Lo8XlT5VrfDO1Q7c3Kra0wd0ualZkxgKMqofwk5NvL%2BEoD%2FJC433lTwryfuscvsZhf59BLQJJerGf42gPk7O3XeJ92lcQYlvc0S685wpqOtQfBAxl5fRWdB05W2k%2BCvJKy%2B4Q7NwyudrDHqvq1oarmDOubMdDSf3uwKMHJI2eSxJCxU0%2BQnVJVdSgOxt%2BaGXmwH%2B2TRu9XE0UgFzg%3D%3D&sign_type=RSA2&timestamp=2018-12-25+15%3A34%3A52&version=1.0
     */

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
