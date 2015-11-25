package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.dataservice.mapi.CacheType;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.booking.BookingActivity;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResFoodList;
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
public class MenuListFragment extends BaseFragment implements View.OnClickListener {
    private long shopid;
    private Button btnBooking;
    private LinearLayout llShoppingCart;//购物车进入按钮

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.menu_list_fragment, container, false);
        initView(v);
        shopid = getArguments().getLong(MenuFragment.BUNDLE_KEY_SHOPID);
        if (shopid <= 0) {
            throw new RuntimeException("没有设置currShopid,请检查代码！");
        }
        loadData();
        btnBooking= (Button) v.findViewById(R.id.btn_pay);
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, BookingActivity.class));
            }
        });
        return v;
    }

    private void initView(View v) {
       llShoppingCart = (LinearLayout) v.findViewById(R.id.ll_shoppinp_cart);
        llShoppingCart.setOnClickListener(this);
    }


    private void loadData() {
        Map<String, String> beSignForm = new HashMap<String, String>();
        Map<String, String> unBeSignform = new HashMap<String, String>();
        unBeSignform.put("shopid", shopid + "");
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag = "foodlist" + (int) (Math.random() * 1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL, MApiService.URL_SHOP_FOODS, beSignForm, unBeSignform, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                ResFoodList resdata = gson.fromJson(response, ResFoodList.class);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shoppinp_cart:
                startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                break;
        }
    }
}
