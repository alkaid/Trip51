package com.alkaid.trip51.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.alkaid.trip51.base.widget.App;

/**
 * Created by alkaid on 2015/11/22.
 */
public class SpUtil {
    public static final String default_sp_name="trip51";
//    public static final String key_mobile="key_mobile";
//    public static final String key_userpwd="key_userpwd";
//    public static final String key_openid="key_openid";
//    public static final String key_accesstoken="key_accesstoken";
//    public static final String key_expiresin="key_expiresin";
//    public static final String key_memberid="key_memberid";
    public static final String key_openinfo="key_openinfo";
    public static final String key_account="key_account";
    public static final String key_provincename="key_provincename";
    public static final String key_cityname="key_cityname";
    public static final String key_cityid="key_cityid";
    public static final String key_coordinates="key_coordinates";
    public static final String key_data_version_citylist="key_data_version_citylist";

    public static SharedPreferences getSp(){
        return App.instance().getSharedPreferences(default_sp_name, Context.MODE_PRIVATE);
    }
    public static void putString(String key,String value){
        getSp().edit().putString(key,value).commit();
    }
    public static String getString(String key,String defValue){
        return getSp().getString(key,defValue);
    }
}
