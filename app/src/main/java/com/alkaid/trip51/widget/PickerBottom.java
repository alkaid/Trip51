package com.alkaid.trip51.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alkaid.trip51.R;

/**
 * Created by df on 2015/12/31.
 */
public class PickerBottom extends LinearLayout{
    private ViewGroup layBtns;
    public PickerBottom(Context context) {
        super(context);
        init();
    }

    public PickerBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        View parent = LayoutInflater.from(getContext()).inflate(R.layout.view_picker_bottom,this,true);
        layBtns= (ViewGroup) parent.findViewById(R.id.layBtns);
        parent.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PickerBottom.this.setVisibility(View.GONE);
            }
        });
    }
    public PickerBottom addBtn(String text,OnClickListener onClickListener){
        Button btn= (Button) LayoutInflater.from(getContext()).inflate(R.layout.btn_picker_bottom,layBtns,false);
        btn.setText(text);
        btn.setOnClickListener(onClickListener);
        layBtns.addView(btn);
        return this;
    }
}
