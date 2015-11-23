package com.alkaid.trip51.base.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

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
		pdg=new ProgressDialog(context);
	}

	protected ProgressDialog pdg;
	protected void setDefaultPdgCanceListener(final String tag){
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
		if(null==pdg){
			pdg=new ProgressDialog(context);
		}
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
}
