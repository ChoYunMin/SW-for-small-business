package com.ccfruit.android.activity;

import android.os.Bundle;
import android.view.View;

import com.ccfruit.android.R;
import com.ccfruit.android.activity.base.BaseActivity;

/**
 * Created by betas on 3/20/16.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_01_main);
    }

    @Override
    public void onClick(View v) {
    }
}
