package com.alkaid.trip51.dataservice.mapi;

import android.os.SystemClock;
import android.text.TextUtils;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.common.SystemUtil;
import com.alkaid.trip51.base.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.util.SecurityUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/18.
 */
public class MApiRequest extends StringRequest {
    private Map<String,String> beSignForm;
    private Map<String,String> unBeSignform;
    private String id;
    private CacheType cacheType;
    public MApiRequest(String url, Map<String,String> beSignForm, Map<String,String> unBeSignform,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        this(CacheType.NORMAL,url,beSignForm,unBeSignform,listener,errorListener);
    }
    public MApiRequest(CacheType cacheType,String url, Map<String,String> beSignForm, Map<String,String> unBeSignform,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST,url,listener,errorListener);
        this.cacheType=cacheType;
        switch (cacheType){
            case DISABLED:
                setShouldCache(false);
                break;
            //TODO 缓存策略的其他情况
        }
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
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params=new HashMap<String,String>();
        //TODO 合并beSignForm和unBeSignForm
        String sign=signature(beSignForm);
        params.put("sign", sign);
        params.putAll(beSignForm);
        params.putAll(unBeSignform);

        if(LogUtil.D){
            StringBuilder sb=new StringBuilder("params:");
            for (String key:params.keySet()){
                sb.append(key+"="+params.get(key)+"&");
            }
            LogUtil.v(sb.toString());
        }
        return params;
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
