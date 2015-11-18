package com.alkaid.trip51.base.widget;

import com.alkaid.base.view.base.BApp;
import com.alkaid.trip51.dataservice.mapi.MApiService;

/**
 * @author Alkaid
 */

public class App extends BApp {
    private static final String TAG="App";

    private MApiService mApiService;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiService.create(this);
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

}
