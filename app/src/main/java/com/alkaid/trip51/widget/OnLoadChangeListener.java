// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.widget;

import android.graphics.Bitmap;

public interface OnLoadChangeListener
{

    public abstract void onImageLoadFailed();

    public abstract void onImageLoadStart();

    public abstract void onImageLoadSuccess(Bitmap bitmap);
}
