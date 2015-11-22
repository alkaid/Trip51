package com.alkaid.trip51.dataservice.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.alkaid.trip51.model.response.ResRegister;
import com.alkaid.trip51.util.SpUtil;

/**
 * Created by alkaid on 2015/11/19.
 */
public class AccountService {
    private static AccountService instance;
    private Context context;
    private ResRegister.OpenInfo openInfo;
    private ResRegister.Account account;

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
        instance.init();
        return instance;
    }
    private void init(){
        logined=false;
        openInfo=new ResRegister.OpenInfo();
        SharedPreferences sp=SpUtil.getSp();
        openInfo.setExpiresin(sp.getLong(SpUtil.key_expiresin, 0));
        openInfo.setAccesstoken(sp.getString(SpUtil.key_accesstoken,null));
        openInfo.setOpenid(sp.getString(SpUtil.key_openid, null));
        //用户信息不存在或已经失效
        if(openInfo.getExpiresin()< SystemClock.uptimeMillis()||openInfo.getAccesstoken()==null||openInfo.getOpenid()==null){
            logined=false;
            openInfo=null;
            return;
        }
        account=new ResRegister.Account();
        account.setMemberid(sp.getString(SpUtil.key_memberid,null));
        account.setMobile(sp.getString(SpUtil.key_mobile,null));

    }


    public boolean isLogined() {
        return logined;
    }
    public void setLogined(boolean logined) {
        this.logined = logined;
    }

}
