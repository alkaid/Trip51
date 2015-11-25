package com.alkaid.trip51.booking;

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
import com.alkaid.trip51.base.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.model.request.ReqOrderInfo;
import com.alkaid.trip51.model.response.ResOrder;
import com.alkaid.trip51.model.shop.Food;
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
        App.accountService().checkLogined(this);
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        //构造订单伪数据
        ReqOrderInfo order=new ReqOrderInfo();
        order.setDinnertime(DateUtil.formatDateString(new Date(), NetDataConstants.DATETIME_FORMAT));
        order.setIscontainfood(NetDataConstants.TRUE);
        order.setIsreplaceother(NetDataConstants.FALSE);
        order.setMobile("18575534171");
        order.setOthermobile(null);
        order.setOthersex(NetDataConstants.SEX_FEMALE);
        order.setOtherusername(null);
        order.setPersonnum(4);
        order.setRoomnum(1);
        order.setRoomtype(NetDataConstants.SEAT_TYPE_HALL);
        order.setSex(NetDataConstants.SEX_FEMALE);
        order.setShopid(37);
        order.setFoodamount(23.00f);
        order.setOrderamount(23.00f);
        List<ReqOrderInfo.ReqFood> foods=new ArrayList<ReqOrderInfo.ReqFood>();
        Food orginFood=new Food();
        orginFood.setFoodid(12);
        orginFood.setFoodname("test");
        orginFood.setPrice(23.00f);
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
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL, MApiService.URL_BOOKING, beSignForm, unBeSignform, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ResOrder resdata = gson.fromJson(response, ResOrder.class);
                dismissPdg();
                if (resdata.isSuccess()) {
                    //TODO 刷新UI

                } else {
                    //TODO 暂时用handleException 应该换成失败时的正式UI
                    handleException(TradException.create(resdata.getMsg()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e("statusCode="+error.networkResponse.statusCode,error);
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error));
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
