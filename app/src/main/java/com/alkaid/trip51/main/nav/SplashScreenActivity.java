package com.alkaid.trip51.main.nav;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Window;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.ConfigService;
import com.alkaid.trip51.dataservice.LocationService;
import com.alkaid.trip51.dataservice.ServiceListener;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.onlineconfig.OnlineConfigAgent;

/**
 * Created by alkaid on 2015/10/29.
 */
public class SplashScreenActivity extends BaseActivity implements ServiceListener {
    private static final long SHOW_TIME=3000;
    private long begintime;
    private int inited=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);
        // 开启友盟测试模式  上线要删除
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        OnlineConfigAgent.getInstance().updateOnlineConfig(App.instance());
        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        AnalyticsConfig.enableEncrypt(true);
        new Thread(){
            @Override
            public void run() {
                begintime= SystemClock.uptimeMillis();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        App.instance().init(SplashScreenActivity.this);
                        App.locationService().startLocation(SplashScreenActivity.this);
                        App.configService().initConfig(SplashScreenActivity.this);
                    }
                });

            }
        }.start();

    }

    @Override
    public void onComplete(String tag) {
        if(tag.equals(App.INIT_TAG_APP)){
            inited|=1;  //标记App初始化完成
        }
        if(tag.equals(ConfigService.INIT_TAG_GET_CONFIG)){
            inited|=2;  //标记配置初始化完成
        }
        if(tag.equals(LocationService.INIT_TAG_START_LOCATION)){
            inited|=4;  //标记定位初始化完成
        }
        //所有标记位为1时所有初始化完成
        if(inited==(1|2|4)){
            long endTime=SystemClock.uptimeMillis();
            long elapsedTime=endTime-begintime;
            if(elapsedTime<SHOW_TIME){
                try {
                    Thread.sleep(SHOW_TIME-elapsedTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(context, MainActivity.class));
                    overridePendingTransition(R.anim.splash_screen_fade, R.anim.splash_screen_hold);
                    finish();
                }
            });
        }
    }
}
