package com.alkaid.trip51.shop;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.shop.adapter.ShoppingCartListAdapter;

public class ShoppingCartActivity extends Activity implements OnClickListener {

	private LinearLayout layout;
	private ListView lvShoppingCartList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);
		initView();
	}

	private void initView(){
		lvShoppingCartList = (ListView) findViewById(R.id.lv_shopping_cart_list);
		lvShoppingCartList.setAdapter(new ShoppingCartListAdapter(this));

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
		finish();
	}
	
}
