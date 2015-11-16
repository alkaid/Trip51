package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseTabPageViewFragment;
import com.alkaid.trip51.shop.OrderListFragment;

/**
 * Created by alkaid on 2015/10/31.
 */
public class MyCouponFragment extends BaseTabPageViewFragment {
    private View slideshowView,layMainMenu,layOrder;

    @Override
    protected ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_main_my_coupon,container,false);
    }

    @Override
    protected void addTabs(TabHost mTabHost, TabsAdapter mTabsAdapter) {
        mTabsAdapter.addTab(mTabHost.newTabSpec("可用优惠券").setIndicator("可用优惠券"),
                MyCouponListFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("过期优惠券").setIndicator("过期优惠券"),
                MyCouponListFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("历史优惠券").setIndicator("历史优惠券"),
                MyCouponListFragment.class, null);
    }
}
