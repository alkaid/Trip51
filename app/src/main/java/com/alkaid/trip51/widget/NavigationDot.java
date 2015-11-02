// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.alkaid.trip51.R;

public class NavigationDot extends View
{

    protected static final Paint paint = new Paint(1);
    protected int currentDot;
    long deleayedTime;
    protected Bitmap dotNormal;
    private int dotNormalId;
    protected Bitmap dotPressed;
    private int dotPressedId;
    protected int dot_height;
    protected int dot_width;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    moveToNext();
                    sendEmptyMessageDelayed(1, deleayedTime);
                    break;

            }
        }
    }
;
    private int height;
    private boolean isLoop;
    protected int padding;
    protected int totalDot;
    protected int width;

    public NavigationDot(Context context)
    {
        this(context, ((AttributeSet) (null)));
    }

    public NavigationDot(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        deleayedTime = 0L;
        Resources resources = getResources();
        TypedArray typedarray = context.obtainStyledAttributes(attributeset, R.styleable.NavigationDot);
        dotPressedId = typedarray.getResourceId(R.styleable.NavigationDot_navigationDotSelected, 0);
        dotNormalId = typedarray.getResourceId(R.styleable.NavigationDot_navigationDotUnselected, 0);
        totalDot = typedarray.getInt(R.styleable.NavigationDot_navigationDotCount, 0);
        typedarray.recycle();
        if(dotPressedId != 0)
            dotPressed = BitmapFactory.decodeResource(resources, dotPressedId);
        else
            dotPressed = BitmapFactory.decodeResource(resources, R.drawable.navigation_dot_pressed);
        if(dotNormalId != 0)
            dotNormal = BitmapFactory.decodeResource(resources, dotNormalId);
        else
            dotNormal = BitmapFactory.decodeResource(resources,R.drawable.navigation_dot_normal);
        if(dotNormal != null)
        {
            dot_width = dotNormal.getWidth();
            dot_height = dotNormal.getHeight();
        }
        padding = dip2px(context, 6F);
    }

    public NavigationDot(Context context, boolean flag)
    {
        this(context, ((AttributeSet) (null)));
        isLoop = flag;
    }

    public static int dip2px(Context context, float f)
    {
        return (int)(0.5F + f * context.getResources().getDisplayMetrics().density);
    }

    private int measureHeight(int i)
    {
        int j;
        int k;
        j = android.view.View.MeasureSpec.getMode(i);
        k = android.view.View.MeasureSpec.getSize(i);
        int l=k;
        if(j != MeasureSpec.EXACTLY){
            l = dot_height + getPaddingTop() + getPaddingBottom();
            if(j == MeasureSpec.AT_MOST)
                l = Math.min(l, k);
            return l;
        }else{
            height = l;
            return l;
        }
    }

    private int measureWidth(int i)
    {
        int j;
        int k;
        j = android.view.View.MeasureSpec.getMode(i);
        k = android.view.View.MeasureSpec.getSize(i);
        int l = k;
        if(j != MeasureSpec.EXACTLY){
            l = (dot_width + padding) * totalDot + getPaddingLeft() + getPaddingRight();
            if(j == 0x80000000)
                l = Math.min(l, k);
            return l;
        }else {
            width = l;
            return l;
        }
    }

    public static int px2dip(Context context, float f)
    {
        return (int)(0.5F + f / context.getResources().getDisplayMetrics().density);
    }

    public void moveToNext()
    {
        if(currentDot < totalDot || isLoop)
        {
            int i = 1 + currentDot;
            currentDot = i;
            currentDot = i % totalDot;
            invalidate();
        }
    }

    public void moveToPosition(int i)
    {
        if(i <= totalDot)
        {
            currentDot = i;
            invalidate();
        }
    }

    public void moveToPrevious()
    {
        if(currentDot > 0){
            currentDot = -1 + currentDot;
            invalidate();
        }else{
            if(isLoop){
                currentDot = totalDot;
            }
        }
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.translate(getPaddingLeft(), getPaddingTop());
        int i = dot_width * totalDot + padding * (-1 + totalDot);
        int j = (width - i) / 2;
        int k = 0;
        while(k < totalDot) 
        {
            Bitmap bitmap;
            if(currentDot == k)
                bitmap = dotPressed;
            else
                bitmap = dotNormal;
            canvas.drawBitmap(bitmap, j + k * (dot_width + padding), 0.0F, paint);
            k++;
        }
    }

    protected void onMeasure(int i, int j)
    {
        setMeasuredDimension(measureWidth(i), measureHeight(j));
    }

    public void setCurrentIndex(int i)
    {
        if(i >= 0 && i <= totalDot && currentDot != i)
        {
            currentDot = i;
            invalidate();
        }
    }

    public void setDotNormalBitmap(Bitmap bitmap)
    {
        dotNormal = bitmap;
        dot_width = dotNormal.getWidth();
        dot_height = dotNormal.getHeight();
    }

    public void setDotNormalId(int i)
    {
        dotNormal = BitmapFactory.decodeResource(getResources(), i);
        dot_width = dotNormal.getWidth();
        dot_height = dotNormal.getHeight();
    }

    public void setDotPressedBitmap(Bitmap bitmap)
    {
        dotPressed = bitmap;
    }

    public void setDotPressedId(int i)
    {
        dotPressed = BitmapFactory.decodeResource(getResources(), i);
    }

    public void setFlipInterval(int i)
    {
        long l;
        if(i > 0)
            l = i;
        else
            l = 500L;
        deleayedTime = l;
    }

    public void setTotalDot(int i)
    {
        if(i > 0)
        {
            totalDot = i;
            requestLayout();
        }
    }

    public void startFlipping()
    {
        deleayedTime = 500L;
        handler.sendEmptyMessageDelayed(1, deleayedTime);
    }

    public void stopFlipping()
    {
        handler.removeMessages(1);
    }

}
