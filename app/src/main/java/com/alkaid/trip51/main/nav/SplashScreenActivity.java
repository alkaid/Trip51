package com.alkaid.trip51.main.nav;

import android.content.Intent;
import android.os.Bundle;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;

/**
 * Created by alkaid on 2015/10/29.
 */
public class SplashScreenActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
