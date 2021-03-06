package com.alkaid.trip51.dataservice.mapi;

import android.content.Context;
import android.text.TextUtils;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.pay.Result;
import com.android.volley.Cache;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by df on 2015/11/18.
 */
public class MApiService {
    private static final String TAG="MApiService";
    public static final String PROTOCOL_VERSION="v1";
//    private static final String weburl="http://admeng-huasheng.xicp.net:8005/api/";
    private static final String weburl="http://api.zsysuan.com/api/";
    public static final String URL_SMSCODE=weburl+"register/getsmscode/"+PROTOCOL_VERSION;
    //    public static final String URL_SMSCODE="http://192.168.1.188/test/a.php";
    public static final String URL_MAIN_HOME =weburl+"shop/list/"+PROTOCOL_VERSION;
    public static final String URL_REGISTER=weburl+"register/"+PROTOCOL_VERSION;
    public static final String URL_LOGIN_MOBILE=weburl+"login/mobile/"+PROTOCOL_VERSION;
    public static final String URL_LOGIN_NORMAL=weburl+"login/common/"+PROTOCOL_VERSION;
    public static final String URL_CITY_LIST_BY_PROVINCE=weburl+"city/list/"+PROTOCOL_VERSION;
    public static final String URL_CITY_LIST=weburl+"city/alllist/"+PROTOCOL_VERSION;
    public static final String URL_CITY_HOTLIST=weburl+"city/hotlist/"+PROTOCOL_VERSION;
    public static final String URL_CITY_GETID=weburl+"city/getcityid/"+PROTOCOL_VERSION;
    public static final String URL_SHOP_FOODS=weburl+"shop/foodlist/"+PROTOCOL_VERSION;
    public static final String URL_SHOP_COMMENTS=weburl+"shop/commentlist/"+PROTOCOL_VERSION;
    public static final String URL_SHOP_SHOP_DETAIL=weburl+"shop/detail/"+PROTOCOL_VERSION;
    public static final String URL_BOOKING=weburl+"order/add/"+PROTOCOL_VERSION;
    public static final String URL_PAY=weburl+"order/confirmpay/"+PROTOCOL_VERSION;
    public static final String URL_SHOP_SEARCH=weburl+"shop/search/"+PROTOCOL_VERSION;
    public static final String URL_PAY_STATUS=weburl+"order/paystatus/"+PROTOCOL_VERSION;
    public static final String URL_ORDER_LIST=weburl+"order/list/"+PROTOCOL_VERSION;
    public static final String URL_ORDER_DETAIL=weburl+"order/detail/"+PROTOCOL_VERSION;
    public static final String URL_CONFIG=weburl+"sys/init/"+PROTOCOL_VERSION;
    public static final String URL_USER_COMMENTS=weburl+"comment/list/"+PROTOCOL_VERSION;
    public static final String URL_USER_COLLECTS=weburl+"shop/collectlist/"+PROTOCOL_VERSION;
    public static final String URL_SHOP_CONDITION=weburl+"shop/init/"+PROTOCOL_VERSION;
    public static final String URL_USER_MODIFY_FACE=weburl+"member/modifyheadpic/"+PROTOCOL_VERSION;
    public static final String URL_USER_MODIFY_NICKNAME=weburl+"member/modifynickname/"+PROTOCOL_VERSION;


    public static final int SMSCODE_FOR_REGISTER=1;
    public static final int SMSCODE_FOR_LOGIN=2;
    public static final int SMSCODE_FOR_CHANGE_PWD=3;

    private static MApiService instance;
    private Context context;
    private RequestQueue reqQueue;
    private ImageLoader imageLoader;
    private MApiService(Context context){
        this.context=context;
    }
//    public MApiService instance(){
//        if(null==instance){
//            throw new RuntimeException("instance is null!You must calll create() to init!");
//        }
//        return instance;
//    }

    public static MApiService create(Context context){
        instance=new MApiService(context);
        instance.reqQueue= Volley.newRequestQueue(context);
        return instance;
    }

    public<T> void exec(Request<T> req, String tag){
        //如果tag为空的话，就是用默认TAG
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        reqQueue.add(req);
    }

    //通过各Request对象的Tag属性取消请求
    public void abort(Object tag) {
        if (reqQueue != null) {
            reqQueue.cancelAll(tag);
        }
    }

    public Cache getCache(){
        return reqQueue.getCache();
    }

    public RequestQueue getRequestQueue(){
        return this.reqQueue;
    }

    public ImageLoader getImageLoader() {
        if(null==imageLoader){
            imageLoader = new ImageLoader(reqQueue, LruImageCache.instance());
        }
        return imageLoader;
    }

    public static TradException parseError(VolleyError e){
        LogUtil.w(e);
        Result r=new Result(Result.RQF_EXCEPTION);
        TradException tradException=new TradException(r.getError(),e);
        if(e instanceof MApiError){
            MApiError me=(MApiError)e;
            r=new Result(Result.RQF_SERVER_NOTICE_ERROR);
            r.setExternal("status:" + me.data.getErrorcode() + " msg:" + me.data.getMsg());
            tradException = new TradException(me.data.getMsg(),me);
        }else if((e instanceof NetworkError) || (e instanceof ServerError) || (e instanceof TimeoutError)){
            r=new Result(Result.RQF_NET_ERROR);
            if(null!=e.networkResponse){
                r.setExternal(r.getError()+" httpStatus:"+e.networkResponse.statusCode+",response="+new String(e.networkResponse.data));
            }
            tradException=new TradException(r.getError(),e);
        }
        LogUtil.v(r.toString());
        return tradException;
    }
}
