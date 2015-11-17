package com.alkaid.trip51.dataservice.mapi;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/18.
 */
public class MApiRequest extends JsonObjectRequest {
    private Map<String,String> beSignForm;
    private Map<String,String> unBeSignform;
    public MApiRequest(String url, Map<String,String> beSignForm, Map<String,String> unBeSignform,Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST,url,null,listener,errorListener);
        this.beSignForm=beSignForm;
        this.unBeSignform=unBeSignform;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params=new HashMap<String,String>();
        //TODO 合并beSignForm和unBeSignForm
        return super.getParams();
    }

}
