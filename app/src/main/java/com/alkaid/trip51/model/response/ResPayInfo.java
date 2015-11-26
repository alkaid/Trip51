package com.alkaid.trip51.model.response;

import com.alkaid.trip51.pay.Payment;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by df on 2015/11/26.
 */
public class ResPayInfo extends ResponseData{

    private AliPayInfo alipay;
    private WechatPayInfo wechatpay;

    public PayInfo getPayInfo(int payType){
        switch (payType){
            case Payment.PAYTYPE_ALI:
                return this.alipay;
            case Payment.PAYTYPE_WECHAT:
                return this.wechatpay;
        }
        return null;
    }

    public static class PayInfo implements Serializable{}

    public static class AliPayInfo extends PayInfo {
        public String notifyurl;
        public String outtradeno;
        public String partner;
        public String _input_charset;
        public String sign_type;
        public String seller_id;
        public String subject;
        public String payment_type;
        public String total_fee;
        public String body;
        public String sign;
        public String service;
        public String it_b_pay;
    }

    public static class WechatPayInfo extends PayInfo{
        public String sign;
        public String appid;
        public String partnerid;
        public String prepayid;
        @SerializedName("package")
        public String _package;
        public String noncestr;
        public String timestamp;
    }
}
