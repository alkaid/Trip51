package com.alkaid.trip51.main.nav;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alkaid.base.common.DateUtil;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseTabFragmentActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseTabFragmentActivity{
    protected boolean activityFinished = false;
    private SharedPreferences mPreferences;
    private static long lastNetworkUnaMills = 0L;

    protected void setOnContentView()
    {
        super.setContentView(R.layout.fragment_tabs_bottom);
    }

    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        if (this.activityFinished) {return;}
            this.mPreferences = getSharedPreferences(getPackageName(), 0);
            NetworkInfo localNetworkInfo;
            boolean bool;
            if (System.currentTimeMillis() - lastNetworkUnaMills > 120000L)
            {
                localNetworkInfo = ((ConnectivityManager)super.getSystemService("connectivity")).getActiveNetworkInfo();
                if (localNetworkInfo != null) {
                    break label863;
                }
                bool = false;
                label207:
                if (!bool) {
                    Toast.makeText(this, "目前网络连接不可用。", 0).show();
                }
            }
            if (paramBundle != null)
            {
                this.mLastTab = paramBundle.getString("mLastTab");
                this.isSearchFragmentExist = paramBundle.getBoolean("isSearchFragmentExist");
            }
            super.setTabWidgetBackground(R.drawable.tab_bar_bg);
            SkinManager.getInstance().refresh(ConfigHelper.appSkinConfig);
            super.addTab("首页", R.layout.tab_indicator_home, MainHomeFragment.class, null);
            super.addTab("闪惠团购", R.layout.tab_indicator_tuan, TuanHomeFragment.class, null);
            super.addTab("发现", R.layout.tab_indicator_search, MainFindFragment.class, null);
            super.addTab("我的", R.layout.tab_indicator_my, UserFragment.class, null);
            this.GAFlag = true;
            if (super.getIntent().getBooleanExtra("fromWX", false))
            {
                ((NovaApplication)super.getApplication()).setStartType(1);
                Bundle localBundle = super.getIntent().getExtras();
                ((NovaApplication)super.getApplication()).setWXBundle(localBundle);
                label368:
                Uri localUri = super.getIntent().getData();
                if (localUri != null)
                {
                    this.host = localUri.getHost();
                    this.gaPageName = this.host;
                }
                super.cityConfig().addListener(this);
                if (!super.cityConfig().currentCity().isTuan()) {
                    this.mTabHost.getTabWidget().getChildTabViewAt(1).setVisibility(8);
                }
                if (!UpdateManager.instance(this).checkNewVersion()) {
                    break label920;
                }
                this.hasNewUpdate = true;
                long l = NovaFragmentTabActivity.preferences().getLong("nextUpdateNotifyTime", 0L);
                Log.d("MainActivity", "notifyTime=" + l + " currentTime=" + DateUtil.currentTimeMillis());
                if (l >= DateUtil.currentTimeMillis()) {
                    break label887;
                }
                NovaFragmentTabActivity.preferences().edit().putLong("nextUpdateNotifyTime", DateUtil.getNextDayTimeMillis()).commit();
                UpdateUIManager.showDialog(this);
                label539:
                if ((NetworkUtils.isWIFIConnection(this)) && (NovaFragmentTabActivity.preferences().getBoolean("autodownload", true))) {
                    UpdateManager.instance(this).startSilentDownload();
                }
                updateVersionCode();
                super.configService().addListener("versionCode", this);
                IntentFilter localIntentFilter = new IntentFilter();
                localIntentFilter.addAction("com.dianping.action.UPLOAD_PHOTO_STATUS_CHANGED");
                localIntentFilter.addAction("com.dianping.action.SPLASH_PIC_UPDATE");
                localIntentFilter.addAction("com.dianping.action.RedAlerts");
                localIntentFilter.addAction("com.dianping.action.SHOW_TUAN_TAB_RED_MARK");
                localIntentFilter.addAction("com.dianping.action.ShowPushDialog");
                localIntentFilter.addAction("com.dianping.action.NEW_MESSAGE");
                registerReceiver(this.receiver, localIntentFilter);
                super.accountService().addListener(this);
                checkNewVersionMark();
                CssStyleManager.instance(this).syncServerCssFile();
            }
            try
            {
                String str2 = NovaFragmentTabActivity.preferences().getString("lastMacDate", null);
                String str3 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
                if (!str3.equals(str2))
                {
                    super.statisticsEvent("index5", "index5_mac", ((WifiManager)super.getSystemService("wifi")).getConnectionInfo().getMacAddress(), 0);
                    NovaFragmentTabActivity.preferences().edit().putString("lastMacDate", str3).commit();
                }
                label774:
                if (!NovaFragmentTabActivity.preferences().getBoolean("shortcutinstalled", false))
                {
                    NovaFragmentTabActivity.preferences().edit().putBoolean("shortcutinstalled", true).commit();
                    if (!ShortcutUtils.hasShortcut(this))
                    {
                        if ((Build.VERSION.SDK_INT >= 16) && (Build.VERSION.SDK_INT <= 18)) {
                            ShortcutUtils.removeShortcut(this, R.string.app_name);
                        }
                        ShortcutUtils.createShortcut(this, R.drawable.icon, R.string.app_name);
                    }
                }
                SkinManager.setTabHostSkin(this, this.mTabHost);
                continue;
                label863:
                bool = localNetworkInfo.isConnectedOrConnecting();
                break label207;
                ((NovaApplication)super.getApplication()).setStartType(0);
                break label368;
                label887:
                if ((!ConfigHelper.forceUpdate) || ((NetworkUtils.getNetworkType(this) != "WIFI") && (NetworkUtils.getNetworkType(this) != "4G"))) {
                    break label539;
                }
                UpdateUIManager.showDialog(this);
                break label539;
                label920:
                this.hasNewUpdate = false;
            }
            catch (Exception localException)
            {
                break label774;
            }
        }
    }
}
