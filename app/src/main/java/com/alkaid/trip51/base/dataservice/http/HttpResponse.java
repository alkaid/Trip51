package com.alkaid.trip51.base.dataservice.http;

import com.alkaid.trip51.base.dataservice.Response;

import org.apache.http.NameValuePair;

import java.util.List;

public interface HttpResponse<E, R>
    extends Response<E, R>
{

    public abstract List<NameValuePair> headers();

    public abstract int statusCode();
}
