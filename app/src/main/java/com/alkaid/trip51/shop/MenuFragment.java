package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseTabPageViewFragment;

/**
 * Created by alkaid on 2015/10/31.
 */
public class MenuFragment extends BaseTabPageViewFragment {
    public static final String BUNDLE_KEY_SHOPID="BUNDLE_KEY_SHOPID";
    private long shopid;
    private View slideshowView,layMainMenu,layOrder;

    @Override
    protected ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shopid= App.shopService().getCurrShopid();
        if(shopid<=0){
            throw new RuntimeException("没有设置currShopid,请检查代码！");
        }
        return (ViewGroup) inflater.inflate(R.layout.fragment_main_menu,container,false);
    }

    @Override
    protected void addTabs(TabHost mTabHost, TabsAdapter mTabsAdapter) {
        Bundle data=new Bundle();
        data.putLong(BUNDLE_KEY_SHOPID,shopid);
        mTabsAdapter.addTab(mTabHost.newTabSpec("菜单").setIndicator("菜单"),
                MenuListFragment.class, data);
        mTabsAdapter.addTab(mTabHost.newTabSpec("评价").setIndicator("评价"),
                EvaluationListFragment.class, data);
        mTabsAdapter.addTab(mTabHost.newTabSpec("店铺详情").setIndicator("店铺详情"),
                ShopDetailFragment.class, data);
    }
}
