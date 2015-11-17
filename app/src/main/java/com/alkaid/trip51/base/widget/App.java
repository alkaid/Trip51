package com.alkaid.trip51.base.widget;

import android.text.TextUtils;

import com.alkaid.base.extern.baseView.BaseApp;
import com.alkaid.base.view.base.BApp;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author Alkaid
 */

public class App extends BApp {
    private static final String TAG="App";
    /**
     * 获得全局单例
     *
     * @return
     */
    public static BaseApp instance() {
        return (BaseApp) instance;
    }

    private RequestQueue reqQueue;

    //用于返回全局RequestQueue对象，如果为空则创建它
    public RequestQueue getRequestQueue() {
        if (reqQueue == null)
            reqQueue = Volley.newRequestQueue(instance);
        return reqQueue;
    }

    /**
     * 将Request对象添加进RequestQueue，由于Request有*StringRequest,JsonObjectResquest...等多种类型，所以需要用到*泛型。同时可将*tag作为可选参数以便标示出每一个不同请求
     */

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        //如果tag为空的话，就是用默认TAG
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    //通过各Request对象的Tag属性取消请求
    public void cancelPendingRequests(Object tag) {
        if (reqQueue != null) {
            reqQueue.cancelAll(tag);
        }
    }


}
