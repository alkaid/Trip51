package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.shop.adapter.MyClientQuestionAdapter;
import com.alkaid.trip51.shop.adapter.MyCouponAdapter;

/**
 * Created by alkaid on 2015/11/8.
 */
public class MyClientQuestionListFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout llQuestion =(LinearLayout) inflater.inflate(R.layout.item_user_client_question, container, false);
        ListView lvQuestion =(ListView)llQuestion.findViewById(R.id.lv_my_client_question);
        lvQuestion.setAdapter(new MyClientQuestionAdapter(getContext()));
        return llQuestion;
    }
}