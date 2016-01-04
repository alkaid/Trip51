package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResShopList;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.shop.adapter.ShopListAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/9.
 */
public class MyFavoriteActivity extends BaseActivity {

    private ListView myFavoriteList;

    private ShopListAdapter shopListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        initTitleBar();
        loadData();
        initView();
    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        tvTitle.setText("我的收藏");
        btnRight.setVisibility(View.GONE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        shopListAdapter = new ShopListAdapter(this);
        myFavoriteList = (ListView)findViewById(R.id.lv_my_fav);
        myFavoriteList.setAdapter(shopListAdapter);
    }

    private void loadData(){
        if(!checkLogined()){
            return;
        }
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        final String tag="myFavorites"+(int)(Math.random()*1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL, true, ResShopList.class, MApiService.URL_USER_COLLECTS, beSignForm, unBeSignform, new Response.Listener<ResShopList>() {
            @Override
            public void onResponse(ResShopList response) {
                shopListAdapter.setData(response.getData());
                shopListAdapter.notifyDataSetChanged();
                dismissPdg();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(MApiService.parseError(error));
                checkIsNeedRelogin(error);
            }
        }), tag);
    }

}
