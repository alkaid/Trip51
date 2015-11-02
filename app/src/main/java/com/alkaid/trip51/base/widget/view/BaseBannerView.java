// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.base.widget.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.util.ViewUtils;
import com.alkaid.trip51.widget.NavigationDot;
import com.alkaid.trip51.widget.OnLoadChangeListener;

import java.util.ArrayList;
import java.util.List;

public class BaseBannerView extends NovaFrameLayout
    implements android.support.v4.view.ViewPager.OnPageChangeListener
{
//    class AdaptiveNetworkImageView extends NetworkImageView
//    {
//
//        final BaseBannerView this$0;
//
//        public void setImageBitmap(Bitmap bitmap)
//        {
//            int i = bitmap.getWidth();
//            int j = bitmap.getHeight();
//            int k = ViewUtils.getScreenWidthPixels(getContext());
//            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, k, (j * k) / i, true);
//            if(bitmap1 != bitmap && !bitmap.isRecycled())
//                bitmap.recycle();
//            MyPager mypager = (MyPager)mPager;
//            int l;
//            if(bitmap1.getHeight() > ((MyPager)mPager).itemHeight)
//                l = bitmap1.getHeight();
//            else
//                l = ((MyPager)mPager).itemHeight;
//            mypager.itemHeight = l;
//            setImageBitmap(bitmap1);
//            mPager.setLayoutParams(new LayoutParams(-1, ((MyPager)mPager).itemHeight, 1));
//        }
//
//        public AdaptiveNetworkImageView(Context context)
//        {
//            AdaptiveNetworkImageView(context, null);
//        }
//
//        public AdaptiveNetworkImageView(Context context, AttributeSet attributeset)
//        {
//            AdaptiveNetworkImageView(context, attributeset, 0);
//        }
//
//        public AdaptiveNetworkImageView(Context context, AttributeSet attributeset, int i)
//        {
//            this$0 = BaseBannerView.this;
//            NetworkImageView(context, attributeset, i);
//        }
//    }

    class MyPagerAdapter extends PagerAdapter
        implements OnLoadChangeListener
    {

        public void destroyItem(ViewGroup viewgroup, int i, Object obj)
        {
            viewgroup.removeView((View)obj);
        }

        public int getCount()
        {
            return mImageViews.size();
        }

        public int getItemPosition(Object obj)
        {
            return -2;
        }

        public Object instantiateItem(ViewGroup viewgroup, int i)
        {
            if(((View)mImageViews.get(i)).getParent() == null)
                viewgroup.addView((View)mImageViews.get(i));
//            if(mImageViews.get(0) instanceof NetworkImageView)
//                ((NetworkImageView)mImageViews.get(0)).setLoadChangeListener(this);
            return mImageViews.get(i);
        }

        public boolean isViewFromObject(View view, Object obj)
        {
            boolean flag;
            if(view == obj)
                flag = true;
            else
                flag = false;
            return flag;
        }

        public void onImageLoadFailed()
        {
        }

        public void onImageLoadStart()
        {
        }

        public void onImageLoadSuccess(Bitmap bitmap)
        {
            setVisibility(0);
        }

    }

    class MyPager extends ViewPager
    {
        class MyGestureListener extends android.view.GestureDetector.SimpleOnGestureListener
        {

            public boolean onScroll(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
            {
                boolean flag;
                if(Math.abs(f1) < Math.abs(f))
                    flag = true;
                else
                    flag = false;
                return flag;
            }

        }


        int itemHeight;
        private GestureDetector mGestureDetector;

        public boolean dispatchTouchEvent(MotionEvent motionevent)
        {
            mLastTouchUpTime = SystemClock.elapsedRealtime();
            if(mGestureDetector.onTouchEvent(motionevent))
                getParent().requestDisallowInterceptTouchEvent(true);
            return dispatchTouchEvent(motionevent);
        }

        public MyPager(Context context)
        {
            super(context, null);
        }

        public MyPager(Context context, AttributeSet attributeset)
        {
            super(context, attributeset);
            itemHeight = 0x80000000;
            mGestureDetector = new GestureDetector(context, new MyGestureListener());
            setFadingEdgeLength(0);
        }
    }

    public static interface OnPageChangedListener
    {

        public abstract void onChanged(int i);
    }

    public static interface OnDragListener
    {

        public abstract void onDraged();
    }


    public static final int ANNOUNCELAY_HEAD_ID = 1111;
    private static final int AUTO_FLIP_INTERVAL = 5000;
    private static final int MSG_AUTO_FLIP = 1001;
    protected int HACK_ITEM_COUNT;
    protected ImageView mBtnClose;
    private OnDragListener mDragListener;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message message)
        {
            switch (message.what){
                case 1001:
                    autoFlip();
                    startAutoFlip();
                    break;
            }
//            JVM INSTR tableswitch 1001 1001: default 24
//        //                       1001 25;
//               goto _L1 _L2
//_L1:
//            return;
//_L2:
//            autoFlip();
//            startAutoFlip();
//            if(true) goto _L1; else goto _L3
//_L3:
        }
    }
