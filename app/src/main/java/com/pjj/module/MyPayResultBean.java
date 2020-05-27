package com.pjj.module;

/**
 * Created by XinHeng on 2018/12/26.
 * describe：自己服务器返回结果
 */
public class MyPayResultBean extends ResultBean {
    /**
     * responseBody : {"alipay_trade_query_response":{"code":"10000","msg":"Success","buyer_logon_id":"182****8182","buyer_pay_amount":"0.00","buyer_user_id":"2088802640926632","invoice_amount":"0.00","out_trade_no":"20181226153345154580962582024022","point_amount":"0.00","receipt_amount":"0.00","send_pay_date":"2018-12-26 15:34:02","total_amount":"0.01","trade_no":"2018122622001426630514999740","trade_status":"TRADE_SUCCESS"},"sign":"Z3M2GpD61azX8laJmYuJA78gMR+mvUT3Nm5We2XbZponD7XFV0w2oXghzL5NbP34TcytkO5ob0eeQiEzZwGQhsa/XvC3Ar7yOxIz2HjD20p1feOtDk4/fRhtc+Amw3VClYfRVlQz8sThBMaa4XqNhEVKCILR72YSSvk0uLev+lQ2vNaF/JvJAZCU3brZDNYiDrsonpJIAP4XDdPr5pcHp059bXvhSjjhj+2rO0s7iwlzdpG2yELAej58aqPuyRAANyyaPZguZh5ghQKhQngUW3K4FSbhH1e0W4tP/oHpS+RuS+wGo3VfoIAy4RrBpM/NrvzBms7pQ7MI5qCLFEmt1Q=="}
     */

    private String responseBody;

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
