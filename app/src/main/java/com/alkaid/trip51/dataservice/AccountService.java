package com.alkaid.trip51.dataservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
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
    public static final int REQUEST_CODE_LOGIN=5001;
    public static final int REQUEST_CODE_REGISTER=5002;
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
       save();
    }

    public void save(){
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
        if(openInfo==null || account==null || TextUtils.isEmpty(openInfo.getOpenid())||TextUtils.isEmpty(account.getMemberid())){
            context.startActivityForResult(new Intent(context, UserLoginActivity.class), REQUEST_CODE_LOGIN);
            return false;
        }
        return true;
    }
    /**
     * 检查是否已登录 若未登录 启动登录页面
     * @param context
     * @return
     */
    public boolean checkLogined(Fragment context){
        if(openInfo==null || account==null || TextUtils.isEmpty(openInfo.getOpenid())||TextUtils.isEmpty(account.getMemberid())){
            context.startActivityForResult(new Intent(context.getContext(), UserLoginActivity.class), REQUEST_CODE_LOGIN);
            return false;
        }
        return true;
    }

    public boolean checkIsNeedRelogin(VolleyError volleyError,Activity context){
        if(checkIsNeedRelogin(volleyError)) {
            context.startActivityForResult(new Intent(context, UserLoginActivity.class), 1);
            return true;
        }
        return false;
    }
    public boolean checkIsNeedRelogin(VolleyError volleyError,Fragment context){
        if(checkIsNeedRelogin(volleyError)) {
            context.startActivityForResult(new Intent(context.getContext(), UserLoginActivity.class), REQUEST_CODE_LOGIN);
            return true;
        }
        return false;
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
    private boolean checkIsNeedRelogin(VolleyError volleyError){
        if(volleyError instanceof MApiError){
            MApiError e=(MApiError)volleyError;
            return checkIsNeedRelogin(e.data);
        }
        return false;
    }

    public boolean isLogined() {
        return logined;
    }
    public OpenInfo getOpenInfo() {
        return openInfo;
    }
    public Account getAccount() {
        return account;
    }




}
