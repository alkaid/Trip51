package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragmentActivity;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopDescActivity extends BaseFragmentActivity {
    View ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_list_activity);
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ShopDescFragment sf=new ShopDescFragment();
        sf.setArguments(getIntent().getExtras());
        transaction.replace(R.id.content,sf).commit();

    }
    private void initTitleBar(){
        ivBack=findViewById(R.id.btn_back_wx);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("店铺详情");
        ImageButton btnRight = (ImageButton) findViewById(R.id.notify);
        btnRight.setVisibility(View.GONE);
//        btnRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), SettingActivity.class));
//            }
//        });
    }
}
