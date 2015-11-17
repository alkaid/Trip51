package com.alkaid.trip51.base.dataservice.mapi;


import com.alkaid.trip51.base.dataservice.http.HttpResponse;
import com.alkaid.trip51.model.SimpleMsg;

public interface MApiResponse<E,R>
    extends HttpResponse<E,R>
{

//    public static final int STATUS_CODE_CACHED = 209;

    public abstract SimpleMsg message();

    public abstract byte[] rawData();
}
