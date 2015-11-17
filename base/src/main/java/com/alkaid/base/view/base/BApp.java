package com.alkaid.base.view.base;

import android.app.Activity;
import android.app.Application;
import android.util.DisplayMetrics;
import android.view.Display;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alkaid
 *
 */

public abstract class BApp extends Application{
	/** 全局数据 类似j2ee的request,为当get某项值后会自动移除该值再也无法获得*/
	protected Map<String, Object> session=new HashMap<String, Object>();
	/** 屏幕相关参数*/
	public DisplayMetrics dm ;
	protected static BApp instance;
	private boolean inited=false;

	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;
	}

	public static BApp instance(){
		return instance;
	}

	/** 初始化应用*/
	public void initInWelcomeActivity(Activity activity){
		if(!inited) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			dm = new DisplayMetrics();
			display.getMetrics(dm);
		}
//		baseApp.setWidthRate(baseApp.getWidth()/320.0f);
//		baseApp.setHeightRate(baseApp.getHeight()/480.0f);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	

	public void putData(String key,Object data){
		session.put(key, data);
	}
	
	public Object getData(String key){
		Object m=session.get(key);
		session.remove(key);
		return m;
	}
}
