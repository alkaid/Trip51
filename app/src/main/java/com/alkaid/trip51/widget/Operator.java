// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 

package com.alkaid.trip51.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.alkaid.trip51.R;

public class Operator extends LinearLayout {
    public static interface FocusListener {

        public abstract void focus();
    }

    public static interface OperationListener {

        public abstract void onAddClick(int i);

        public abstract void onSubClick(int i);

        public abstract void onTextChange(int i);
    }


    private boolean isFromInput;
    private ImageButton mAddBtn;
    private EditText mEditNum;
    private FocusListener mFocusListener;
    private OperationListener mOperationCallback;
    private ImageButton mSubBtn;
    public int maxCount;
    public int minCount;
    public int selectedCount;
    private int resMinusIconId=-1;
    private int resAddIconId=-1;
    private int colorStyle;
    private int numMinWidth;
    private static final int COLOR_STYLE_ORANGE=0;
    private static final int COLOR_STYLE_GREEN=1;

    public Operator(Context context) {
        this(context, null);
    }

    public Operator(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        maxCount = 30;
        minCount = 0;
        selectedCount = 0;
        isFromInput = true;
        resMinusIconId = R.drawable.purchase_amount_minus;
        resAddIconId = R.drawable.purchase_amount_add;
        if(null!=attributeset) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable.coodOper);
            maxCount = typedArray.getInteger(R.styleable.coodOper_opMaxCount, 30);
            minCount = typedArray.getInteger(R.styleable.coodOper_opMinCount, 0);
            colorStyle=typedArray.getInteger(R.styleable.coodOper_colorStyle, -1);
//            numMinWidth=typedArray.getDimensionPixelSize()
            switch (colorStyle){
                case COLOR_STYLE_GREEN:
                    resMinusIconId=R.drawable.purchase_amount_minus;
                    resAddIconId=R.drawable.purchase_amount_add;
                    break;
                case COLOR_STYLE_ORANGE:
                    resMinusIconId=R.drawable.purchase_amount_minus_orange;
                    resAddIconId=R.drawable.purchase_amount_add_orange;
                    break;
            }
            resMinusIconId = typedArray.getResourceId(R.styleable.coodOper_opAddBtnBackground,resMinusIconId);
            resAddIconId = typedArray.getResourceId(R.styleable.coodOper_opMinusBtnBackground,resAddIconId);
        }
        init(context);
    }

    private void checkvalue(int i, int j, int k) {
        if (i >= j) {
            mSubBtn.setEnabled(true);
            mAddBtn.setEnabled(false);
        } else if (i <= k) {
            mAddBtn.setEnabled(true);
            mSubBtn.setEnabled(false);
        } else {
            mAddBtn.setEnabled(true);
            mSubBtn.setEnabled(true);
        }
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_operator, this, true);
        mSubBtn = (ImageButton) findViewById(R.id.sub_btn);
        mAddBtn = (ImageButton) findViewById(R.id.add_btn);
        mEditNum = (EditText) findViewById(R.id.edit_num);
        if(resMinusIconId>0)
            mSubBtn.setImageResource(resMinusIconId);
        if(resAddIconId>0)
            mAddBtn.setImageResource(resAddIconId);
        mEditNum.addTextChangedListener(new TextWatcher() {
                                            public void afterTextChanged(Editable editable) {
                                                isFromInput = true;
                                            }

                                            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
                                            }

                                            public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
                                                int l;
                                                l = 0;
                                                if (TextUtils.isEmpty(charsequence.toString()))
                                                    mOperationCallback.onTextChange(0);
                                                int i1 = Integer.parseInt(mEditNum.getText().toString());
                                                l = i1;
                                                if (l > maxCount) {
                                                    l = maxCount;
                                                    mEditNum.setText((new StringBuilder()).append(l).append("").toString());
                                                    mEditNum.setSelection(mEditNum.getText().toString().trim().length());
                                                }
                                                if (isFromInput)
                                                    mOperationCallback.onTextChange(l);
                                                checkValue(l);
                                            }
                                        }
        );
        mSubBtn.setOnClickListener(new android.view.View.OnClickListener() {
                                       public void onClick(View view) {
                                           int k = Integer.parseInt(mEditNum.getText().toString());
                                           int i = k;
                                           if (i <= minCount) {
                                               mSubBtn.setEnabled(false);
                                           } else {
                                               isFromInput = false;
                                               EditText edittext = mEditNum;
                                               StringBuilder stringbuilder = new StringBuilder();
                                               int j = i - 1;
                                               edittext.setText(stringbuilder.append(j).append("").toString());
                                               mEditNum.setSelection(mEditNum.getText().toString().trim().length());
                                               selectedCount = j;
                                               mOperationCallback.onSubClick(maxCount);
                                           }
                                       }
                                   }
        );
        mAddBtn.setOnClickListener(new android.view.View.OnClickListener() {
                                       public void onClick(View view) {
                                           int k = Integer.parseInt(mEditNum.getText().toString());
                                           int i = k;
                                           if (i >= maxCount) {
                                               mAddBtn.setEnabled(false);
                                           } else {
                                               isFromInput = false;
                                               EditText edittext = mEditNum;
                                               StringBuilder stringbuilder = new StringBuilder();
                                               int j = i + 1;
                                               edittext.setText(stringbuilder.append(j).append("").toString());
                                               mEditNum.setSelection(mEditNum.getText().toString().trim().length());
                                               selectedCount = j;
                                               mOperationCallback.onAddClick(maxCount);
                                           }
                                       }
                                   }
        );
    }

    public void checkValue(int i) {
        checkvalue(i, maxCount, minCount);
    }

    public void disableOperation() {
        mAddBtn.setEnabled(false);
        mSubBtn.setEnabled(false);
        mEditNum.setEnabled(false);
    }

    public void restoreValue(boolean flag, int i, int j, int k) {
        if (!flag) {
            mEditNum.setText("0");
            disableOperation();
        } else {
            mEditNum.setText((new StringBuilder()).append(i).append("").toString());
            checkvalue(i, j, k);
        }
    }

    public void setOperationCallback(OperationListener operationlistener) {
        mOperationCallback = operationlistener;
    }

    public void setTouchListenerForEditText(FocusListener focuslistener) {
        mFocusListener = focuslistener;
        mEditNum.setOnTouchListener(new android.view.View.OnTouchListener() {
                                        public boolean onTouch(View view, MotionEvent motionevent) {
                                            if (motionevent.getAction() == 1 && mFocusListener != null)
                                                mFocusListener.focus();
                                            return false;
                                        }
                                    }
        );
    }

    public void setValue(int num){
        mEditNum.setText(num + "");
    }



/*
    static boolean access$202(Operator operator, boolean flag)
    {
        operator.isFromInput = flag;
        return flag;
    }

*/


}
