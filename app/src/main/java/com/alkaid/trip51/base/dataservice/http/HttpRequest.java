package com.alkaid.trip51.base.dataservice.http;

import com.alkaid.trip51.base.dataservice.Request;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.List;

public interface HttpRequest extends Request
{

    public abstract void addHeaders(List<NameValuePair> list);

    public abstract List<NameValuePair> headers();

    public abstract InputStream input();

    public abstract String method();

    public abstract long timeout();
}
