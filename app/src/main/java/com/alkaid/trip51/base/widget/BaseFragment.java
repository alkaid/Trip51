package com.alkaid.trip51.base.widget;

import android.os.Bundle;

import com.alkaid.base.view.base.BContextWrap;
import com.alkaid.base.view.base.BFragment;
import com.android.volley.VolleyError;

public class BaseFragment extends BFragment {
	private BaseContextWrap baseContextWrap;
	protected App app;
	private boolean isFirstShow=true;
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

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser&&isFirstShow){
			isFirstShow=false;
			onLazyLoad();
		}else{

		}
	}

	protected void onLazyLoad(){

	}
}
