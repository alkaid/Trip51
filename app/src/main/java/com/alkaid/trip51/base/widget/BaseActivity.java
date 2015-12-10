package com.alkaid.trip51.base.widget;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;

import com.alkaid.base.view.base.BActivity;
import com.alkaid.base.view.base.BContextWrap;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

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

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(this.getClass().getSimpleName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);          //统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(this.getClass().getSimpleName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
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
	protected ProgressDialog getProgressDialog(){
		return baseContextWrap.getProgressDialog();
	}

	protected void toastShort(String msg){
		baseContextWrap.toastShort(msg);
	}
	protected void toastLong(String msg){
		baseContextWrap.toastLong(msg);
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
