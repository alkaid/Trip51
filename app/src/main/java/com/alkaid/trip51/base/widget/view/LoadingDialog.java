package com.alkaid.trip51.base.widget.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alkaid.trip51.R;

/**
 * Created by alkaid on 2015/12/27.
 */
public class LoadingDialog extends AlertDialog {
    private View layMain;
    private TextView tvMsg;
    private String msg;
    public LoadingDialog(Context context)
    {
        super(context);
    }
    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layMain= LayoutInflater.from(getContext()).inflate(R.layout.loading_dialog,null);
        tvMsg = (TextView) layMain.findViewById(R.id.tvMsg);
        setContentView(layMain);
    }

    @Override
    public void setMessage(CharSequence message) {
        if(null!=message)
            msg=message.toString();
    }

    @Override
    public void show() {
        if(!TextUtils.isEmpty(msg))
            tvMsg.setText(msg);
        super.show();
    }
}
