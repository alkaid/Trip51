package com.alkaid.base.view.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.alkaid.base.exception.TradException;

public abstract class BActivity extends Activity {
	protected Context context;
	private BContextWrap bContextWrap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=this;
		bContextWrap = createContextWrap();
		bContextWrap.onCreate(savedInstanceState);
	}
	/**
	 * 在 {@link #onCreate(Bundle)}中会调用此方法生成一个  {@link BContextWrap}实例
	 * <br/>若不想使用默认的 {@link BContextWrap}实例 请重载此方法
	 * @return
	 */
	protected BContextWrap createContextWrap(){
		return BContextWrap.wrap(context);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		bContextWrap.onResume();
	}
	
	@Override
	protected void onPause() {
		bContextWrap.onPause();
		super.onPause();
	}
	protected void pauseContextWrap(){
	}
	/**
	 * 处理异常 
	 * @param e
	 */
	public void handleException(TradException e){
		bContextWrap.handleException(e);
	}
	/**
	 * 退出应用
	 */
    protected void exit() {
    	bContextWrap.exitOnDoubleBack();
    };
	
}



