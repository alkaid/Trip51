package com.alkaid.trip51.main.nav;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.dataservice.ConfigService;
import com.alkaid.trip51.dataservice.InitListener;
import com.alkaid.trip51.dataservice.LocationService;

/**
 * Created by alkaid on 2015/10/29.
 */
public class SplashScreenActivity extends BaseActivity implements InitListener{
    private static final long SHOW_TIME=3000;
    private long begintime;
    private int inited=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);

        new Thread(){
            @Override
            public void run() {
                begintime= SystemClock.uptimeMillis();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        App.instance().init(SplashScreenActivity.this);
                        App.locationService().initLocation(SplashScreenActivity.this);
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
