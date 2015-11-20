package com.alkaid.trip51.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.alkaid.trip51.R;

/**
 * Created by alkaid on 2015/11/21.
 */
public class RatingBar extends android.widget.RatingBar {
    private Drawable starDrawable;

    public RatingBar(Context context) {
        this(context, null);
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        starDrawable = getResources().getDrawable(
                R.drawable.ratingstars);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Make sure to account for padding and margin, if you care.
        // I only cared about left padding.
        setMeasuredDimension(starDrawable.getIntrinsicWidth() * 5
                + getPaddingLeft(), starDrawable.getIntrinsicHeight());
    }
}
