package com.alkaid.trip51.dataservice.mapi;

import com.alkaid.trip51.model.response.ResponseData;
import com.android.volley.VolleyError;

/**
 * Created by alkaid on 2015/11/29.
 */
public class MApiError extends VolleyError {
    public ResponseData data;
    public MApiError(ResponseData data){
        super(data.getMsg());
        this.data=data;
    }
}
