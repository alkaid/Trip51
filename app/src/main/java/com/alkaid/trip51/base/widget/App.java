package com.alkaid.trip51.base.widget;

import com.alkaid.base.view.base.BApp;
import com.alkaid.trip51.dataservice.account.AccountService;
import com.alkaid.trip51.dataservice.mapi.MApiService;

/**
 * @author Alkaid
 */

public class App extends BApp {
    private static final String TAG="App";

    private MApiService mApiService;
    private AccountService accountService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        mApiService=MApiService.create(this);
        accountService=AccountService.create(this);
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

}
