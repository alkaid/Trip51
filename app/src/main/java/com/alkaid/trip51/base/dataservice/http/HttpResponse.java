package com.alkaid.trip51.base.dataservice.http;

import com.alkaid.trip51.base.dataservice.Response;
import java.util.List;

public interface HttpResponse
    extends Response
{

    public abstract List headers();

    public abstract int statusCode();
}
