package com.alkaid.trip51.base.dataservice;



public interface RequestHandler
{

    public abstract void onRequestFailed(Request request, Response response);

    public abstract void onRequestFinish(Request request, Response response);
}
