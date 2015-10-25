package com.alkaid.base.view.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

import com.alkaid.base.common.LogUtil;
import com.alkaid.base.exception.ExceptionHandler;
import com.alkaid.base.exception.TradException;
import com.example.com.alkaid.base.R;
/**
 * 整个应用的Context包装类 目前仅支持包装Activity
 * @author Administrator
 *
 */
public class BContextWrap {
	private boolean isExist;
	protected ExceptionHandler eHandler;
	protected Activity context;
	protected BContextWrap(Activity context){
		this.context=context;
		eHandler=new ExceptionHandler(context);
	}
	public static BContextWrap wrap(Context context){
		//限制context参数必须为Activity类型
		if(context instanceof Activity){
			return new BContextWrap((Activity) context);
		}else{
			throw new IllegalArgumentException("The argument of context must be Activity!");
		}
	}
	public void onCreate(Bundle savedInstanceState){
	}
	public void onResume() {
		//UMeng统计
//		MobclickAgent.onResume(context);
	}
	public void onPause() {
		//UMeng统计
//		MobclickAgent.onPause(context);
	}
	/**
	 * 处理异常 
	 * @param e
	 */
	public void handleException(TradException e){
		String msg=e.getMessage();
		msg=null!=msg?msg:context.getString(e.getResId());
		LogUtil.e(msg,e);
		eHandler.sendExceptionMsg(msg);
	}
	/**
	 * 处理异常  为那些无法继承{@link BActivity}的Activity提供异常处理<br/>
	 * 等同于{@link BContextWrap#wrap(Context)}后的实例调用{@link BContextWrap#handleException(TradException)}方法<br/>
	 * 若已有{@link BContextWrap}实例就不要用此静态方法了
	 * @param context
	 * @param e
	 */
	public static void handleException(Context context,TradException e){
		wrap(context).handleException(e);
	}
	/**
	 * 退出应用
	 */
    public void exitOnDialog() {
    	new AlertDialog.Builder(context)
    		.setMessage(R.string.common_exit_isExit)
    		.setNegativeButton(R.string.common_cancel, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.setPositiveButton(R.string.common_sure, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					context.finish();
	                android.os.Process.killProcess(android.os.Process.myPid());
	                System.exit(0);
				}
			}).create().show();
    };
    /**
     * 退出应用
     */
    public void exitOnDoubleBack(){
        if(!isExist){  
            isExist = true;  
            Toast.makeText(context,R.string.common_exit_remind, Toast.LENGTH_SHORT).show(); 
            eHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					isExist=false;
				}
			}, 4000);
        }else{  
	        context.finish(); 
	        android.os.Process.killProcess(android.os.Process.myPid());
	        System.exit(0);  
        }
    }
    
}
