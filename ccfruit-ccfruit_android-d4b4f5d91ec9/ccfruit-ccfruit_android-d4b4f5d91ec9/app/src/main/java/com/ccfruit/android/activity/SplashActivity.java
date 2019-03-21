package com.ccfruit.android.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import com.ccfruit.android.R;
import com.ccfruit.android.activity.base.BaseActivity;
import com.ccfruit.android.util.PhoneInfo;

/**
 * Created by betas on 12/28/15.
 */
public class SplashActivity extends BaseActivity {
    private Runnable nextScreen = new Runnable() {
        @Override
        public void run() {
            finish();
            startActivity(MainActivity.class);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_00_splash, Theme.NO_TITLE);

        PhoneInfo.refresh(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();

        setTimeout(1000, nextScreen);
    }

    @Override
    protected void onPause() {
        super.onPause();
        killTimeout(nextScreen);
    }
}
