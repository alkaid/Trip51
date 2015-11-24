package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResBaseInfo;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 商户菜单明细列表
 */

/**
 * Created by jyz on 2015/11/8.
 */
public class ShopDetailFragment extends BaseFragment{
    private long shopid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shopid=getArguments().getLong(MenuFragment.BUNDLE_KEY_SHOPID);
        View v=inflater.inflate(R.layout.fragment_shop_detail,container,false);
        loadData();
        return v;
    }
    private void loadData(){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        unBeSignform.put("shopid", shopid+"");
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag="shopdetail"+(int)(Math.random()*1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL, MApiService.URL_SHOP_SHOP_DETAIL, beSignForm, unBeSignform, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ResBaseInfo resdata = gson.fromJson(response, ResBaseInfo.class);
                dismissPdg();
                if (resdata.isSuccess()) {
                    //TODO 刷新UI
                } else {
                    //TODO 暂时用handleException 应该换成失败时的正式UI
                    handleException(TradException.create(resdata.getMsg()));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.e(error);
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error));
            }
        }), tag);
    }
}
