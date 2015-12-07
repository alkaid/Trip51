package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResComments;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.shop.adapter.EvaluationAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

/**
 * 商店评价列表
 */

/**
 * Created by jyz on 2015/11/8.
 */
public class EvaluationListFragment extends BaseFragment{
    private Shop currShop;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currShop = (Shop) getArguments().getSerializable(ShopDetailActivity.BUNDLE_KEY_SHOP);
        View v=inflater.inflate(R.layout.fragment_evaluation,container,false);
        ListView lvEvaluation = (ListView) v.findViewById(R.id.lv_evaluation);
        lvEvaluation.setAdapter(new EvaluationAdapter(getContext()));
//        loadData();
        return v;
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        loadData();
    }

    private void loadData(){
        if(currShop == null){
            return;
        }
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        unBeSignform.put("shopid",currShop.getShopid()+"");
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag="comments"+(int)(Math.random()*1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL,true,ResComments.class, MApiService.URL_SHOP_COMMENTS, beSignForm, unBeSignform, new Response.Listener<ResComments>() {
            @Override
            public void onResponse(ResComments response) {
                dismissPdg();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error));
            }
        }), tag);
    }
}
