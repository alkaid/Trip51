package com.alkaid.trip51.model.enums;

/**
 * Created by df on 2015/11/20.
 */
public enum SeatType {
    HALL(1,"大厅"),
    LOUNGE(2,"雅座"),
    PRIVATE_ROOM(3,"包间");
    public int code;
    public String desc;
    SeatType(int code,String desc){
        this.code=code;
        this.desc=desc;
    }
    public static SeatType getByCode(int code){
        for(SeatType s:SeatType.values()){
            if(s.code==code){
                return s;
            }
        }
        return null;
    }
}
