package com.alkaid.trip51.main.nav;

import android.content.Intent;
import android.os.Bundle;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;

/**
 * Created by alkaid on 2015/10/29.
 */
public class SplashScreenActivity extends BaseActivity {
    private static final long SHOW_TIME=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.splash_screen_fade, R.anim.splash_screen_hold);
        finish();
    }
}
