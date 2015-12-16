package com.alkaid.trip51.main.nav;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseTabFragmentActivity;
import com.alkaid.trip51.location.CityListActivity;
import com.alkaid.trip51.main.home.MainHomeFragment;
import com.alkaid.trip51.util.UpdateUtil;

public class MainActivity extends BaseTabFragmentActivity{
    private static final String TAB_TAG_HOME = "首页";
    private static final String TAB_TAG_MINE = "我的";
    private static final String TAB_TAG_ORDER = "订单";
    private String gaPageName = "home";
    private String mLastTab;
    private boolean isSearchFragmentExist = false;
    protected boolean activityFinished = false;
    private SharedPreferences mPreferences;
    private static long lastNetworkUnaMills = 0L;

    protected void setOnContentView()
    {
        super.setContentView(R.layout.fragment_tabs_bottom);
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UpdateUtil.update(context);
        if (this.activityFinished) {return;}
            this.mPreferences = getSharedPreferences(getPackageName(), 0);
            if (savedInstanceState != null)
            {
                this.mLastTab = savedInstanceState.getString("mLastTab");
                this.isSearchFragmentExist = savedInstanceState.getBoolean("isSearchFragmentExist");
            }
            super.setTabWidgetBackground(R.drawable.tab_bar_bg);
//            SkinManager.getInstance().refresh(ConfigHelper.appSkinConfig);
            super.addTab(TAB_TAG_HOME, R.layout.tab_indicator_home, MainHomeFragment.class, null);
            super.addTab(TAB_TAG_ORDER, R.layout.tab_indicator_order, MyOrderFragment.class, null);
            super.addTab(TAB_TAG_MINE, R.layout.tab_indicator_my, UserCenterFragment.class, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(mLastFragment.onBackPressed()) {
        }else{
            super.exit();
        }
    }

    public void registerSearchFragment()
    {
        isSearchFragmentExist = true;
    }

    public void unregisterSearchFragment()
    {
        isSearchFragmentExist = false;
    }
}
