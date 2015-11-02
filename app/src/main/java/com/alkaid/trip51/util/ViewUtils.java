// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alkaid.base.common.LogUtil;

// Referenced classes of package com.dianping.util:
//            LogUtil, TextUtils

public class ViewUtils
{

    private static int screenHeightPixels;
    private static int screenWidthPixels;

    public ViewUtils()
    {
    }

    public static int dip2px(Context context, float f)
    {
        int i;
        if(context == null)
            i = (int)f;
        else
            i = (int)(0.5F + f * context.getResources().getDisplayMetrics().density);
        return i;
    }

    public static void disableView(View view)
    {
        if(view != null)
            view.setEnabled(false);
    }

    public static void enableView(View view)
    {
        if(view != null)
            view.setEnabled(true);
    }

    public static int getScreenHeightPixels(Context context)
    {
        int i;
        if(context == null)
        {
            LogUtil.e("Can't get screen size while the activity is null!");
            i = 0;
        } else
        if(screenHeightPixels > 0)
        {
            i = screenHeightPixels;
        } else
        {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getMetrics(displaymetrics);
            screenHeightPixels = displaymetrics.heightPixels;
            i = screenHeightPixels;
        }
        return i;
    }

    public static int getScreenWidthPixels(Context context)
    {
        int i;
        if(context == null)
        {
            LogUtil.e("Can't get screen size while the activity is null!");
            i = 0;
        } else
        if(screenWidthPixels > 0)
        {
            i = screenWidthPixels;
        } else
        {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((WindowManager)context.getSystemService("window")).getDefaultDisplay().getMetrics(displaymetrics);
            screenWidthPixels = displaymetrics.widthPixels;
            i = screenWidthPixels;
        }
        return i;
    }

    public static int getTextViewWidth(TextView textview, String s)
    {
        textview.setText(s);
        textview.measure(android.view.View.MeasureSpec.makeMeasureSpec(0, 0), android.view.View.MeasureSpec.makeMeasureSpec(0, 0));
        return textview.getMeasuredWidth();
    }

    public static int getTextViewWidth(TextView textview, String s, int i)
    {
        textview.setText(s);
        textview.setTextSize(2, i);
        textview.measure(android.view.View.MeasureSpec.makeMeasureSpec(0, 0), android.view.View.MeasureSpec.makeMeasureSpec(0, 0));
        return textview.getMeasuredWidth();
    }

    public static int getViewHeight(View view)
    {
        int i = 0;
        if(view != null)
        {
            view.measure(android.view.View.MeasureSpec.makeMeasureSpec(0, 0), android.view.View.MeasureSpec.makeMeasureSpec(0, 0));
            i = view.getMeasuredHeight();
        }
        return i;
    }

    public static int getViewWidth(View view)
    {
        int i = 0;
        if(view != null)
        {
            view.measure(android.view.View.MeasureSpec.makeMeasureSpec(0, 0), android.view.View.MeasureSpec.makeMeasureSpec(0, 0));
            i = view.getMeasuredWidth();
        }
        return i;
    }

    public static void hideView(View view)
    {
        hideView(view, false);
    }

    public static void hideView(View view, boolean flag)
    {
        if(view != null)
            if(flag)
                view.setVisibility(8);
            else
                view.setVisibility(4);
    }

//    public static boolean isPointInsideView(float f, float f1, View view)
//    {
//        boolean flag = true;
//        int ai[] = new int[2];
//        view.getLocationOnScreen(ai);
//        int i = ai[0];
//        int j = ai[flag];
//        if(f <= (float)i || f >= (float)(i + view.getWidth()) || f1 <= (float)j || f1 >= (float)(j + view.getHeight()))
//            flag = false;
//        return flag;
//    }

    public static boolean isShow(View view)
    {
        boolean flag;
        flag = false;
        if(view != null && view.getVisibility() == 0)
            flag = true;
        return flag;
    }

    public static int measureTextView(TextView textview)
    {
        int i;
        if(textview == null)
            i = -1;
        else
            i = (int)textview.getPaint().measureText(textview.getText().toString());
        return i;
    }

    public static int px2dip(Context context, float f)
    {
        int i;
        if(context == null)
            i = (int)f;
        else
            i = (int)(0.5F + f / context.getResources().getDisplayMetrics().density);
        return i;
    }

    public static void readOnlyView(EditText edittext)
    {
        if(edittext != null)
            edittext.setKeyListener(null);
    }

    public static void setVisibilityAndContent(TextView textview, String s)
    {
        if(!TextUtils.isEmpty(s))
        {
            textview.setText(s);
            textview.setVisibility(0);
        } else
        {
            textview.setVisibility(8);
        }
    }

    public static void showView(View view)
    {
        if(view != null)
            view.setVisibility(0);
    }

    public static void showView(View view, boolean flag)
    {
        showView(view, flag, false);
    }

    public static void showView(View view, boolean flag, boolean flag1)
    {
        if(flag)
            showView(view);
        else
            hideView(view, flag1);
    }

    public static float sp2px(Context context, float f)
    {
        if(context != null)
            f = TypedValue.applyDimension(2, f, context.getResources().getDisplayMetrics());
        return f;
    }

    public static void updateViewVisibility(View view, int i)
    {
        if(view != null)
            view.setVisibility(i);
    }
}
