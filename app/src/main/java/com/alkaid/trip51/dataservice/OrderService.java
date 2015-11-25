package com.alkaid.trip51.dataservice;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.model.account.Account;
import com.alkaid.trip51.model.account.OpenInfo;
import com.alkaid.trip51.model.request.ReqOrderInfo;
import com.alkaid.trip51.model.response.ResLogin;
import com.alkaid.trip51.model.response.ResponseCode;
import com.alkaid.trip51.model.response.ResponseData;
import com.alkaid.trip51.usercenter.UserLoginActivity;
import com.alkaid.trip51.util.SpUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/19.
 */
public class OrderService {
    private static OrderService instance;
    private Context context;
    /**购物车 存放的是订单列表*/
    private Map<String,ReqOrderInfo> cart;

    private OrderService(Context context){
        this.context=context;
    }
//    public OrderService instance(){
//        if(null==instance){
//            throw new RuntimeException("instance is null!You must calll create() to init!");
//        }
//        return instance;
//    }

    public static OrderService create(Context context){
        instance=new OrderService(context);
        //TODO 暂时写死 应该从缓存中读取用户信息，还是说每次都需要登录。
        instance.init();
        return instance;
    }
    private void init(){
        cart=new HashMap<String,ReqOrderInfo>();
    }

    public Map<String, ReqOrderInfo> getCart() {
        return cart;
    }
}
