package com.alkaid.trip51.base.widget;

import android.os.Bundle;
import android.view.Window;

import com.alkaid.base.view.base.BContextWrap;
import com.alkaid.base.view.base.BFragmentActivity;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

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
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(context);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(context);
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
		return app.accountService().checkIsNeedRelogin(volleyError, this);
	}
	protected boolean checkLogined(){
		return app.accountService().checkLogined(this);
	}
}