;
    protected List mImageViews;
    long mLastTouchUpTime;
    protected NavigationDot mNaviDot;
//    private NovaActivity mNovaActivity;
private Activity mNovaActivity;
    private OnPageChangedListener mPageChangedListener;
    protected ViewPager mPager;
    private int pageIndex;

    public BaseBannerView(Context context)
    {
        this(context, null);
    }

    public BaseBannerView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mImageViews = new ArrayList();
        HACK_ITEM_COUNT = 0;
        setVisibility(View.GONE);
//        Context context1 = GAHelper.instance().getDpActivity(context);
//        if(context1 instanceof NovaActivity)
//            mNovaActivity = (NovaActivity)context1;
                if(context instanceof Activity)
            mNovaActivity = (Activity)context;
        initView(context);
    }

    void autoFlip()
    {
        if(mNovaActivity != null /*&& mNovaActivity.isResumed*/ && SystemClock.elapsedRealtime() - mLastTouchUpTime >= 5000L)
        {
            int i = 1 + mPager.getCurrentItem();
            if(i >= mPager.getAdapter().getCount())
                i = 0;
            mPager.setCurrentItem(i);
        }
    }

    public void hideCloseButton()
    {
        ViewUtils.hideView(mBtnClose, true);
    }

    protected void initView(Context context)
    {
        FrameLayout framelayout = new FrameLayout(getContext());
        framelayout.setId(1111);
        framelayout.setLayoutParams(new LayoutParams(-1, -2));
        mPager = new MyPager(context);
        mPager.setLayoutParams(new LayoutParams(-1, -2, 1));
        mPager.setAdapter(new MyPagerAdapter());
        mPager.setOnPageChangeListener(this);
        framelayout.addView(mPager);
        mBtnClose = new NovaImageView(context);
        mBtnClose.setImageResource(R.drawable.announcement_close);
        mBtnClose.setScaleType(android.widget.ImageView.ScaleType.CENTER);
        android.widget.FrameLayout.LayoutParams layoutparams = new LayoutParams(-2, -2, 21);
        layoutparams.setMargins(0, 0, ViewUtils.dip2px(getContext(), 10F), 0);
        mBtnClose.setLayoutParams(layoutparams);
        framelayout.addView(mBtnClose);
        mNaviDot = new NavigationDot(getContext(), true);
        android.widget.FrameLayout.LayoutParams layoutparams1 = new LayoutParams(-2, -2, 83);
        layoutparams1.setMargins(ViewUtils.dip2px(context, 10F), 0, 0, ViewUtils.dip2px(context, 6F));
        mNaviDot.setLayoutParams(layoutparams1);
        framelayout.addView(mNaviDot);
        addView(framelayout);
    }

    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        startAutoFlip();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoFlip();
    }

    public void onPageScrollStateChanged(int i)
    {
        if(i == 0 && mPager.getCurrentItem() != pageIndex)
            mPager.setCurrentItem(pageIndex, false);
    }

    public void onPageScrolled(int i, float f, int j)
    {
        if(mDragListener != null)
            mDragListener.onDraged();
    }

    public void onPageSelected(int i)
    {
        int j;
        pageIndex = i;
        j = mImageViews.size();
        if(HACK_ITEM_COUNT != 2 || j <= 1){/* goto _L2; else goto _L1*/
//            _L2:
            int k = (i - 1) % (j - HACK_ITEM_COUNT);
            if(HACK_ITEM_COUNT == 2 && k == -1 && j > 2)
                k = -1 + (j - HACK_ITEM_COUNT);
            mNaviDot.setCurrentIndex(k);
            if(mPageChangedListener != null)
                mPageChangedListener.onChanged(k);
//            GAHelper.instance().contextStatisticsEvent(getContext(), "basebanner", null, i, "slide");
            return;
        }else{
//            _L1:
            if(i != 0){ /*goto _L4; else goto _L3*/
//                _L4:
                if(i == -1 + mImageViews.size())
                    pageIndex = 1;
//                if(true) goto _L2; else goto _L5
                //            _L2:
                int k = (i - 1) % (j - HACK_ITEM_COUNT);
                if(HACK_ITEM_COUNT == 2 && k == -1 && j > 2)
                    k = -1 + (j - HACK_ITEM_COUNT);
                mNaviDot.setCurrentIndex(k);
                if(mPageChangedListener != null)
                    mPageChangedListener.onChanged(k);
//            GAHelper.instance().contextStatisticsEvent(getContext(), "basebanner", null, i, "slide");
                return;
            }else{
//                _L3:
                pageIndex = j - HACK_ITEM_COUNT;
            }
        }



    }

    public void setBtnOnCloseListener(android.view.View.OnClickListener onclicklistener)
    {
        if(mBtnClose != null)
            mBtnClose.setOnClickListener(onclicklistener);
    }

    public void setCloseDrawable(int i)
    {
        mBtnClose.setImageResource(i);
    }

