package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseTabPageViewFragment;
import com.alkaid.trip51.usercenter.MyCouponListFragment;

/**
 * Created by alkaid on 2015/10/31.
 */
public class MenuFragment extends BaseTabPageViewFragment {
    private View slideshowView,layMainMenu,layOrder;

    @Override
    protected ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_main_menu,container,false);
    }

    @Override
    protected void addTabs(TabHost mTabHost, TabsAdapter mTabsAdapter) {
        mTabsAdapter.addTab(mTabHost.newTabSpec("菜单").setIndicator("菜单"),
                MenuListFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("评价").setIndicator("评价"),
                EvaluationListFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("店铺详情").setIndicator("店铺详情"),
                ShopDetailFragment.class, null);
    }
}
