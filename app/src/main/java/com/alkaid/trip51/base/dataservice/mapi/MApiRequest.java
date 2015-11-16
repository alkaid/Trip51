// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.base.dataservice.mapi;

import com.dianping.dataservice.http.HttpRequest;
import java.io.InputStream;

// Referenced classes of package com.dianping.dataservice.mapi:
//            CacheType

public interface MApiRequest
    extends HttpRequest
{

    public abstract CacheType defaultCacheType();

    public abstract boolean disableStatistics();

    public abstract InputStream input();
}
