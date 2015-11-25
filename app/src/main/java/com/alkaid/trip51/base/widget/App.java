package com.alkaid.trip51.base.widget;

import com.alkaid.base.view.base.BApp;
import com.alkaid.trip51.dataservice.OrderService;
import com.alkaid.trip51.dataservice.account.AccountService;
import com.alkaid.trip51.dataservice.location.LocationService;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.dataservice.shop.ShopService;

/**
 * @author Alkaid
 */

public class App extends BApp {
    private static final String TAG="App";

    private MApiService mApiService;
    private AccountService accountService;
    private LocationService locationService;
    private ShopService shopService;
    private OrderService orderService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        mApiService=MApiService.create(this);
        accountService=AccountService.create(this);
        locationService=LocationService.create(this);
        shopService=ShopService.create(this);
        orderService=OrderService.create(this);
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
    public static ShopService shopService(){
        return instance().shopService;
    }
    public static OrderService orderService(){
        return instance().orderService;
    }


}
