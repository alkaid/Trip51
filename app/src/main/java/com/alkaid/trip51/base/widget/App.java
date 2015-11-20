package com.alkaid.trip51.base.widget;

import android.content.Intent;
import android.util.Log;

import com.alkaid.base.view.base.BApp;
import com.alkaid.trip51.dataservice.account.AccountService;
import com.alkaid.trip51.dataservice.location.LocationService;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;

import java.util.List;

/**
 * @author Alkaid
 */

public class App extends BApp {
    private static final String TAG="App";

    private MApiService mApiService;
    private AccountService accountService;
    private LocationService locationService;


    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        mApiService=MApiService.create(this);
        accountService=AccountService.create(this);
        locationService=LocationService.create(this);
    }

    /**
     * 获得全局单例
     *
     * @return
     */
    public static App instance() {
        return (App) instance;
    }

    public static MApiService mApiService(){
        return instance().mApiService;
    }
    public static AccountService accountService(){
        return instance().accountService;
    }
    public static LocationService locationService(){
        return instance().locationService;
    }



}
