package com.alkaid.trip51.booking;

import android.os.Bundle;
import android.view.View;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseActivity;

/**
 * Created by alkaid on 2015/11/7.
 */
public class BookingFilterActivity extends BaseActivity {
    private View layContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_booking);
        layContent=findViewById(R.id.content);
    }
}
