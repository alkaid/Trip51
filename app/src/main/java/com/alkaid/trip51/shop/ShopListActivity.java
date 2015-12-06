package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragmentActivity;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopListActivity extends BaseFragmentActivity {
    View ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_list_activity);
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ShopListFragment sf=new ShopListFragment();
        sf.setArguments(getIntent().getExtras());
        transaction.replace(R.id.content,sf).commit();

        ivBack=findViewById(R.id.btn_back_wx);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
