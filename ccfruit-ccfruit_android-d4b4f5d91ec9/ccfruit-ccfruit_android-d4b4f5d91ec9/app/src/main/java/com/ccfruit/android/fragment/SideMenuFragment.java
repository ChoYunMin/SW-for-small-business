package com.ccfruit.android.fragment;

import android.os.Bundle;
import android.view.View;

import com.ccfruit.android.R;
import com.ccfruit.android.activity.base.BaseActivity;
import com.ccfruit.android.fragment.base.BaseFragment;


/**
 * Created by betas on 12/30/15.
 */
public class SideMenuFragment extends BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_sidemenu);
    }

    @Override
    public void onLoad() {
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClickEvent(int id, View v) {
        if(id == R.id.btn_close) {
            ((BaseActivity)getActivity()).toggleSideMenu();
            return;
        }

        ((BaseActivity)getActivity()).toggleSideMenu();
    }
}
