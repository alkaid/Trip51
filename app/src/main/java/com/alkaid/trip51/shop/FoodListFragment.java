package com.alkaid.trip51.shop;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.booking.BookingActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResFoodList;
import com.alkaid.trip51.model.shop.FoodCategory;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.shop.adapter.MenuLeftListAdapter;
import com.alkaid.trip51.shop.adapter.MenuRightListAdapter;
import com.alkaid.trip51.widget.linkedlistview.LinkedListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户菜单明细列表
 */

/**
 * Created by jyz on 2015/11/8.
 */
public class FoodListFragment extends BaseFragment implements View.OnClickListener {
    private Shop currShop;
    private Button btnBooking;
    private LinearLayout llShoppingCart;//购物车进入按钮
    private LinkedListView llMenu;
    private MenuLeftListAdapter menuLeftListAdapter;
    private MenuRightListAdapter menuRightListAdapter;

    private List<FoodCategory> foodCategories;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shop_detail_container, container, false);
        initView(v);
        currShop = (Shop) getArguments().getSerializable(ShopDetailActivity.BUNDLE_KEY_SHOP);
        if (currShop == null) {
            throw new RuntimeException("没有设置currShopid,请检查代码！");
        }
        loadData();
        btnBooking= (Button) v.findViewById(R.id.btn_booking);
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
        //设置adapter
        llMenu = (LinkedListView) v.findViewById(R.id.llview_menu);
        menuLeftListAdapter = new MenuLeftListAdapter(getContext());
        menuRightListAdapter = new MenuRightListAdapter(getContext());
        llMenu.setAdapter(menuLeftListAdapter, menuRightListAdapter);


    }

    private void setMenuListData(List<FoodCategory> foodCategories){
        if(menuLeftListAdapter!=null&&menuRightListAdapter!=null&&llMenu!=null&&foodCategories!=null){
            String[] cateforyNames = new String[foodCategories.size()];
            for(int i=0;i<foodCategories.size();i++){
                cateforyNames[i] = foodCategories.get(i).getCategoryname();
            }
            menuLeftListAdapter.setNavigationData(cateforyNames);
            menuRightListAdapter.setMenuListData(foodCategories);
            llMenu.notifyDataSetChanged();
        }
    }

    private void loadData() {
        Map<String, String> beSignForm = new HashMap<String, String>();
        Map<String, String> unBeSignform = new HashMap<String, String>();
        unBeSignform.put("shopid",currShop.getShopid()+"");
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag = "foodlist" + (int) (Math.random() * 1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL,true,ResFoodList.class, MApiService.URL_SHOP_FOODS, beSignForm, unBeSignform, new Response.Listener<ResFoodList>() {
            @Override
            public void onResponse(ResFoodList response) {
                //设置假数据
                foodCategories = response.getFoodcategory();
                setMenuListData(foodCategories);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shoppinp_cart:
                startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
                break;
        }
    }
}