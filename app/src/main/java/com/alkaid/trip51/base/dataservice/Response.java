package com.alkaid.trip51.base.dataservice;


public interface Response
{

    public static final Object SUCCESS = null;

    public abstract Object error();

    public abstract Object result();

}
