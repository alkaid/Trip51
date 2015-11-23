package com.alkaid.trip51.dataservice.shop;

import android.content.Context;

import com.alkaid.trip51.model.account.Account;
import com.alkaid.trip51.model.account.OpenInfo;

/**
 * Created by df on 2015/11/23.
 * 存放当前
 */
public class ShopService {
    private static ShopService instance;
    private Context context;
    //当前选中项的商店id
    private int currShopid;

    private ShopService(Context context){
        this.context=context;
    }
    public ShopService instance(){
        if(null==instance){
            throw new RuntimeException("instance is null!You must calll create() to init!");
        }
        return instance;
    }

    public static ShopService create(Context context){
        instance=new ShopService(context);
        instance.init();
        return instance;
    }
    private void init(){

    }

    public int getCurrShopid() {
        return currShopid;
    }

    public void setCurrShopid(int currShopid) {
        this.currShopid = currShopid;
    }
}
