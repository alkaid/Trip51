package com.alkaid.trip51.pay;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.model.response.ResPayInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by df on 2015/11/26.
 */
public abstract class Payment {
    public static final int MSG_WHAT_PAY_COMPLETE=1;
    public static final int PAYTYPE_ALI=1;
    public static final int PAYTYPE_WECHAT=2;
    private Map<String,PaymentCallback> callbacks=new HashMap<String,PaymentCallback>();
    public void pay(String orderNo,ResPayInfo.PayInfo parsms,PaymentCallback paymentCallback){
        callbacks.put(orderNo,paymentCallback);
    }
    protected void onComplete(String orderNo,Result result){
        LogUtil.v("paySdk onComplete:orderNo="+orderNo+",result code="+result.getCode()+"result error="+result.getError()+",result orderno="+result.getOrderno());
        PaymentCallback paymentCallback=callbacks.get(orderNo);
        if(paymentCallback!=null){
            paymentCallback.onComplete(result);
        }
        callbacks.remove(orderNo);
    }
}
