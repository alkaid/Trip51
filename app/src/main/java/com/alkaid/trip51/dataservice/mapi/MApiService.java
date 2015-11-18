package com.alkaid.trip51.dataservice.mapi;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by df on 2015/11/18.
 */
public class MApiService {
    private static final String TAG="MApiService";
    private static MApiService instance;
    private Context context;
    private RequestQueue reqQueue;
    private MApiService(Context context){
        this.context=context;
    }
    public MApiService instance(){
        if(null==instance){
            throw new RuntimeException("instance is null!You must calll create() to init!");
        }
        return instance;
    }

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

}
