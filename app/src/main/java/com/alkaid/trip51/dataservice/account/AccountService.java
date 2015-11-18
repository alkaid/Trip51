package com.alkaid.trip51.dataservice.account;

import android.content.Context;

/**
 * Created by alkaid on 2015/11/19.
 */
public class AccountService {
    private static AccountService instance;
    private Context context;

    private boolean logined=false;

    private AccountService(Context context){
        this.context=context;
    }
    public AccountService instance(){
        if(null==instance){
            throw new RuntimeException("instance is null!You must calll create() to init!");
        }
        return instance;
    }

    public static AccountService create(Context context){
        instance=new AccountService(context);
        //TODO 暂时写死 应该从缓存中读取用户信息，还是说每次都需要登录。
        instance.logined=false;
        return instance;
    }


    public boolean isLogined() {
        return logined;
    }
    public void setLogined(boolean logined) {
        this.logined = logined;
    }

}
