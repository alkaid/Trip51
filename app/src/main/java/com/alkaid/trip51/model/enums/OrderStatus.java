package com.alkaid.trip51.model.enums;

/**
 * Created by df on 2015/11/20.
 */
public enum OrderStatus {
    COMMITED(1,"已提交"),
    PAYED(2,"已支付"),
    CONSUMED(3,"已消费"),
    EXPIRED(4,"已过期"),
    REFUND(5,"已退款");
    public int code;
    public String desc;

    OrderStatus(int code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public static OrderStatus getByCode(int code){
        for (OrderStatus s:OrderStatus.values()){
            if(s.code==code){
                return s;
            }
        }
        return null;
    }
}
