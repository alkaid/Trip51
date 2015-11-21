package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.enums.ShopType;
import com.alkaid.trip51.model.response.ResShopList;
import com.alkaid.trip51.shop.adapter.ShopListAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopListFragment extends BaseFragment {
    PullToRefreshListView ptrlv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shop_list_fragment,container,false);
        ptrlv= (PullToRefreshListView) v.findViewById(R.id.ptrList);
        ptrlv.setAdapter(new ShopListAdapter(getActivity()));
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(app, SystemClock.uptimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                // Do work to refresh the list here.
                Map<String, String> beSignForm = new HashMap<String, String>();
                Map<String, String> unBeSignform = new HashMap<String, String>();
                unBeSignform.put("cityid", "77");//TODO 深圳77 debug
                unBeSignform.put("shoptype", ShopType.RESTAURANT.code + "");
                unBeSignform.put("location", "2000");
                unBeSignform.put("pageindex", "1");
                unBeSignform.put("pagesize", "20");
                unBeSignform.put("sortid", "default");
                unBeSignform.put("coordinate", app.locationService().getCoordinates());

                //请求商店列表
                App.mApiService().exec(new MApiRequest(MApiService.URL_SHOP_LIST, beSignForm, unBeSignform, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.v(response.toString());
                        Gson gson = new Gson();
                        ResShopList resShopList = gson.fromJson(response, ResShopList.class);
                        //TODO 更新视图
                        ptrlv.onRefreshComplete();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LogUtil.v(error.toString());
                    }
                }), "smscode");
            }
        });
        return v;
    }
}
