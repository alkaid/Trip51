package com.alkaid.trip51.base.widget;

import android.os.Bundle;
import android.view.Window;

import com.alkaid.base.view.base.BActivity;
import com.alkaid.base.view.base.BContextWrap;

public class BaseActivity extends BActivity {
	private BaseContextWrap baseContextWrap;
	protected App app;
	@Override
	protected BContextWrap createContextWrap() {
		baseContextWrap=BaseContextWrap.wrap(context);
		return baseContextWrap;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		app=baseContextWrap.app;
	}
}
