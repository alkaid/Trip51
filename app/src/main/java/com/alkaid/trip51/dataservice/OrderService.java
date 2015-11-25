package com.alkaid.trip51.dataservice;

import android.content.Context;

import com.alkaid.trip51.model.request.ReqOrderInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/19.
 */
public class OrderService {
    private static OrderService instance;
    private Context context;
    /**购物车 存放的是订单列表*/
    private Map<Integer,ReqOrderInfo> cart;

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
        cart=new HashMap<Integer,ReqOrderInfo>();
    }

    public Map<Integer, ReqOrderInfo> getCart() {
        return cart;
    }
}
