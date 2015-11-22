package com.alkaid.trip51.base.widget;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.view.Window;
import android.widget.Toast;

import com.alkaid.base.view.base.BActivity;
import com.alkaid.base.view.base.BContextWrap;

public class BaseActivity extends BActivity {
	private BaseContextWrap baseContextWrap;
	protected App app;
	protected ProgressDialog pdg;
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
		pdg=new ProgressDialog(context);
	}
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
		Toast.makeText(context,msg,Toast.LENGTH_SHORT);
	}
	protected void toastShortAsync(String msg){
		Looper.prepare();
		Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		Looper.loop();
	}
}
