package com.alkaid.trip51.model;

/**
 * Created by df on 2015/11/25.
 * 于服务端协议中相关常量
 */
public class NetDataConstants {
    /**是*/
    public static final int TRUE=1;
    /**否*/
    public static final int FALSE=2;

    /**男性*/
    public static final int SEX_MALE=1;
    /**女性*/
    public static final int SEX_FEMALE=2;

    /**时间格式化用*/
    public static final String DATETIME_FORMAT="yyyy-MM-dd hh:mm";
    /**日期格式化用*/
    public static final String DATE_FORMAT="yyyy-MM-dd";

    /**支付状态：成功*/
    public static final int PAY_STATUS_SUCCESS=1;
    /**支付状态：失败*/
    public static final int PAY_STATUS_FAILED=2;
    /**支付状态：待确认*/
    public static final int PAY_STATUS_WAITTING=3;

    /**查询用的订单状态：可使用*/
    public static final int CONDITION_ORDER_STATUS_NORMAL=1;
    /**查询用的订单状态：待付款*/
    public static final int CONDITION_ORDER_STATUS_UNPAY=2;
    /**查询用的订单状态：退款单*/
    public static final int CONDITION_ORDER_STATUS_REFUND=3;
}
