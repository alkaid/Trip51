package com.alkaid.base.extern.baseView;

import android.os.Bundle;

import com.alkaid.base.view.base.BContextWrap;
import com.alkaid.base.view.base.BFragment;

public class BaseFragment extends BFragment {
	private BaseContextWrap baseContextWrap;
	protected BaseApp baseApp;
	@Override
	protected BContextWrap createContextWrap() {
		baseContextWrap=BaseContextWrap.wrap(context);
		return baseContextWrap;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseApp=baseContextWrap.baseApp;
	}
}
