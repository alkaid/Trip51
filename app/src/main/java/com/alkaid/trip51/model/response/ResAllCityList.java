package com.alkaid.trip51.model.response;

import com.alkaid.trip51.model.SimpleCity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by df on 2015/11/24.
 */
public class ResAllCityList extends ResponseData {
    public int versionnum=0;
    public List<Province> provincelist=new ArrayList<>();
    public static class Province implements Serializable{
        public int provinceid=0;
        public String provincename="";
        public List<SimpleCity> citylist=new ArrayList<>();
    }
}
