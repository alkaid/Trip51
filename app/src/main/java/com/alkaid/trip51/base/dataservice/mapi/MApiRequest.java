package com.alkaid.trip51.base.dataservice.mapi;

import com.alkaid.trip51.base.dataservice.http.HttpRequest;
import com.alkaid.trip51.dataservice.mapi.CacheType;

public interface MApiRequest
    extends HttpRequest
{

    public CacheType defaultCacheType();

//    public abstract boolean disableStatistics();

}
