package com.alkaid.trip51.base.dataservice;


public abstract class BasicRequest
    implements Request
{

    private String url;

    public BasicRequest(String url)
    {
        this.url = url;
    }

    public String toString()
    {
        return url;
    }

    public String url()
    {
        return url;
    }
}
