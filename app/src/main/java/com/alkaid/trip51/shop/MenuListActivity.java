package com.alkaid.trip51.shop;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragmentActivity;
import com.alkaid.trip51.base.widget.view.NovaImageButton;

/**
 * Created by alkaid on 2015/11/7.
 */
public class MenuListActivity extends BaseFragmentActivity {
    View ivBack;
    TextView tvTitle;
    NovaImageButton btnFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_list_activity);
        FragmentManager fragmentManager =getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        MenuListFragment mf=new MenuListFragment();
        transaction.replace(R.id.content,mf).commit();

        ivBack=findViewById(R.id.btn_back_wx);
        tvTitle =  ((TextView)findViewById(R.id.tvTitle));
                                                    setShopNameForTitle("餐厅名称");
        btnFavorite = (NovaImageButton)findViewById(R.id.notify);
        btnFavorite.setImageResource(R.drawable.btn_favorite);
        //增加收藏的餐厅
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
