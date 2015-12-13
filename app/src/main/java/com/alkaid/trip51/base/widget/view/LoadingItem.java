// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.base.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.alkaid.trip51.R;

public class LoadingItem extends LinearLayout
{

    public LoadingItem(Context context)
    {
        this(context, null);
    }

    public LoadingItem(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate_alpha);
        animation.setInterpolator(new LinearInterpolator());
        findViewById(R.id.anim_icon).startAnimation(animation);
    }
}
