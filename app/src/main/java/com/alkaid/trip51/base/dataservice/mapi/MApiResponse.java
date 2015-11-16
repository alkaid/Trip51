// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.base.dataservice.mapi;

import com.dianping.dataservice.http.HttpResponse;
import com.dianping.model.SimpleMsg;

public interface MApiResponse
    extends HttpResponse
{

    public static final int STATUS_CODE_CACHED = 209;

    public abstract SimpleMsg message();

    public abstract byte[] rawData();
}
