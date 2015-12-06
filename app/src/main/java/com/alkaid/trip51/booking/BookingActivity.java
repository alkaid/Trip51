package com.alkaid.trip51.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alkaid.base.common.DateUtil;
import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.model.enums.SeatType;
import com.alkaid.trip51.model.request.ReqOrderInfo;
import com.alkaid.trip51.model.response.ResOrder;
import com.alkaid.trip51.model.shop.Food;
import com.alkaid.trip51.shop.OrderDetailActivity;
import com.alkaid.trip51.widget.Operator;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/7.
 */
public class BookingActivity extends BaseActivity {
    private ViewGroup layContent,laySeat;
    private Operator opNums;
    private LayoutInflater inflater;
    private View btnSure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        initTitleBar();
        btnSure =findViewById(R.id.btnSure);

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booking();
            }
        });
    }


    private void booking(){
        //检查登录
        if(!checkLogined()){
            return;
        }
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        //构造订单伪数据
        final ReqOrderInfo order=new ReqOrderInfo();
        order.setDinnertime(DateUtil.formatDateString(new Date(), NetDataConstants.DATETIME_FORMAT));
        order.setIscontainfood(NetDataConstants.TRUE);
        order.setIsreplaceother(NetDataConstants.FALSE);
        order.setMobile("18575534171");
        order.setOthermobile(null);
        order.setOthersex(NetDataConstants.SEX_FEMALE);
        order.setOtherusername(null);
        order.setPersonnum(4);
        order.setRoomnum(1);
        order.setRoomtype(SeatType.HALL.code);
        order.setSex(NetDataConstants.SEX_FEMALE);
        order.setShopid(39);
        order.setFoodamount(0.01f);
        order.setOrderamount(0.01f);
        List<ReqOrderInfo.ReqFood> foods=new ArrayList<ReqOrderInfo.ReqFood>();
        Food orginFood=new Food();
        orginFood.setFoodid(34);
        orginFood.setFoodname("test");
        orginFood.setPrice(0.01f);
        ReqOrderInfo.ReqFood food=new ReqOrderInfo.ReqFood(orginFood,1);
        foods.add(food);
        order.setFoods(foods);
        String orderJson=new Gson().toJson(order);
        LogUtil.v("orderJson:"+orderJson);
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        unBeSignform.put("orderinfo", orderJson);
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag="booking"+(int)(Math.random()*1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED,false,ResOrder.class, MApiService.URL_BOOKING, beSignForm, unBeSignform, new Response.Listener<ResOrder>() {
            @Override
            public void onResponse(ResOrder response) {
                dismissPdg();
                //TODO 下单成功 刷新UI
                String orderNo=response.getOuttradeno();
                Intent intent=new Intent(context, OrderDetailActivity.class);
                intent.putExtra(OrderDetailActivity.BUNDLE_KEY_ORDERNO,orderNo);
                startActivity(intent);
                finish();
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

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        tvTitle.setText("预订信息");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private static class ViewHolder{
        private ViewGroup lay;
        private ImageView icon;
        private TextView itemName;
        private TextView itemValue;
    }
}
