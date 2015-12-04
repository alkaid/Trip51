package com.alkaid.trip51.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.App;
import com.alkaid.trip51.base.widget.BaseFragmentActivity;
import com.alkaid.trip51.model.shop.Shop;

/**
 * Created by alkaid on 2015/11/7.
 */
public class ShopDetailActivity extends BaseFragmentActivity {
    public static final String BUNDLE_KEY_SHOP="BUNDLE_KEY_SHOP";
    TextView tvTitle;
    private Shop currShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        currShop = (Shop) intent.getExtras().get("currShop");
        initTitleBar();
        initView();
    }

    private void initTitleBar(){
        View layTitleBar=findViewById(R.id.title_bar);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        View btnLeft=findViewById(R.id.btn_back_wx);
        View btnRight=findViewById(R.id.notify);
        if(currShop!=null) {
            tvTitle.setText(currShop.getShopname());
        }
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        ShopDetailContainerFragment fragment = new ShopDetailContainerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_KEY_SHOP,currShop);
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fl_menu_container,fragment).commit();
    }

    /**
     * 设置商家名字
     * @param shopName
     */
    public void setShopNameForTitle(String shopName){
        if(tvTitle!=null){
            tvTitle.setText(shopName);
        }
    }
}
