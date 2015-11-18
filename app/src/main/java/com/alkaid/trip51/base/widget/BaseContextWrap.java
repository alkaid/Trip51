package com.alkaid.trip51.base.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.alkaid.base.view.base.BContextWrap;

public class BaseContextWrap extends BContextWrap {
	protected App app;
	
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
		app=App.instance();
	}
	
}
