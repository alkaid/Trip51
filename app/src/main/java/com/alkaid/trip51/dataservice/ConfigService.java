package com.alkaid.trip51.dataservice;

import android.content.Context;

import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResConfig;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/12/1.
 */
public class ConfigService {
    public static final String INIT_TAG_GET_CONFIG="INIT_TAG_GET_CONFIG";
    private static ConfigService instance;
    private Context context;
    private ResConfig config;

    private ConfigService(Context context){
        this.context=context;
    }
//    public AccountService instance(){
//        if(null==instance){
//            throw new RuntimeException("instance is null!You must calll create() to init!");
//        }
//        return instance;
//    }

    public static ConfigService create(Context context){
        instance=new ConfigService(context);
        instance.init();
        return instance;
    }
    private void init(){

    }
    public void initConfig(final InitListener initListener){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        final String tag="getConfig"+(int)(Math.random()*1000);
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED,true,ResConfig.class, MApiService.URL_CONFIG, beSignForm, unBeSignform, new Response.Listener<ResConfig>() {
            @Override
            public void onResponse(ResConfig response) {
                config=response;
                initListener.onComplete(INIT_TAG_GET_CONFIG);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                initListener.onComplete(INIT_TAG_GET_CONFIG);
            }
        }), tag);
    }

    public ResConfig getConfig() {
        return config;
    }

    public void setConfig(ResConfig config) {
        this.config = config;
    }
}
