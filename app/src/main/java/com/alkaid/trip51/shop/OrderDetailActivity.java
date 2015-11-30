package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.model.response.ResPayInfo;
import com.alkaid.trip51.model.response.ResPayStatus;
import com.alkaid.trip51.pay.Payment;
import com.alkaid.trip51.pay.PaymentCallback;
import com.alkaid.trip51.pay.PaymentFactory;
import com.alkaid.trip51.pay.Result;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/9.
 */
public class OrderDetailActivity extends BaseActivity {
    public static final String BUNDLE_KEY_ORDERNO="BUNDLE_KEY_ORDERNO";
    private String orderNo;
    private Button btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderNo=getIntent().getStringExtra(BUNDLE_KEY_ORDERNO);
        initTitleBar();
        btnPay= (Button) findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPay.setEnabled(false);
                pay(Payment.PAYTYPE_ALI);
            }
        });
    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        tvTitle.setText("订单详情");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void pay(final int payType){
        //检查登录
        if(!checkLogined()){
            return;
        }
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        unBeSignform.put("outtradeno", orderNo);
        unBeSignform.put("paytype", payType + "");
        final String tag="paysign"+(int)(Math.random()*1000);
//        setDefaultPdgCanceListener(tag);
        getProgressDialog().setCancelable(false);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED, true, ResPayInfo.class, MApiService.URL_PAY, beSignForm, unBeSignform, new Response.Listener<ResPayInfo>() {
            @Override
            public void onResponse(ResPayInfo response) {
                dismissPdg();
                //得到支付信息 调用SDK
                Payment payment = PaymentFactory.instance(payType, context);
                payment.pay(orderNo, response.getPayInfo(payType), new PaymentCallback() {
                    @Override
                    public void onComplete(Result result) {
                        switch (result.getCode()) {
                            case RESULT_CANCELED:
                                toastShort("您已取消支付");
                                break;
                            case RESULT_OK:
                                //TODO 验证支付结果
                                break;
                            default:
                                //TODO　支付失败，暂时toast 需换UI
                                toastShort("支付失败:" + result.getError());
                                break;
                        }
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error));
                checkIsNeedRelogin(error);
            }
        }), tag);
    }

    private void checkPay(){
        //检查登录
        if(!checkLogined()){
            return;
        }
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        unBeSignform.put("outtradeno", orderNo);
        final String tag="checkPayStatus"+(int)(Math.random()*1000);
//        setDefaultPdgCanceListener(tag);
        getProgressDialog().setCancelable(false);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED,true,ResPayStatus.class, MApiService.URL_PAY_STATUS, beSignForm, unBeSignform, new Response.Listener<ResPayStatus>() {
            @Override
            public void onResponse(ResPayStatus response) {
                dismissPdg();
                //TODO 更新UI执行相应跳转
                switch (response.getPaystatus()){
                    case NetDataConstants.PAY_STATUS_SUCCESS:
                        break;
                    case NetDataConstants.PAY_STATUS_FAILED:
                        break;
                    case NetDataConstants.PAY_STATUS_WAITTING:
                        break;
                }
                toastShort(response.getMsg());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error));
                checkIsNeedRelogin(error);
            }
        }), tag);
    }
}
