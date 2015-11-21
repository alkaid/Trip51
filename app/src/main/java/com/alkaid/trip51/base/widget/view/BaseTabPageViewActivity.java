package com.alkaid.trip51.base.widget.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragmentActivity;

import java.util.ArrayList;

/**
 * Created by alkaid on 2015/11/8.
 */
public abstract class BaseTabPageViewActivity extends BaseFragmentActivity {
    private TabHost mTabHost;
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;

//    protected abstract ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract void addTabs(TabHost tabHost,TabsAdapter tabsAdapter);
    protected abstract int getContentLayoutId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        LayoutInflater inflater=LayoutInflater.from(context);
        setContentView(getContentLayoutId());
//        setContentView(v);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        addTabs(mTabHost,mTabsAdapter);
//        mTabsAdapter.addTab(mTabHost.newTabSpec("楼盘").setIndicator("楼盘"),
//                HouseListActivity.HouseListFragment.class, null,R.drawable.sel_tab_house);
//        mTabsAdapter.addTab(mTabHost.newTabSpec("资讯").setIndicator("资讯"),
//                HouseListActivity.HouseListFragment.class, null,R.drawable.sel_tab_info);
//        mTabsAdapter.addTab(mTabHost.newTabSpec("最新优惠").setIndicator("最新优惠"),
//                HouseListActivity.HouseListFragment.class, null,R.drawable.sel_tab_coupon);
//        mTabsAdapter.addTab(mTabHost.newTabSpec("活动").setIndicator("活动"),
//                HouseListActivity.HouseListFragment.class, null,R.drawable.sel_tab_activity);
//        mTabsAdapter.addTab(mTabHost.newTabSpec("更多").setIndicator("更多"),
//                HouseListActivity.HouseListFragment.class, null,R.drawable.sel_tab_more);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        mTabHost.getTabWidget().setBackgroundColor(0xffffffff);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    public static class TabsAdapter extends FragmentPagerAdapter implements
            TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
        private LayoutInflater inflater;

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity context, TabHost tabHost,ViewPager pager) {
            super(context.getSupportFragmentManager());
            mContext = context;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
            inflater=LayoutInflater.from(mContext);
        }
        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args){
            addTab(tabSpec,clss,args,-1);
        }
        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args,int layouId) {
            View v=inflater.inflate(layouId>0?layouId:R.layout.tab_indicator_holo, null,false);
            TextView tv = (TextView) v.findViewById(android.R.id.title);
            tv.setText(tabSpec.getTag());
            tabSpec.setIndicator(v);
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
//            tabSpec.setIndicator(text);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

//        private View createTabView(int iconResId) {
//            RelativeLayout tabIndicator1 = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);
//            ImageView button = (ImageView) tabIndicator1.getChildAt(0);
//            button.setImageResource(iconResId);
//            button.setTag(iconResId);
//            return tabIndicator1;
//        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(),
                    info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
