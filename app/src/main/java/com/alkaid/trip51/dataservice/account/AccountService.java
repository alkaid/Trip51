package com.alkaid.trip51.dataservice.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.dataservice.mapi.MApiError;
import com.alkaid.trip51.model.account.Account;
import com.alkaid.trip51.model.account.OpenInfo;
import com.alkaid.trip51.model.response.ResLogin;
import com.alkaid.trip51.model.response.ResponseCode;
import com.alkaid.trip51.model.response.ResponseData;
import com.alkaid.trip51.usercenter.UserLoginActivity;
import com.alkaid.trip51.util.SpUtil;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

/**
 * Created by alkaid on 2015/11/19.
 */
public class AccountService {
    private static AccountService instance;
    private Context context;
    private OpenInfo openInfo;
    private Account account;

    private boolean logined=false;

    private AccountService(Context context){
        this.context=context;
    }
//    public AccountService instance(){
//        if(null==instance){
//            throw new RuntimeException("instance is null!You must calll create() to init!");
//        }
//        return instance;
//    }

    public static AccountService create(Context context){
        instance=new AccountService(context);
        //TODO 暂时写死 应该从缓存中读取用户信息，还是说每次都需要登录。
        instance.init();
        return instance;
    }
    private void init(){
        logined=false;
        boolean accountValidate=false;
        //读取并验证登录信息和用户信息
        do{
            SharedPreferences sp=SpUtil.getSp();
            String accountJson=sp.getString(SpUtil.key_account, null);
            String openinfoJson=sp.getString(SpUtil.key_openinfo,null);
            LogUtil.v("accj:"+accountJson);
            LogUtil.v("opj:"+openinfoJson);
            if(accountJson==null||openinfoJson==null) {
                break;
            }
            Gson gson=new Gson();
            openInfo=gson.fromJson(openinfoJson,OpenInfo.class);
            account=gson.fromJson(accountJson,Account.class);
            if(openInfo.getExpiresin()< SystemClock.uptimeMillis()||openInfo.getAccesstoken()==null||openInfo.getOpenid()==null){
                break;
            }
            accountValidate=true;
        }while (false);
        //用户信息不存在或已经失效
        if(!accountValidate){
            logined=false;
            openInfo=null;
        }else{
            logined=true;
        }

    }

    public void handleLogined(ResLogin resLogin){
        this.account=resLogin.getAccount();
        this.openInfo=resLogin.getOpeninfo();
        logined=true;
        Gson gson=new Gson();
        String accountJson = gson.toJson(account);
        String openInfoJson=gson.toJson(openInfo);
        SharedPreferences sp=SpUtil.getSp();
        sp.edit().putString(SpUtil.key_openinfo,openInfoJson).putString(SpUtil.key_account,accountJson).commit();
    }

    /**
     * 检查是否已登录 若未登录 启动登录页面
     * @param context
     * @return
     */
    public boolean checkLogined(Activity context){
        if(TextUtils.isEmpty(openInfo.getOpenid())){
            context.startActivityForResult(new Intent(context, UserLoginActivity.class), 1);
            return false;
        }
        return true;
    }

    private boolean checkIsNeedRelogin(ResponseData responseData){
        if(!responseData.isSuccess()&&responseData.getErrcode()== ResponseCode.ERROR_NEED_RELOGIN){
            this.account=null;
            this.openInfo=null;
            logined=false;
            return true;
        }
        return false;
    }
    public boolean checkIsNeedRelogin(VolleyError volleyError){
        if(volleyError instanceof MApiError){
            MApiError e=(MApiError)volleyError;
            return checkIsNeedRelogin(e.data);
        }
        return false;
    }


    public boolean isLogined() {
        return logined;
    }

    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    public OpenInfo getOpenInfo() {
        return openInfo;
    }

    public void setOpenInfo(OpenInfo openInfo) {
        this.openInfo = openInfo;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }



}
