package com.alkaid.trip51.base.widget;

import android.os.Bundle;

import com.alkaid.base.view.base.BActivity;
import com.alkaid.base.view.base.BContextWrap;

public class BaseActivity extends BActivity {
	private BaseContextWrap baseContextWrap;
	protected BaseApp baseApp;
	@Override
	protected BContextWrap createContextWrap() {
		baseContextWrap=BaseContextWrap.wrap(context);
		return baseContextWrap;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseApp=baseContextWrap.baseApp;
	}
}
