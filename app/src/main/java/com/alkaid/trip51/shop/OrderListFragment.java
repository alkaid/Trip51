package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.model.response.ResOrderList;
import com.alkaid.trip51.model.response.ResPayStatus;
import com.alkaid.trip51.shop.adapter.OrderListAdapter;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/8.
 */
public class OrderListFragment extends BaseFragment {
    public static final String BUNDLE_KEY_CONDITION_ORDER_STATUS="BUNDLE_KEY_CONDITION_ORDER_STATUS";
    PullToRefreshListView ptrlv;
    private int conditionOrderStatus;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.default_pull_list_view, container, false);
        ptrlv = (PullToRefreshListView) v.findViewById(R.id.ptrList);
        ptrlv.setAdapter(new OrderListAdapter(getActivity()));
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context,OrderDetailActivity.class));
            }
        });
        conditionOrderStatus=getArguments().getInt(BUNDLE_KEY_CONDITION_ORDER_STATUS);
        loadData(conditionOrderStatus);
        return v;
    }

    private void loadData(int orderStatus){
        Map<String,String> beSignForm=new HashMap<String, String>();
        Map<String,String> unBeSignform=new HashMap<String, String>();
        beSignForm.put("openid", App.accountService().getOpenInfo().getOpenid());
        unBeSignform.put("orderstatus", orderStatus + "");
        final String tag="orderList"+(int)(Math.random()*1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.DISABLED,true, ResOrderList.class, MApiService.URL_ORDER_LIST, beSignForm, unBeSignform, new Response.Listener<ResOrderList>() {
            @Override
            public void onResponse(ResOrderList response) {
                dismissPdg();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissPdg();
                //TODO 暂时用handleException 应该换成失败时的正式UI
                handleException(new TradException(error));
                checkIsNeedRelogin(error);
            }
        }), tag);
    }
}