package com.alkaid.trip51.dataservice.shop;

import android.content.Context;

/**
 * Created by df on 2015/11/23.
 * 存放当前
 */
public class ShopService {
    private static ShopService instance;
    private Context context;
    //当前选中项的商店id
    private long currShopid;

    private ShopService(Context context){
        this.context=context;
    }
//    public ShopService instance(){
//        if(null==instance){
//            throw new RuntimeException("instance is null!You must calll create() to init!");
//        }
//        return instance;
//    }

    public static ShopService create(Context context){
        instance=new ShopService(context);
        instance.init();
        return instance;
    }
    private void init(){

    }

    public long getCurrShopid() {
        return currShopid;
    }

    public void setCurrShopid(long currShopid) {
        this.currShopid = currShopid;
    }
}
