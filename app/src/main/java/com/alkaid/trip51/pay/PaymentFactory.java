package com.alkaid.trip51.pay;

import android.content.Context;

import com.alkaid.trip51.pay.ali.AlixPayment;

/**
 * Created by alkaid on 2015/11/27.
 */
public class PaymentFactory {


    public static Payment instance(int paytype,Context context){
        switch (paytype){
            case Payment.PAYTYPE_ALI:
                return AlixPayment.getInstance(context);
            case Payment.PAYTYPE_WECHAT:
                return  WXPayment.getInstance(context);
        }
        return null;
    }
}
