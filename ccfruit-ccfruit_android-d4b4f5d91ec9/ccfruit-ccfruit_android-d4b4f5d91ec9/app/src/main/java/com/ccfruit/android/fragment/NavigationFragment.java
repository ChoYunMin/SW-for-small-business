package com.ccfruit.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ccfruit.android.Preference;
import com.ccfruit.android.R;
import com.ccfruit.android.activity.base.BaseActivity;
import com.ccfruit.android.fragment.base.BaseFragment;


/**
 * Created by betas on 12/30/15.
 */
public class NavigationFragment extends BaseFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.fragment_navbar);
    }

    @Override
    public void onLoad() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        findViewById(R.id.btn_menu).setOnClickListener(this);
    }

    public void setTitle(String title) {
        if(title != null && title.length() > 0) {
            ((TextView)findViewById(R.id.txt_title)).setText(title);
        } else {
            ((TextView)findViewById(R.id.txt_title)).setText("청춘과수원");
        }
    }

    public void setMenu(boolean menu) {
        if(menu) {
            findViewById(R.id.btn_menu).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.btn_menu).setVisibility(View.GONE);
        }
    }
    public void setBack(boolean back) {
        if(back) {
            findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
            findViewById(R.id.txt_title).setPadding(0, 0, 0, 0);
        } else {
            findViewById(R.id.btn_back).setVisibility(View.GONE);
            findViewById(R.id.txt_title).setPadding(Preference.getPixelFromDP(getContext(), 20), 0, 0, 0);
        }
    }

    @Override
    public void onClickEvent(int id, View v) {
        if(id == R.id.btn_menu) {
            ((BaseActivity) getActivity()).toggleSideMenu();
        } else if(id == R.id.btn_back) {
            getActivity().setResult(Activity.RESULT_CANCELED);
            getActivity().onBackPressed();
        }
    }
}
