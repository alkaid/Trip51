package com.alkaid.trip51.dataservice.mapi;

import android.os.SystemClock;
import android.text.TextUtils;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.common.SystemUtil;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.model.response.ResponseData;
import com.alkaid.trip51.util.SecurityUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/18.
 */
public class MApiRequest<T extends ResponseData> extends Request<T> {
    private Response.Listener<T> mListener=null;
    protected Map<String,String> beSignForm;
    protected Map<String,String> unBeSignform;
    private String id;
    protected CacheType cacheType;
    protected Map<String, String> params=new HashMap<String,String>();
    protected String cacheKey;
    private Class mResponseClss;
    public MApiRequest(CacheType cacheType,boolean shouldRefreshCache,Class<T> responseClss,String url, Map<String,String> beSignForm, Map<String,String> unBeSignform,Response.Listener<T> listener, Response.ErrorListener errorListener) {
//        super(Method.POST,url,listener,errorListener);
        super(Method.POST, url, errorListener);
        mResponseClss=responseClss;
        mListener = listener;
        setShouldRefreshCache(shouldRefreshCache);
        this.cacheType=cacheType;
        switch (cacheType){
            case DISABLED:
                setShouldCache(false);
                break;
            case SERVICE:
                setShouldCache(true);
                break;
            case NORMAL:
                setShouldCache(true);
                setCacheTime(3600*24*365*10);
                break;
            case DAILY:
                setShouldCache(true);
                setCacheTime(3600*24);
                break;
            case HOURLY:
                setShouldCache(true);
                setCacheTime(3600*24);
                break;
        }
        this.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.beSignForm=beSignForm;
        this.unBeSignform=unBeSignform;
        id=java.util.UUID.randomUUID().toString();
        //设置公共头
        beSignForm.put("version", MApiService.PROTOCOL_VERSION);
        beSignForm.put("timestamp", SystemClock.uptimeMillis()+"");

        unBeSignform.put("phonetype","android");
        unBeSignform.put("accessno", id);
        unBeSignform.put("imei", SystemUtil.getImei(App.instance()));
        unBeSignform.put("appversion",SystemUtil.getSoftVersion(App.instance()));

        // 合并beSignForm和unBeSignForm
        String sign=signature(beSignForm);
        params.put("sign", sign);
        params.putAll(beSignForm);
        params.putAll(unBeSignform);
        if(LogUtil.D){
            LogUtil.v(getUrl());
            StringBuilder sb=new StringBuilder();
            for (String key:params.keySet()){
                sb.append(key+"="+params.get(key)+"&");
            }
            LogUtil.v(sb.toString());
        }

        //设置cachekey
        Map<String, String> cacheKeyParams=new HashMap<String,String>();
        cacheKeyParams.putAll(params);
        //排除公共头里需要参与cacheKey的字段
        cacheKeyParams.remove("timestamp");
        cacheKeyParams.remove("phonetype");
        cacheKeyParams.remove("accessno");
        cacheKeyParams.remove("imei");
        cacheKeyParams.remove("appversion");
        cacheKeyParams.remove("sign");
        cacheKeyParams.remove("openid");
        //排除特别字段
        cacheKeyParams.remove("location");
        cacheKeyParams.remove("coordinates");
        cacheKeyParams.remove("sortid");
        StringBuilder sb=new StringBuilder(getUrl());
        sb.append("?");
        for (String key:cacheKeyParams.keySet()){
            sb.append(key+"="+cacheKeyParams.get(key)+"&");
        }
        cacheKey=sb.toString();
    }

//    public MApiRequest(CacheType disabled, boolean b, String url, Map<String, String> beSignForm, Map<String, String> unBeSignform, Response.Listener<String> listener, Response.ErrorListener errorListener) {
//        super(Method.POST, url, errorListener);
//    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public String getCacheKey() {
//        return super.getCacheKey();
        return cacheKey;
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        LogUtil.v(parsed);
//        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        Gson gson = new Gson();
        T resData= (T) gson.fromJson(parsed,mResponseClss);
        if(resData.isSuccess()){
            removeAllPageCachesExcludeFirstPage(response);
            return Response.success(resData, HttpHeaderParser.parseCacheHeaders(response));
        } else {
            MApiError e=new MApiError(resData);
            LogUtil.e(e);
            return Response.error(e);
        }
    }

    /**
     * 清除除第一页外的所有缓存，用于强制刷新缓存后删除关联缓存，仅对存在多页的缓存有效
     */
    private void removeAllPageCachesExcludeFirstPage(NetworkResponse response){
        LogUtil.v("开始判断是否应该删除其他页缓存,cacheKey="+cacheKey);
        if(response.isFromCache){
            LogUtil.v("response.isFromCache==true，无需删除其他页缓存");
            return;
        }
        if(!this.cacheKey.contains("pageindex=1&")){
            LogUtil.v("不存在页码或当前刷新页不是第一页，无需删除其他页缓存");
            return;
        }
        LogUtil.v("开始从第二页开始逐一删除缓存");
        int i=2;
        Cache cache=App.mApiService().getCache();
        while (true){
            String key=this.cacheKey.replaceFirst("pageindex=%d","pageindex="+i);
            if(cache.get(key)==null){
                LogUtil.v("共删除至第"+i+"页缓存");
                break;
            }
            cache.remove(key);
            i++;
        }

    }

    private String signature(Map<String, String> params){
        //排序签名
        List<String> keys=new ArrayList<String>();
        for (String key : params.keySet()) {
            keys.add(key);
        }
        Collections.sort(keys);
        StringBuffer bf = new StringBuffer();
        for (String key : keys) {
            if(!TextUtils.isEmpty(params.get(key)))
                bf.append(key + params.get(key));
        }
        String signBefore = bf.toString();
//        LogUtil.v("signBefore==" + signBefore);
        String signature = null;
        signature = SecurityUtil.getMD5WithSalt(signBefore);
//        LogUtil.v("signAfter==" + signature);
        return signature;
    }
}
