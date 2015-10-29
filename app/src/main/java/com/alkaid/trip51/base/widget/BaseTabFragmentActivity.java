package com.alkaid.trip51.base.widget;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.alkaid.trip51.R;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by alkaid on 2015/10/29.
 */
public class BaseTabFragmentActivity extends BaseFragmentActivity{
    protected TabHost mTabHost;
    protected TabManager mTabManager;

    public void addTab(final String label, int tabLayId, Class<?> paramClass, Bundle bundle)
    {
        if (label == null) {
            throw new IllegalArgumentException("title cann't be null!");
        }
        View localView = new LabelIndicatorStrategy(this, label, tabLayId).createIndicatorView(this.mTabHost);
        this.mTabManager.addTab(this.mTabHost.newTabSpec(label).setIndicator(localView), paramClass, bundle);
        localView.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
            {
                if ((paramAnonymousMotionEvent.getAction() == 1) && (!label.equals(BaseTabFragmentActivity.this.mTabHost.getCurrentTabTag())))
                {
                    FragmentTabActivity.this.setGaPageNameByTitle(label);
                    GAHelper.instance().contextStatisticsEvent(FragmentTabActivity.this, "tab", label, 2147483647, "tap");
                    GAHelper.instance().setGAPageName(FragmentTabActivity.this.getGAPageName());
                    GAHelper.instance().setRequestId(FragmentTabActivity.this, UUID.randomUUID().toString(), null, false);
                }
                return false;
            }
        });
    }

    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setOnContentView();
        this.mTabHost = ((TabHost)findViewById(16908306));
        this.mTabHost.setup();
        this.mTabManager = new TabManager(this, this.mTabHost, R.id.realtabcontent);
        setTabWidgetBackground(0);
    }

    protected void onRestoreInstanceState(Bundle paramBundle)
    {
        super.onRestoreInstanceState(paramBundle);
        this.mTabHost.setCurrentTabByTag(paramBundle.getString("tab"));
    }

    protected void onSaveInstanceState(Bundle paramBundle)
    {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putString("tab", this.mTabHost.getCurrentTabTag());
    }

    public void onTabChanged(String paramString) {}

    protected void setGaPageNameByTitle(String paramString) {}

    protected void setOnContentView()
    {
        super.setContentView(R.layout.fragment_tabs);
    }

    protected void setTabWidgetBackground(int paramInt)
    {
        if (paramInt > 0) {
            this.mTabHost.getTabWidget().setBackgroundResource(paramInt);
        }
    }

    public static class TabManager
            implements TabHost.OnTabChangeListener
    {
        private final FragmentTabActivity mActivity;
        private final int mContainerId;
        TabInfo mLastTab;
        private final TabHost mTabHost;
        private final HashMap<String, TabInfo> mTabs = new HashMap();

        public TabManager(FragmentTabActivity paramFragmentTabActivity, TabHost paramTabHost, int paramInt)
        {
            this.mActivity = paramFragmentTabActivity;
            this.mTabHost = paramTabHost;
            this.mContainerId = paramInt;
            this.mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec paramTabSpec, Class<?> paramClass, Bundle paramBundle)
        {
            paramTabSpec.setContent(new DummyTabFactory(this.mActivity));
            String str = paramTabSpec.getTag();
            TabInfo localTabInfo = new TabInfo(str, paramClass, paramBundle);
            localTabInfo.fragment = this.mActivity.getSupportFragmentManager().findFragmentByTag(str);
            if ((localTabInfo.fragment != null) && (!localTabInfo.fragment.isHidden()))
            {
                FragmentTransaction localFragmentTransaction = this.mActivity.getSupportFragmentManager().beginTransaction();
                localFragmentTransaction.hide(localTabInfo.fragment);
                localFragmentTransaction.commitAllowingStateLoss();
            }
            this.mTabs.put(str, localTabInfo);
            this.mTabHost.addTab(paramTabSpec);
        }

        public void onTabChanged(String paramString)
        {
            TabInfo localTabInfo = (TabInfo)this.mTabs.get(paramString);
            FragmentTransaction localFragmentTransaction;
            if (this.mLastTab != localTabInfo)
            {
                localFragmentTransaction = this.mActivity.getSupportFragmentManager().beginTransaction();
                if ((this.mLastTab != null) && (this.mLastTab.fragment != null)) {
                    localFragmentTransaction.hide(this.mLastTab.fragment);
                }
                if (localTabInfo == null) {
                    break label219;
                }
                if (localTabInfo.fragment != null) {
                    break label177;
                }
                localTabInfo.fragment = Fragment.instantiate(this.mActivity, localTabInfo.clss.getName(), localTabInfo.args);
                localFragmentTransaction.add(this.mContainerId, localTabInfo.fragment, localTabInfo.tag);
                Log.i(FragmentTabActivity.LOG_TAG, "onTabChanged with tabId:" + paramString + ", newTab.fragment is null, newTab.tag is " + localTabInfo.tag);
            }
            for (;;)
            {
                this.mLastTab = localTabInfo;
                localFragmentTransaction.commitAllowingStateLoss();
                this.mActivity.getSupportFragmentManager().executePendingTransactions();
                this.mActivity.onTabChanged(paramString);
                return;
                label177:
                localFragmentTransaction.show(localTabInfo.fragment);
                Log.i(FragmentTabActivity.LOG_TAG, "onTabChanged with tabId:" + paramString + ", show fragment success");
                continue;
                label219:
                Log.i(FragmentTabActivity.LOG_TAG, "onTabChanged with tabId:" + paramString + ", newTab is null");
            }
        }

        static class DummyTabFactory
                implements TabHost.TabContentFactory
        {
            private final Context mContext;

            public DummyTabFactory(Context paramContext)
            {
                this.mContext = paramContext;
            }

            public View createTabContent(String paramString)
            {
                View localView = new View(this.mContext);
                localView.setMinimumWidth(0);
                localView.setMinimumHeight(0);
                return localView;
            }
        }

        static final class TabInfo
        {
            final Bundle args;
            final Class<?> clss;
            Fragment fragment;
            final String tag;

            TabInfo(String paramString, Class<?> paramClass, Bundle paramBundle)
            {
                this.tag = paramString;
                this.clss = paramClass;
                this.args = paramBundle;
            }
        }
    }

    static class LabelIndicatorStrategy
    {
        private Context mContext;
        private int mIndicatorView;
        private final CharSequence mLabel;

        public LabelIndicatorStrategy(Context context, CharSequence label)
        {
            this.mContext = context;
            this.mLabel = label;
        }

        public LabelIndicatorStrategy(Context context, CharSequence label, int layId)
        {
            this(context, label);
            this.mIndicatorView = layId;
        }

        public View createIndicatorView(TabHost tabHost)
        {
//            if (this.mIndicatorView == 0) {
//                this.mIndicatorView = R.layout.tab_indicator_holo;
//            }
            View localView = ((LayoutInflater)this.mContext.getSystemService("layout_inflater")).inflate(this.mIndicatorView, tabHost.getTabWidget(), false);
            ((TextView)localView.findViewById(android.R.id.text1)).setText(this.mLabel);
            return localView;
        }
    }
}
