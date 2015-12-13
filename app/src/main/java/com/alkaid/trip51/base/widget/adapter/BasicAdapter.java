// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.base.widget.adapter;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alkaid.trip51.R;

public abstract class BasicAdapter extends BaseAdapter
{

    public static final Object EMPTY = new Object();
    public static final Object ERROR = new Object();
    public static final Object FOOT = new Object();
    public static final Object HEAD = new Object();
    public static final Object LAST_EXTRA = new Object();
    public static final Object LOADING = new Object();
    public static final Object LOCATION_ERROR = new Object();

    public BasicAdapter()
    {
    }

    private View initImageAndTextView(String errorText, int resIdErrorImg, android.view.View.OnClickListener onRetryListener, ViewGroup viewgroup, View view)
    {
        if(view == null)
        {
            view = null;
        } else
        {
            view.setMinimumHeight(viewgroup.getMeasuredHeight());
            view.setMinimumWidth(viewgroup.getMeasuredWidth());
            ((ImageView)view.findViewById(R.id.error_bad)).setImageResource(resIdErrorImg);
            ((TextView)view.findViewById(R.id.error_text_bad)).setText(errorText);
            View btnRetry = view.findViewById(R.id.btn_retry);
            btnRetry.setVisibility(onRetryListener==null?View.GONE:View.VISIBLE);
            btnRetry.setOnClickListener(onRetryListener);
            btnRetry.requestFocus();
        }
        return view;
    }

    protected View getEmptyCryFaceView(String s, ViewGroup viewgroup, View view)
    {
        return getEmptyImageAndTextView(s, R.drawable.bad_face_lib, viewgroup, view);
    }

    protected View getEmptyImageAndTextView(String s, int i, android.view.View.OnClickListener onclicklistener, ViewGroup viewgroup, View view)
    {
        View view1;
        view1 = null;
        if(view != null && view.getTag() == EMPTY)
            view1 = view;
        if(view1 == null)
        {
            view1 = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.emtpy_view_image_and_text, viewgroup, false);
            view1.setTag(EMPTY);
            initImageAndTextView(s, i, onclicklistener, viewgroup, view1);
        }
        return view1;
    }

    protected View getEmptyImageAndTextView(String s, int i, ViewGroup viewgroup, View view)
    {
        return getEmptyImageAndTextView(s, i, null, viewgroup, view);
    }

    protected TextView getEmptyView(String s, String s1, ViewGroup viewgroup, View view)
    {
        TextView textview;
        if(view == null)
            textview = null;
        else
        if(view.getTag() == EMPTY)
            textview = (TextView)view;
        else
            textview = null;
        if(textview == null)
        {
            textview = (TextView) LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.simple_list_item_18, viewgroup, false);
            textview.setTag(EMPTY);
            Drawable drawable = viewgroup.getResources().getDrawable(R.drawable.empty_page_nothing);    //TODO 换图
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            textview.setCompoundDrawablePadding(8);
            textview.setCompoundDrawables(drawable, null, null, null);
            textview.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if(TextUtils.isEmpty(s))
        {
            if(!TextUtils.isEmpty(s1))
                textview.setText(Html.fromHtml(s1));
        } else
        {
            textview.setText(Html.fromHtml(s));
        }
        return textview;
    }

//    protected View getFailedView(String s, com.dianping.widget.LoadingErrorView.LoadRetry loadretry, ViewGroup viewgroup, View view)
//    {
//        View view1;
//        if(view == null)
//            view1 = null;
//        else
//        if(view.getTag() == ERROR)
//            view1 = view;
//        else
//            view1 = null;
//        if(view1 == null)
//        {
//            view1 = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.error_item, viewgroup, false);
//            view1.setTag(ERROR);
//        }
//        ((TextView)view1.findViewById(0x1020014)).setText(s);
//        if(!(view1 instanceof LoadingErrorView))
//            view1 = null;
//        else
//            ((LoadingErrorView)view1).setCallBack(loadretry);
//        return view1;
//    }

    protected View getLoadingView(ViewGroup viewgroup, View view)
    {
        View view1;
        view1 = null;
        if(view != null && view.getTag() == LOADING)
            view1 = view;
        if(view1 == null)
        {
            view1 = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.loading_item, viewgroup, false);
            view1.setTag(LOADING);
        }
        return view1;
    }

    protected View getLocationErrorView(String s, android.view.View.OnClickListener onclicklistener, ViewGroup viewgroup, View view)
    {
        View view1;
        view1 = null;
        if(view != null && view.getTag() == LOCATION_ERROR)
            view1 = view;
        if(view1 == null)
        {
            view1 = LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.emtpy_view_image_and_text, viewgroup, false);
            view1.setTag(LOCATION_ERROR);
            initImageAndTextView(s, R.drawable.bad_face_lib, onclicklistener, viewgroup, view1);
        }
        return view1;
    }

}
