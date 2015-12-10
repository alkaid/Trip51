package com.alkaid.trip51.base.widget;

import com.alkaid.base.view.base.BApp;
import com.alkaid.trip51.dataservice.ConfigService;
import com.alkaid.trip51.dataservice.ServiceListener;
import com.alkaid.trip51.dataservice.OrderService;
import com.alkaid.trip51.dataservice.AccountService;
import com.alkaid.trip51.dataservice.LocationService;
import com.alkaid.trip51.dataservice.ShoppingCartService;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.dataservice.ShopService;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Alkaid
 */

public class App extends BApp {
    private static final String TAG="App";
    public static final String INIT_TAG_APP="INIT_TAG_APP";

    private MApiService mApiService;
    private AccountService accountService;
    private LocationService locationService;
    private ShopService shopService;
    private OrderService orderService;
    private ConfigService configService;
    private ShoppingCartService shoppingCartService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
//        init();
    }

    public void init(ServiceListener serviceListener){
        mApiService=MApiService.create(this);
        accountService=AccountService.create(this);
        locationService=LocationService.create(this);
        shopService=ShopService.create(this);
        orderService=OrderService.create(this);
        configService=ConfigService.create(this);
        shoppingCartService=ShoppingCartService.create(this);
        serviceListener.onComplete(INIT_TAG_APP);
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
    public static ConfigService configService(){return instance().configService;}
    public static ShoppingCartService shoppingCartService(){return instance().shoppingCartService;}

    public void exit() {
        MobclickAgent.onKillProcess(this);
        System.exit(0);
    }
}
