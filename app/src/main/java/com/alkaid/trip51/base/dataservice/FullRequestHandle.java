package com.alkaid.trip51.base.dataservice;

public interface FullRequestHandle
    extends RequestHandler
{

    public abstract void onRequestProgress(Request request, int i, int j);

    public abstract void onRequestStart(Request request);
}
