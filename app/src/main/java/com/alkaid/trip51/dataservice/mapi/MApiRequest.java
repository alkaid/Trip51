package com.alkaid.trip51.dataservice.mapi;

import android.os.SystemClock;
import android.text.TextUtils;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.common.SystemUtil;
import com.alkaid.trip51.base.widget.App;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    public MApiRequest(String url, Map<String,String> beSignForm, Map<String,String> unBeSignform,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST,url,listener,errorListener);
        this.beSignForm=beSignForm;
        this.unBeSignform=unBeSignform;
        id=java.util.UUID.randomUUID().toString();
        //设置公共头
        beSignForm.put("version", MApiService.PROTOCOL_VERSION);
        beSignForm.put("timestamp", SystemClock.uptimeMillis()+"");

        unBeSignform.put("phonetype","android");
        unBeSignform.put("accessno",id);
        unBeSignform.put("imei", SystemUtil.getImei(App.instance()));
        unBeSignform.put("appversion",SystemUtil.getSoftVersion(App.instance()));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params=new HashMap<String,String>();
        //TODO 合并beSignForm和unBeSignForm
        String sign=signature(beSignForm,"3de8d0f8079b1dc3e4397b8c540823e8");
        params.put("sign",sign);
        params.putAll(beSignForm);
        params.putAll(unBeSignform);
        return params;
    }

    private String signature(Map<String, String> params,String secrect){
        //排序签名
        List<String> keys=new ArrayList<String>();
        for (String key : params.keySet()) {
            keys.add(key);
        }
        Collections.sort(keys);
        StringBuffer bf = new StringBuffer(secrect);
        for (String key : keys) {
            if(!TextUtils.isEmpty(params.get(key)))
                bf.append(key + params.get(key));
        }
        bf.append(secrect);
        String signBefore = bf.toString();
        LogUtil.v("signBefore==" + signBefore);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(signBefore.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String signature = null;
        try {
            signature = byte2hex(encryptMD5(signBefore));
        } catch (IOException e) {
            LogUtil.e(e);
        }
        LogUtil.v("signAfter==" + signature);
        return signature;
    }

    private static byte[] encryptMD5(String data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            LogUtil.e(e);
        }
        return bytes;
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

}