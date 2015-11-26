package com.alkaid.trip51.model.response;

/**
 * Created by df on 2015/11/26.
 */
public class ResPayInfo extends ResponseData{

    public static class AliPayInfo{
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
    }

    public static class WechatPayInfo{
        public String sign;
        public String appid;
        public String partnerid;
        public String prepayid;
        public String _package;
        public String noncestr;
        public String timestamp;
    }
}
