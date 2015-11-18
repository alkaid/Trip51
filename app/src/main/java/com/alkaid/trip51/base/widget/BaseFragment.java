package com.alkaid.trip51.base.widget;

import android.os.Bundle;

import com.alkaid.base.view.base.BContextWrap;
import com.alkaid.base.view.base.BFragment;

public class BaseFragment extends BFragment {
	private BaseContextWrap baseContextWrap;
	protected App app;
	@Override
	protected BContextWrap createContextWrap() {
		baseContextWrap=BaseContextWrap.wrap(context);
		return baseContextWrap;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app=baseContextWrap.app;
	}
}
