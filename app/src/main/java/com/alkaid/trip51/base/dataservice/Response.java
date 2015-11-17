package com.alkaid.trip51.base.dataservice;


public interface Response<E, R>
{

//    public static final Object SUCCESS = null;

    public abstract E error();

    public abstract R result();

}
