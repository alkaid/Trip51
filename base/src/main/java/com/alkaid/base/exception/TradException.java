/**
 * 
 */
package com.alkaid.base.exception;

import android.text.TextUtils;

import com.example.com.alkaid.base.R;


/**
 * @author alkaid
 * 业务异常类
 */
public class TradException extends Exception {
	private static final long serialVersionUID = 76782219790970565L;
	private int resId=0;
	public TradException(){
		super();
	}
	public TradException(String msg){
		super(msg);
	}
	public static TradException create(String msg){
		if(TextUtils.isEmpty(msg)){
			return new TradException(0);
		}
		return new TradException(msg);
	}
	public TradException(Throwable e) {
		super(null,e);
	}
	public TradException(String msg,Throwable e) {
		super(msg, e);
	}
	public TradException(int resId) {
		super();
		this.resId=resId;
	}
	public TradException(int resId,Throwable e) {
		super(null, e);
		this.resId=resId;
	}
	public int getResId(){
		return this.resId!=0?resId:R.string.exception_unknow;
	}
	
}
