package com.alkaid.trip51.usercenter;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;
import com.alkaid.trip51.base.widget.BaseFragmentActivity;

/**
 * Created by alkaid on 2015/11/9.
 */
public class MyCouponActivity extends BaseFragmentActivity {

    private ListView myFavoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        initTitleBar();
        initView();
    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        tvTitle.setText("优惠券");
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        MyCouponFragment fragment = new MyCouponFragment();
        fragmentTransaction.add(R.id.fl_coupon_container,fragment).commit();
    }

}
