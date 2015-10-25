package com.alkaid.base.view.base;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * @author Alkaid
 *
 */

public abstract class BApp extends Application{
	/** 全局数据 类似j2ee的request,为当get某项值后会自动移除该值再也无法获得*/
	protected Map<String, Object> session=new HashMap<String, Object>();
	/** 屏幕相关参数*/
	public DisplayMetrics dm ;
	protected static boolean inited=false;
//	public CookieStore cookieStore=null;
	protected static AppLifeListener mAppLifeListener;
//	public User user=null;
	
	/**
	 * 获得全局单例  若没有初始化过BaseApp则默认会初始化
	 * @param context
	 * @return
	 */
	protected static BApp getBaseApp(Context context){
		return getBApp(context, true);
	}
	/**
	 * 获得全局单例  根据needInitApp判断在没有初始化过BaseApp的情况下是否要初始化
	 * @param context
	 * @param needInitApp 是否需要初始化BaseApp
	 * @return
	 */
	protected static BApp getBApp(Context context,boolean needInitApp){
		BApp bApp=(BApp)context.getApplicationContext();
		if(needInitApp&&!inited)
			initApp(bApp,(Activity) context);
		return bApp;
	}

	/** 初始化应用*/
	private static void initApp(BApp bApp, Activity context){
		inited=true;
		Display display = context.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm=new DisplayMetrics();
		display.getMetrics(dm);
		bApp.setDm(dm);
//		baseApp.setWidthRate(baseApp.getWidth()/320.0f);
//		baseApp.setHeightRate(baseApp.getHeight()/480.0f);
		if(null!=mAppLifeListener){
			mAppLifeListener.onInitApp(bApp,context);
		}
	}
	
	@Override
	public void onTerminate() {
		if(null!=mAppLifeListener){
			mAppLifeListener.onTerminate(this);
		}
		super.onTerminate();
	}
	
	/**
	 * {@link BApp}的生命周期监听<br/>
	 * 在{@link BApp#initApp(BApp, Activity)}
	 * 和 {@link BApp#onTerminate()}等方法中将会调用接口相应方法
	 * @author Administrator
	 *
	 */
	protected static interface AppLifeListener{
		void onInitApp(BApp bApp,Activity context);
		void onTerminate(BApp bApp);
	}
	
	public void putData(String key,Object data){
		session.put(key, data);
	}
	
	public Object getData(String key){
		Object m=session.get(key);
		session.remove(key);
		return m;
	}
	private void setDm(DisplayMetrics dm) {
		this.dm = dm;
	}
	
	protected static void setAppLifeListener(AppLifeListener appLifeListener) {
		mAppLifeListener=appLifeListener;
	}
	
}
