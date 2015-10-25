package com.alkaid.base.extern.baseView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.alkaid.base.view.base.BContextWrap;

public class BaseContextWrap extends BContextWrap {
	protected BaseApp baseApp;
	
	protected BaseContextWrap(Context context){
		super((Activity) context);
	}
	
	public static BaseContextWrap wrap(Context context){
		//限制context参数必须为Activity类型
		if(context instanceof Activity){
			return new BaseContextWrap((Activity) context);
		}else{
			throw new IllegalArgumentException("The argument of context must be Activity!");
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//TODO 这里needInitApp参数为false意为由子类自己手动控制BaseApp的初始化时机 。一般初始化都放在欢迎界面的异步线程里。
		baseApp=BaseApp.getBaseApp(context,false);
	}
	
}
