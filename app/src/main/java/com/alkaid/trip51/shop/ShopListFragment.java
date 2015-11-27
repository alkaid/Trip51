package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
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
import com.alkaid.trip51.widget.ShopListViewUtil;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopListFragment extends BaseFragment {
    PullToRefreshListView shopListView;
    ShopListAdapter shopListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.shop_list_fragment,container,false);
        ShopListViewUtil shopListViewUtil=new ShopListViewUtil();
        shopListView= shopListViewUtil.onCreateView(inflater,v,getActivity(),this);
        return v;
    }
}
