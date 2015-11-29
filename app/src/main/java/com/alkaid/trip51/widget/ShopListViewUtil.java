package com.alkaid.trip51.widget;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.enums.ShopType;
import com.alkaid.trip51.model.response.ResShopList;
import com.alkaid.trip51.shop.MenuActivity;
import com.alkaid.trip51.shop.adapter.ShopListAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by df on 2015/11/27.
 */
public class ShopListViewUtil {
    PullToRefreshListView shopListView;
    ShopListAdapter shopListAdapter;

    public PullToRefreshListView onCreateView(LayoutInflater inflater, View v, final Activity context, final Fragment fragment){
        shopListView = (PullToRefreshListView) v.findViewById(R.id.ptrList);
        shopListAdapter=new ShopListAdapter(context);
        shopListView.setAdapter(shopListAdapter);
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                App.shopService().setCurrShopid(id);    //保存当前shopid
                Intent intent = new Intent(context, MenuActivity.class);
                fragment.startActivity(intent);
            }
        });
        shopListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                String label = SimpleDateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CHINESE).format(new Date());

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("上次更新：" + label);
                final String tag="shoplist"+(int)(Math.random()*1000);
                // Do work to refresh the list here.
                Map<String, String> beSignForm = new HashMap<String, String>();
                Map<String, String> unBeSignform = new HashMap<String, String>();
                unBeSignform.put("cityid", "77");//TODO 深圳77 debug
                unBeSignform.put("shoptype", ShopType.RESTAURANT.code + "");
                unBeSignform.put("location", "2000");
                unBeSignform.put("pageindex", "1");
                unBeSignform.put("pagesize", "20");
                unBeSignform.put("sortid", "default");
                unBeSignform.put("coordinates", App.locationService().getCoordinates());

                //请求商店列表
                App.mApiService().exec(new MApiRequest(CacheType.NORMAL,true,ResShopList.class,MApiService.URL_SHOP_LIST, beSignForm, unBeSignform, new Response.Listener<ResShopList>() {
                    @Override
                    public void onResponse(ResShopList response) {
                        shopListAdapter.setData(response.getData());
                        shopListAdapter.notifyDataSetChanged();
                        //TODO 更新视图
                        shopListView.onRefreshComplete();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        shopListView.getLoadingLayoutProxy().setRefreshingLabel("出错了");
                        shopListView.setShowViewWhileRefreshing(false);
                        shopListView.onRefreshComplete();
                    }
                }), tag);
            }
        });
        // 添加滑动到底部的监听器
        shopListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
            }
        });
        return shopListView;
    }
}
