package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseTabPageViewFragment;
import com.alkaid.trip51.model.shop.Shop;

/**
 * Created by alkaid on 2015/10/31.
 */
public class ShopDetailContainerFragment extends BaseTabPageViewFragment {
    private Shop currShop;
    private View slideshowView,layMainMenu,layOrder;

    @Override
    protected ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currShop = (Shop) getArguments().getSerializable(ShopDetailActivity.BUNDLE_KEY_SHOP);
        if(currShop ==null){
            throw new RuntimeException("没有设置currShopid,请检查代码！");
        }
        return (ViewGroup) inflater.inflate(R.layout.fragment_main_menu,container,false);
    }

    @Override
    protected void addTabs(TabHost mTabHost, TabsAdapter mTabsAdapter) {
        Bundle data=new Bundle();
        data.putSerializable(ShopDetailActivity.BUNDLE_KEY_SHOP, currShop);
        mTabsAdapter.addTab(mTabHost.newTabSpec("菜单").setIndicator("菜单"),
                FoodListFragment.class, data);
        mTabsAdapter.addTab(mTabHost.newTabSpec("评价").setIndicator("评价"),
                EvaluationListFragment.class, data);
        mTabsAdapter.addTab(mTabHost.newTabSpec("店铺详情").setIndicator("店铺详情"),
                ShopDescFragment.class, data);
    }
}
