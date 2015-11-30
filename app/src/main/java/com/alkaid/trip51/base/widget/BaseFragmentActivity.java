package com.alkaid.trip51.base.widget;

import android.os.Bundle;
import android.view.Window;

import com.alkaid.base.view.base.BContextWrap;
import com.alkaid.base.view.base.BFragmentActivity;
import com.alkaid.trip51.model.response.ResponseData;
import com.android.volley.VolleyError;

public class BaseFragmentActivity extends BFragmentActivity {
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

	protected void setDefaultPdgCanceListener(final String tag){
		baseContextWrap.setDefaultPdgCanceListener(tag);
	}
	protected void dismissPdg(){
		baseContextWrap.dismissPdg();
	}
	protected void showPdg(){
		baseContextWrap.showPdg();
	}
	protected void showPdg(String msg){
		baseContextWrap.showPdg(msg);
	}
	protected void toastShort(String msg){
		baseContextWrap.toastShort(msg);
	}
	protected void toastShortAsync(String msg){
		baseContextWrap.toastShortAsync(msg);
	}
	protected boolean checkIsNeedRelogin(VolleyError volleyError){
		return baseContextWrap.checkIsNeedReloginInActivity(volleyError, this);
	}
}
