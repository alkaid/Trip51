package com.alkaid.trip51.model.enums;

/**
 * Created by df on 2015/11/20.
 */
public enum PersonNumType {
    A(1,"1-2人"),
    B(2,"2-4人"),
    C(3,"4-6人"),
    D(4,"6-8人"),
    E(5,"8-12人"),
    F(6,"12人以上");
    public int code;
    public String desc;
    PersonNumType(int code, String desc){
        this.code=code;
        this.desc=desc;
    }
    public static PersonNumType getByOrdinal(int ordinal){
        for(PersonNumType p:PersonNumType.values()){
            if(p.ordinal()==ordinal){
                return p;
            }
        }
        return null;
    }
}
