package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alkaid.base.exception.TradException;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.booking.BookingActivity;
import com.alkaid.trip51.dataservice.mapi.CacheType;
import com.alkaid.trip51.dataservice.mapi.MApiRequest;
import com.alkaid.trip51.dataservice.mapi.MApiService;
import com.alkaid.trip51.model.response.ResFoodList;
import com.alkaid.trip51.model.shop.Food;
import com.alkaid.trip51.model.shop.FoodCategory;
import com.alkaid.trip51.model.shop.Shop;
import com.alkaid.trip51.shop.adapter.MenuLeftListAdapter;
import com.alkaid.trip51.shop.adapter.MenuRightListAdapter;
import com.alkaid.trip51.shop.adapter.ShoppingCartListAdapter;
import com.alkaid.trip51.widget.linkedlistview.LinkedListView;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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
    private LinearLayout llCart;//购物车进入按钮
    private LinkedListView llMenu;
    private MenuLeftListAdapter menuLeftListAdapter;
    private MenuRightListAdapter menuRightListAdapter;

    private ShoppingCartListAdapter cartListAdapter;

    private RelativeLayout llCartDetail;
    private LinearLayout llCloseShoppingCart;
    private ListView cartList;

    private TextView tvTotalPrice;
    private TextView tvShoppingCartFoodNum;
    private TextView tvClearCartAll;

    private Button btnBooking;

    private List<FoodCategory> foodCategories;//菜单列表数据

    public static final int UPDATE_MENU_LIST = 1001;
    public static final int UPDATE_SHOPPING_CART = 1002;
    public static final int OPEN_SHOPPING_CART_BUTOON = 1003;
    public static final int CLOSE_SHOPPING_CART_BUTTON = 1004;
    public static final int CLEAR_SHOPPING_CART_ALL = 1005;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currShop = (Shop) getArguments().getSerializable(ShopDetailActivity.BUNDLE_KEY_SHOP);
        View v = inflater.inflate(R.layout.fragment_shop_detail_container, container, false);
        v.setOnClickListener(this);
        initView(v);
        if (currShop == null) {
            throw new RuntimeException("没有设置currShopid,请检查代码！");
        }
        loadData();
        return v;
    }

    private void initView(View v) {
        llCart = (LinearLayout) v.findViewById(R.id.ll_shoppinp_cart);
        llCart.setOnClickListener(this);
        llCartDetail = (RelativeLayout) v.findViewById(R.id.rl_shopping_cart);
        llCloseShoppingCart = (LinearLayout) v.findViewById(R.id.ll_close_shopping_cart);
        tvClearCartAll = (TextView) v.findViewById(R.id.tv_clear_all);
        tvClearCartAll.setOnClickListener(this);
        cartList = (ListView) v.findViewById(R.id.lv_shopping_cart_list);
        tvTotalPrice = (TextView) v.findViewById(R.id.tv_total_price);
        tvShoppingCartFoodNum = (TextView) v.findViewById(R.id.tv_shopping_cart_food_num);
        btnBooking = (Button) v.findViewById(R.id.btn_booking);
        btnBooking.setOnClickListener(this);
        cartListAdapter = new ShoppingCartListAdapter(getContext(), currShop, mHandler);
        cartList.setAdapter(cartListAdapter);
        llCartDetail.setOnClickListener(this);
        // llCartDetail.setOnClickListener(this);
        //设置adapter
        llMenu = (LinkedListView) v.findViewById(R.id.llview_menu);
        menuLeftListAdapter = new MenuLeftListAdapter(getContext());
        menuRightListAdapter = new MenuRightListAdapter(getContext(), currShop);
        llMenu.setAdapter(menuLeftListAdapter, menuRightListAdapter);

    }

    /**
     * 设置数据的地方，listview也进行update
     *
     * @param foodCategories
     */
    private void setListData(List<FoodCategory> foodCategories) {
        if (menuLeftListAdapter != null && menuRightListAdapter != null && llMenu != null && foodCategories != null) {
            String[] cateforyNames = new String[foodCategories.size()];
            for (int i = 0; i < foodCategories.size(); i++) {
                cateforyNames[i] = foodCategories.get(i).getCategoryname();
            }
            menuLeftListAdapter.setNavigationData(cateforyNames);
            menuRightListAdapter.setMenuListData(foodCategories);
            mHandler.sendEmptyMessage(UPDATE_MENU_LIST);
        }
        if (cartListAdapter != null) {
            cartListAdapter.setCartData(foodCategories);
            //mHandler.sendEmptyMessage(UPDATE_SHOPPING_CART);
        }
    }

    private void loadData() {
        Map<String, String> beSignForm = new HashMap<String, String>();
        Map<String, String> unBeSignform = new HashMap<String, String>();
        unBeSignform.put("shopid", currShop.getShopid() + "");
//        unBeSignform.put("pageindex", "1");
//        unBeSignform.put("pagesize", "20");
        final String tag = "foodlist" + (int) (Math.random() * 1000);
        setDefaultPdgCanceListener(tag);
        showPdg();
        App.mApiService().exec(new MApiRequest(CacheType.NORMAL, true, ResFoodList.class, MApiService.URL_SHOP_FOODS, beSignForm, unBeSignform, new Response.Listener<ResFoodList>() {
            @Override
            public void onResponse(ResFoodList response) {
                //设置假数据
                foodCategories = response.getFoodcategory();
                setListData(foodCategories);
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
            case R.id.btn_booking:
                Intent mIntent = new Intent(context, BookingActivity.class);
                if(currShop!=null) {
                    mIntent.putExtra(ShopDetailActivity.BUNDLE_KEY_SHOP, currShop);
                }
                startActivity(mIntent);
                break;
            case R.id.tv_clear_all:
                mHandler.sendEmptyMessage(CLEAR_SHOPPING_CART_ALL);
                break;
            case R.id.ll_shoppinp_cart://点击购物车按钮操作
                if (llCartDetail.getVisibility() == View.GONE) {
                    mHandler.sendEmptyMessage(OPEN_SHOPPING_CART_BUTOON);
                } else if (llCartDetail.getVisibility() == View.VISIBLE) {
                    mHandler.sendEmptyMessage(CLOSE_SHOPPING_CART_BUTTON);
                }
                break;
            case R.id.rl_shopping_cart://点击购物车布局要关闭
                if (llCartDetail.getVisibility() == View.VISIBLE) {
                    mHandler.sendEmptyMessage(CLOSE_SHOPPING_CART_BUTTON);
                }
                break;
            default:
                break;
        }
    }

    public List<FoodCategory> getFoodCategories() {
        return foodCategories;
    }

    /**
     * 重置食物的数量
     */
    private void resetFoodNum() {
        if (foodCategories != null) {
            for (FoodCategory foodCategory : foodCategories) {
                for (Food f : foodCategory.getFoods()) {
                    f.setFoodNum(0);
                }
            }
        }

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_MENU_LIST:
                    if (menuLeftListAdapter != null && menuRightListAdapter != null && llMenu != null) {
                        llMenu.notifyDataSetChanged();
                    }
                    break;
                case UPDATE_SHOPPING_CART:
                    if (cartListAdapter != null) {
                        cartListAdapter.notifyDataSetChanged();
                    }
                    break;
                case OPEN_SHOPPING_CART_BUTOON:
                    llCartDetail.setVisibility(View.VISIBLE);
                    cartListAdapter.setCartData(foodCategories);
                    cartListAdapter.notifyDataSetChanged();
                    if (tvTotalPrice != null) {
                        tvTotalPrice.setVisibility(View.VISIBLE);
                        if (currShop != null) {
                            tvTotalPrice.setText("共￥" + App.shoppingCartService().getCartTotalPrice(currShop.getShopid()) + "");
                            tvShoppingCartFoodNum.setText(App.shoppingCartService().getCartFoodNum(currShop.getShopid()) + "");
                        }
                        llCloseShoppingCart.setVisibility(View.GONE);
                    }
                    break;
                case CLOSE_SHOPPING_CART_BUTTON:
                    llCartDetail.setVisibility(View.GONE);
                    llCloseShoppingCart.setVisibility(View.VISIBLE);
                    tvTotalPrice.setVisibility(View.GONE);
                    break;
                case CLEAR_SHOPPING_CART_ALL:
                    App.shoppingCartService().clearCartFood(currShop.getShopid());
                    if (cartListAdapter != null) {
                        cartListAdapter.clearCartAll();
                    }
                    resetFoodNum();
                    sendEmptyMessage(UPDATE_MENU_LIST);
                    sendEmptyMessage(UPDATE_SHOPPING_CART);
                default:
                    break;
            }
        }
    };

}
