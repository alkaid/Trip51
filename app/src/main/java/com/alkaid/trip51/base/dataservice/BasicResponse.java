package com.alkaid.trip51.base.dataservice;

public class BasicResponse<E, R>
    implements Response<E, R>
{

    private E error;
    private R result;

    public BasicResponse(R result, E error)
    {
        this.result = result;
        this.error = error;
    }

    public E error()
    {
        return error;
    }

    public R result()
    {
        return result;
    }
}
