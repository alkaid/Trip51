package com.alkaid.trip51.base.dataservice.http.impl;

import com.alkaid.trip51.base.dataservice.BasicResponse;
import com.alkaid.trip51.base.dataservice.http.HttpResponse;

import org.apache.http.NameValuePair;

import java.util.List;

public class BasicHttpResponse<E,R> extends BasicResponse<E,R>
    implements HttpResponse<E,R>
{

    private List headers;
    private int statusCode;

    public BasicHttpResponse(int statusCode, R result, List<NameValuePair> headers, E error)
    {
        super(result, error);
        this.statusCode = statusCode;
        this.headers = headers;
    }

    public List<NameValuePair> headers()
    {
        return headers;
    }

    public int statusCode()
    {
        return statusCode;
    }
}
