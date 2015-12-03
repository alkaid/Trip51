package com.alkaid.trip51.model.enums;

/**
 * Created by df on 2015/11/20.
 */
public enum SortType {
    DEFAULT("default","智能排序"),
    REPUTATION("goodcommentrate","评价最好"),
    POPULARITY("popularity","人气最高"),
    DISTANCE("distance","离我最近");
    public String code;
    public String desc;
    SortType(String code, String desc){
        this.code=code;
        this.desc=desc;
    }
}
