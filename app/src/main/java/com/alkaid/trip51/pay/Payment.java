package com.alkaid.trip51.pay;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by df on 2015/11/26.
 */
public abstract class Payment<T> {
    public static final int MSG_WHAT_PAY_COMPLETE=1;
    private Map<String,PaymentCallback> callbacks=new HashMap<String,PaymentCallback>();
    public void pay(String orderNo,T parsms,PaymentCallback paymentCallback){
        callbacks.put(orderNo,paymentCallback);
    }
    protected void onComplete(String orderNo,Result result){
        PaymentCallback paymentCallback=callbacks.get(orderNo);
        if(paymentCallback!=null){
            paymentCallback.onComplete(result);
        }
        callbacks.remove(orderNo);
    }
}
