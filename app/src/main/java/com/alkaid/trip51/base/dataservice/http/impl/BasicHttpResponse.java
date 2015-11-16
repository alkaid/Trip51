package com.alkaid.trip51.base.dataservice.http.impl;

import com.alkaid.trip51.base.dataservice.BasicResponse;
import com.alkaid.trip51.base.dataservice.http.HttpResponse;

import java.util.List;

public class BasicHttpResponse extends BasicResponse
    implements HttpResponse
{

    private List headers;
    private int statusCode;

    public BasicHttpResponse(int statusCode, Object result, List headers, Object error)
    {
        super(result, error);
        this.statusCode = statusCode;
        this.headers = headers;
    }

    public List headers()
    {
        return headers;
    }

    public int statusCode()
    {
        return statusCode;
    }
}
