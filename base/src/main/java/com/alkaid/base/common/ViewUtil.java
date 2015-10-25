package com.alkaid.base.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import com.alkaid.base.view.base.BApp;

public class ViewUtil {
	public static StateListDrawable getBtnStateListDrawable(Context context, int drawId_enabled, int drawId_pressed)
	  {
	    StateListDrawable stateListDrawable = new StateListDrawable();
	    Drawable imgPressed = context.getResources().getDrawable(drawId_pressed);
	    Drawable imgEnabled = context.getResources().getDrawable(drawId_enabled);
	    stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, imgPressed);
	    stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, imgEnabled);
	    return stateListDrawable;
	  }
	
	public static int dp2px(int dp,Context context){
	    return (int)(dp * context.getResources().getDisplayMetrics().density);
	}
	
	/**
	 * 适配默认字体尺寸
	 * @return
	 */
	public static int getAdjustFontSize(Context context) {
		int width = ((BApp) context.getApplicationContext())
				.dm.widthPixels;
		if (width <= 320)
			return 16;
		if (width <= 480)
			return 18;
		if (width <= 540)
			return 21;
		if (width <= 800)
			return 23;
		return 26;
	}
	
	/**
	 * 适配默认行间距
	 * @return
	 */
	public static int getAdjustLineSpacing(Context context) {
		int width = ((BApp) context.getApplicationContext())
				.dm.widthPixels;
		if (width <= 240)
			return 0;
		if (width <= 320)
			return 5;
		if (width <= 480)
			return 7;
		if (width <= 540)
			return 9;
		if (width <= 800)
			return 11;
		return 13;
	}
	
}
