package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseTabPageViewFragment;

/**
 * Created by alkaid on 2015/10/31.
 */
public class MyClientFragment extends BaseTabPageViewFragment {
    private View slideshowView,layMainMenu,layOrder;

    @Override
    protected ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.fragment_main_my_coupon,container,false);
    }

    @Override
    protected void addTabs(TabHost mTabHost, TabsAdapter mTabsAdapter) {
        mTabsAdapter.addTab(mTabHost.newTabSpec("客服热线").setIndicator("客服热线"),
                MyClientQuestionListFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec("意见反馈").setIndicator("意见反馈"),
                MyClientFeedbackFragment.class, null);
    }
}
