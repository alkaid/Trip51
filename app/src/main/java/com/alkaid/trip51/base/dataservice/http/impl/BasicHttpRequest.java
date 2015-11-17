package com.alkaid.trip51.base.dataservice.http.impl;

import com.alkaid.trip51.base.dataservice.BasicRequest;
import com.alkaid.trip51.base.dataservice.http.HttpRequest;

import java.io.InputStream;
import java.util.List;

public class BasicHttpRequest extends BasicRequest
    implements HttpRequest
{

    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    private List headers;
    private InputStream input;
    private String method;
    private long timeout;

    public BasicHttpRequest(String url, String method, InputStream inputstream)
    {
        this(url, method, inputstream, null, 0L);
    }

    public BasicHttpRequest(String url, String method, InputStream inputstream, List headers)
    {
        this(url, method, inputstream, headers, 0L);
    }

    public BasicHttpRequest(String url, String method, InputStream inputstream, List headers, long timeout)
    {
        super(url);
        this.method = method;
        input = inputstream;
        this.headers = headers;
        this.timeout = timeout;
    }

    public static HttpRequest httpGet(String url)
    {
        return new BasicHttpRequest(url, "GET", null, null);

        /*android.net.Uri.Builder builder;
        if(mRgcReq != null)
        {
            mMApiService.abort(mRgcReq, this, true);
            mRgcReq = null;
        }
        builder = Uri.parse("http://l.api.dianping.com/rgc.bin").buildUpon();
        builder.appendQueryParameter("impl", String.valueOf(286));
        builder.appendQueryParameter("city", String.valueOf(i));
        builder.appendQueryParameter("lat", String.valueOf(d));
        builder.appendQueryParameter("lng", String.valueOf(d1));
        builder.appendQueryParameter("acc", String.valueOf(j));
        mRgcReq = BasicMApiRequest.mapiGet(builder.build().toString(), CacheType.DISABLED);*/
    }
//    public static transient HttpRequest httpPost(String s, String as[])
//    {
//        return new BasicHttpRequest(s, "POST", new FormInputStream(as), null);
//    }


    public void addHeaders(List headers)
    {
        if(headers != null)
            if(this.headers != null)
                this.headers.addAll(headers);
            else
                this.headers = headers;
    }

    public List headers()
    {
        return headers;
    }

    public InputStream input()
    {
        return input;
    }

    public String method()
    {
        return method;
    }

    public long timeout()
    {
        return timeout;
    }

    public String toString()
    {
        return (new StringBuilder()).append(method).append(": ").append(super.toString()).toString();
    }
}