//    public void setNaviDotGravity(int i)
//    {
//        Context context;
//        Object obj;
//        context = getContext();
//        obj = null;
//        i;
//        JVM INSTR lookupswitch 4: default 52
//    //                   3: 61
//    //                   5: 126
//    //                   17: 96
//    //                   21: 161;
//           goto _L1 _L2 _L3 _L4 _L5
//_L1:
//        mNaviDot.setLayoutParams(((android.view.ViewGroup.LayoutParams) (obj)));
//        return;
//_L2:
//        obj = new LayoutParams(-2, -2, 83);
//        ((android.widget.FrameLayout.LayoutParams) (obj)).setMargins(ViewUtils.dip2px(context, 10F), 0, 0, ViewUtils.dip2px(context, 6F));
//        continue; /* Loop/switch isn't completed */
//_L4:
//        obj = new LayoutParams(-2, -2, 81);
//        ((android.widget.FrameLayout.LayoutParams) (obj)).setMargins(0, 0, 0, ViewUtils.dip2px(context, 6F));
//        continue; /* Loop/switch isn't completed */
//_L3:
//        obj = new LayoutParams(-2, -2, 85);
//        ((android.widget.FrameLayout.LayoutParams) (obj)).setMargins(0, 0, ViewUtils.dip2px(context, 10F), ViewUtils.dip2px(context, 6F));
//        continue; /* Loop/switch isn't completed */
//_L5:
//        obj = new LayoutParams(-2, -2, 21);
//        ((android.widget.FrameLayout.LayoutParams) (obj)).setMargins(0, 0, ViewUtils.dip2px(context, 10F), 0);
//        if(true) goto _L1; else goto _L6
//_L6:
//    }

    public void setNavigationDotNormalDrawable(int i)
    {
        mNaviDot.setDotNormalId(i);
    }

    public void setNavigationDotPressedDrawable(int i)
    {
        mNaviDot.setDotPressedId(i);
    }

    public void setOnDragListener(OnDragListener ondraglistener)
    {
        mDragListener = ondraglistener;
    }

    public void setOnPageChangedListener(OnPageChangedListener onpagechangedlistener)
    {
        mPageChangedListener = onpagechangedlistener;
    }

    public void startAutoFlip()
    {
        stopAutoFlip();
        if(mImageViews.size() >= 2)
            mHandler.sendEmptyMessageDelayed(1001, 5000L);
    }

    public void stopAutoFlip()
    {
        mHandler.removeMessages(1001);
    }

    public void updateBannerView(int i, ArrayList arraylist)
    {
        updateBannerView(i, arraylist, true);
    }

    public void updateBannerView(int i, ArrayList arraylist, boolean flag)
    {
        int j = 0;
        int k;
        NavigationDot navigationdot;
        int l;
        ViewPager viewpager;
        if(flag)
            k = 2;
        else
            k = 0;
        HACK_ITEM_COUNT = k;
        mNaviDot.setTotalDot(i);
        navigationdot = mNaviDot;
        if(i > 1)
            l = 0;
        else
            l = 8;
        navigationdot.setVisibility(l);
        if(arraylist == null)
            mImageViews.clear();
        else
            mImageViews = (ArrayList)arraylist.clone();
        mPager.getAdapter().notifyDataSetChanged();
        viewpager = mPager;
        if(flag && mImageViews.size() > 1)
            j = 1;
        viewpager.setCurrentItem(j);
    }
}
