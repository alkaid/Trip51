package com.alkaid.trip51.model.enums;

/**
 * Created by df on 2015/11/20.
 */
public enum ShopType {
    ALL(0,"全部分类"),
    RESTAURANT(1,"餐厅"),
    KTV(2,"KTV"),
    ASS4S(3,"4S店");
    public int code;
    public String desc;
    ShopType(int code,String desc){
        this.code=code;
        this.desc=desc;
    }
}
