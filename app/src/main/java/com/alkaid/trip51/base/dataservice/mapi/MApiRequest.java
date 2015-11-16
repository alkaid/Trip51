package com.alkaid.trip51.base.dataservice.mapi;

import com.alkaid.trip51.base.dataservice.http.HttpRequest;

import java.io.InputStream;

public interface MApiRequest
    extends HttpRequest
{

    public abstract CacheType defaultCacheType();

    public abstract boolean disableStatistics();

    public abstract InputStream input();
}
