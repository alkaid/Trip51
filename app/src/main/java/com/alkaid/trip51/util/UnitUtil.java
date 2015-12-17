package com.alkaid.trip51.util;

import java.util.Formatter;

/**
 * Created by df on 2015/12/17.
 */
public class UnitUtil {
    public static String formatDistance(int mill){
        if(mill<1000){
            return mill+"m";
        }
        float km=((float)mill) / 1000.0f;
        return  String.format("%.2f%s",km,"km");
    }
}
