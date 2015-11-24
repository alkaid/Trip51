package com.alkaid.trip51.shop;


import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alkaid.trip51.R;

public class ShoppingCartActivity extends Activity implements OnClickListener {

	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private LinearLayout layout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);
		btn_take_photo = (Button) this.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) this.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) this.findViewById(R.id.btn_cancel);
		
		layout=(LinearLayout)findViewById(R.id.pop_layout);
		
		layout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
			}
		});
		btn_cancel.setOnClickListener(this);
		btn_pick_photo.setOnClickListener(this);
		btn_take_photo.setOnClickListener(this);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_take_photo:
			break;
		case R.id.btn_pick_photo:				
			break;
		case R.id.btn_cancel:				
			break;
		default:
			break;
		}
		finish();
	}
	
}
