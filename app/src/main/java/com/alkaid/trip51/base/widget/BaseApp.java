package com.alkaid.trip51.base.widget;

import android.app.Activity;
import android.content.Context;

import com.alkaid.base.view.base.BApp;

/**
 * @author Alkaid
 *
 */

public class BaseApp extends BApp{
	/**
	 * 获得全局单例  若没有初始化过BaseApp则默认会初始化
	 * @param context
	 * @return
	 */
	public static BaseApp getBaseApp(Context context){
		return getBaseApp(context,true);
	}
	/**
	 * 获得全局单例  根据needInitApp判断在没有初始化过BaseApp的情况下是否要初始化
	 * @param context
	 * @param needInitApp 是否需要初始化BaseApp
	 * @return
	 */
	public static BaseApp getBaseApp(Context context,boolean needInitApp){
		//先设置监听
		if(mAppLifeListener==null)
			setAppLifeListener(appLifeListener);
		return (BaseApp)getBApp(context,needInitApp);
	}
	/** BaseApp生命周期监听 */
	private static AppLifeListener appLifeListener=new AppLifeListener() {
		@Override
		public void onInitApp(BApp baseApp, Activity context) {
		}
		@Override
		public void onTerminate(BApp baseApp) {
		}
	};

	
}
