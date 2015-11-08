package com.alkaid.trip51.base.widget;

import android.os.Bundle;
import android.view.Window;

import com.alkaid.base.view.base.BContextWrap;
import com.alkaid.base.view.base.BFragmentActivity;

public class BaseFragmentActivity extends BFragmentActivity {
	private BaseContextWrap baseContextWrap;
	protected BaseApp baseApp;
	@Override
	protected BContextWrap createContextWrap() {
		baseContextWrap=BaseContextWrap.wrap(context);
		return baseContextWrap;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		baseApp=baseContextWrap.baseApp;
	}
}
