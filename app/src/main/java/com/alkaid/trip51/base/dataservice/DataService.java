package com.alkaid.trip51.base.dataservice;


public interface DataService
{

    public abstract void abort(Request request, RequestHandler requesthandler, boolean flag);

    public abstract void exec(Request request, RequestHandler requesthandler);

    public abstract Response execSync(Request request);
}
