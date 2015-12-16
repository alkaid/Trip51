package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.alkaid.trip51.model.enums.OrderStatus;
import com.alkaid.trip51.model.enums.SeatType;
import com.alkaid.trip51.model.response.ResOrderDetail;
import com.alkaid.trip51.model.response.ResPayInfo;
import com.alkaid.trip51.model.response.ResPayStatus;
import com.alkaid.trip51.model.shop.Food;
import com.alkaid.trip51.model.shop.Shop;
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
    private ResOrderDetail orderDetail;
    private TextView tvShopName,tvInfo,tvOrderNo,tvTotal;
    private ViewGroup layFoodList,layShop,layOrderDetail;
    private Button btnPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderNo=getIntent().getStringExtra(BUNDLE_KEY_ORDERNO);
        initTitleBar();
        findView();
        resetView();
        loadData();
    }

    private void findView(){
        tvShopName= (TextView) findViewById(R.id.tvShopName);
        tvInfo= (TextView) findViewById(R.id.tvInfo);
        tvOrderNo= (TextView) findViewById(R.id.tvOrderNo);
        tvTotal= (TextView) findViewById(R.id.tvTotal);
        btnPay= (Button) findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPay.setEnabled(false);
                pay(Payment.PAYTYPE_ALI);
            }
        });
        layFoodList= (ViewGroup) findViewById(R.id.layFoodList);
        layShop= (ViewGroup) findViewById(R.id.layShop);
        layOrderDetail= (ViewGroup) findViewById(R.id.layOrderDetail);
    }
    private void resetView(){
        tvShopName.setText("");
        tvInfo.setText("");
        tvOrderNo.setText("");
        tvTotal.setText("");
        layFoodList.removeAllViews();
        btnPay.setEnabled(false);
    }
    private void loadData(){
        //检查登录
        if(!checkLogined()){
            return;
        }
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        unBeSignform.put("orderno", orderNo);
        final String tag="orderDetail"+(int)(Math.random()*1000);
//        setDefaultPdgCanceListener(tag);
        getProgressDialog().setCancelable(false);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED, true, ResOrderDetail.class, MApiService.URL_ORDER_DETAIL, beSignForm, unBeSignform, new Response.Listener<ResOrderDetail>() {
            @Override
            public void onResponse(ResOrderDetail response) {
                dismissPdg();
                orderDetail = response;
                updateView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error.getMessage(),error));
                checkIsNeedRelogin(error);
                btnPay.setEnabled(false);
            }
        }), tag);
    }

    private void updateView(){
        tvShopName.setText(orderDetail.shopname);
//        预订时间：2015-08-11（周二）18：00\n预订信息：包房/4人\n用户信息：高先生  18976765423\n支付方式：货到付款
        StringBuilder sb=new StringBuilder();
        sb.append("预订时间：").append(orderDetail.dinnerdate)
                .append("\n预订信息：").append(orderDetail.roomtype>0? SeatType.getByCode(orderDetail.roomtype).desc:"").append("/").append(orderDetail.personnum)
                .append("人\n用户信息：");
        if(orderDetail.isinstead==NetDataConstants.TRUE){
            sb.append(orderDetail.otherusername).append("  ").append(orderDetail.othermobile);
        }else{
            sb.append(App.accountService().getAccount().getRealname()).append("  ").append(App.accountService().getAccount().getMobile());
        }
//        sb.append("\n支付方式:").append(orderDetail.)
        tvInfo.setText(sb.toString());
        tvOrderNo.setText(orderDetail.orderno);
        tvTotal.setText(orderDetail.orderamount+"");
        //菜单
        LayoutInflater inflater=LayoutInflater.from(context);
        for(Food f:orderDetail.foods) {
            View p= inflater.inflate(R.layout.order_detail_food_item, layFoodList, false);
            TextView tvFoodName,tvPrice,tvCount,tvAmount;
            tvFoodName= (TextView) p.findViewById(R.id.tvFoodName);
            tvPrice= (TextView) p.findViewById(R.id.tvPrice);
            tvCount= (TextView) p.findViewById(R.id.tvCount);
            tvAmount= (TextView) p.findViewById(R.id.tvAmount);
            tvFoodName.setText(f.getFoodname());
            tvPrice.setText(f.getPrice()+"");
            tvCount.setText(f.getFoodNum()+"");
            tvAmount.setText(f.getPrice()*f.getFoodNum()+"");
            layFoodList.addView(p);
        }
        //判断订单状态 更新按钮
        if(orderDetail.orderstatus== OrderStatus.COMMITED.code){
            btnPay.setEnabled(true);
        }else{
            btnPay.setEnabled(false);
            btnPay.setText(OrderStatus.getByCode(orderDetail.orderstatus).desc);
        }
        //跳转
        layShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ShopDescActivity.class);
                Shop shop=new Shop();
                shop.setShopid(orderDetail.shopid);
                intent.putExtra(ShopDetailActivity.BUNDLE_KEY_SHOP,shop);
                startActivity(intent);
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
                            case Result.RQF_PAY_USER_CANCEL:
                                toastShort("您已取消支付");
                                btnPay.setEnabled(true);
                                break;
                            case Result.RQF_ORDER_SUCCESS:
                                //TODO 验证支付结果
                                checkPay();
                                break;
                            default:
                                //TODO　支付失败，暂时toast 需换UI
                                toastShort("支付失败:" + result.getError());
                                btnPay.setEnabled(true);
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
                handleException(new TradException(error.getMessage(),error));
                checkIsNeedRelogin(error);
                btnPay.setEnabled(true);
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
                        toastLong("支付成功！");
                        finish();
                        break;
                    case NetDataConstants.PAY_STATUS_FAILED:
                        toastLong("抱歉，支付失败！请稍后重试");
                        break;
                    case NetDataConstants.PAY_STATUS_WAITTING:
                        toastLong("订单支付状态还在确认中，请稍后再查看该订单状态.");
                        break;
                }
                if(!TextUtils.isEmpty(response.getMsg()))
                    toastLong(response.getMsg());
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error.getMessage(),error));
                checkIsNeedRelogin(error);
            }
        }), tag);
    }
}
