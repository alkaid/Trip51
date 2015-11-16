// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.base.dataservice.mapi;

import com.dianping.dataservice.http.FormInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;

public class MApiFormInputStream extends FormInputStream
{

    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String UTF_8 = "UTF-8";

    public MApiFormInputStream(List list)
    {
        super(list, "UTF-8");
    }

    public transient MApiFormInputStream(String as[])
    {
        super(form(as), "UTF-8");
    }

    private static transient List form(String as[])
    {
        int i = as.length / 2;
        ArrayList arraylist = new ArrayList(i);
        for(int j = 0; j < i; j++)
            arraylist.add(new BasicNameValuePair(as[j * 2], as[1 + j * 2]));

        return arraylist;
    }
}
