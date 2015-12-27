package com.alkaid.trip51.model;

import java.util.List;

/**
 * Created by alkaid on 2015/12/27.
 */
public class AdvBanner {
    public long advid;
    public int advtype;
    public String picurl="";
    public String linkurl="";

    @Override
    public boolean equals(Object o) {
        AdvBanner other=(AdvBanner)o;
        return advid==other.advid && advtype==other.advtype && ( (picurl==null&&other.picurl==null) || (picurl!=null && picurl.equals(other.picurl)) ) && ( (linkurl==null&&other.linkurl==null) || (linkurl!=null && linkurl.equals(other.linkurl)) );
    }

    public static boolean same(List<AdvBanner> orgin,List<AdvBanner> other){
        if(orgin==null && other==null)
            return true;
        if(orgin!=null && other!=null && orgin.size()==other.size()){
            boolean noEqual=false;
            for(int i=0;i<orgin.size();i++){
                if(!orgin.get(i).equals(other.get(i))){
                    noEqual=true;
                    break;
                }
            }
            if(!noEqual)
                return true;
        }
        return false;
    }
}
