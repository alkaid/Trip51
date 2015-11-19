package com.alkaid.trip51.usercenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseFragment;

/**
 * Created by alkaid on 2015/11/19.
 */
public class NormalLoginFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_normal_login,container,false);
        Button btnLogin= (Button) v.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        return v;
    }
}
