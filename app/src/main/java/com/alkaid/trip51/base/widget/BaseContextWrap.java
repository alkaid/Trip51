package com.alkaid.trip51.base.widget;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.view.base.BContextWrap;
import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.view.LoadingDialog;
import com.alkaid.trip51.main.nav.SplashScreenActivity;

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
		//进程被系统自动杀死后Application重建回到当前页面，由于没有经过First Activity的初始化，数据会异常。这里判断下进行重启
		if(!(context instanceof SplashScreenActivity)) {
			if (app.locationService() == null) {
				restartApp();
			}
		}
		pdg=new ProgressDialog(context);
	}

//	protected ProgressDialog pdg;
	protected AlertDialog pdg;
	protected void setDefaultPdgCanceListener(final String tag){
		getProgressDialog();
		pdg.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				App.mApiService().abort(tag);
			}
		});
	}
	protected void dismissPdg(){
		if(null!=pdg&&pdg.isShowing())
			pdg.dismiss();
	}
	protected void showPdg(){
		showPdg(null);
	}
	protected void showPdg(String msg){
		showPdg(true, msg);
	}
	protected void showPdg(boolean cancelable,String msg){
		getProgressDialog();
		pdg.setCancelable(cancelable);
		pdg.setMessage(msg);
		pdg.show();
	}
	protected void toastShort(String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	protected void toastShortAsync(String msg){
		Looper.prepare();
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		Looper.loop();
	}
	protected void toastLong(String msg){
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
	public Dialog getProgressDialog(){
		if(null==pdg){
			pdg=new LoadingDialog(context, R.style.LoadingDialog);
		}
		return pdg;
	}

	/**
	 * 杀死并重启APP
	 */
	public void restartApp(){
		LogUtil.w("Data error!Restart trip51 now!");
		Intent restartIntent = context.getPackageManager()
				.getLaunchIntentForPackage(context.getPackageName() );
		PendingIntent intent = PendingIntent.getActivity(context, 0,restartIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.RTC, System.currentTimeMillis() + 1, intent);
		System.exit(2);
	}
}
