// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.base.dataservice;

public class BasicResponse
    implements Response
{

    private Object error;
    private Object result;

    public BasicResponse(Object result, Object error)
    {
        this.result = result;
        this.error = error;
    }

    public Object error()
    {
        return error;
    }

    public Object result()
    {
        return result;
    }
}
