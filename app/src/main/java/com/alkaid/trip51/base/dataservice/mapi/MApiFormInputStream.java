package com.alkaid.trip51.base.dataservice.mapi;

import com.alkaid.trip51.base.dataservice.http.FormInputStream;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MApiFormInputStream extends FormInputStream
{

    public static final String UTF_8 = "UTF-8";
    public static final String DEFAULT_CHARSET = UTF_8;

    public MApiFormInputStream(List<BasicNameValuePair> list)
    {
        super(list, DEFAULT_CHARSET);
    }

    public MApiFormInputStream(String as[])
    {
        super(form(as), DEFAULT_CHARSET);
    }

    private static List<BasicNameValuePair> form(String as[])
    {
        int i = as.length / 2;
        ArrayList<BasicNameValuePair> arraylist = new ArrayList<BasicNameValuePair>(i);
        for(int j = 0; j < i; j++)
            arraylist.add(new BasicNameValuePair(as[j * 2], as[1 + j * 2]));

        return arraylist;
    }
}
