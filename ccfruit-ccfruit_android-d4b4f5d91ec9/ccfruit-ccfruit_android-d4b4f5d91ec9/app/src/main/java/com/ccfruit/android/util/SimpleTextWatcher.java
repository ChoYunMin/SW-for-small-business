package com.ccfruit.android.util;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ccfruit.android.R;

/**
 * Created by BetaS on 2016-03-24.
 */
public class SimpleTextWatcher implements TextWatcher {
    private Activity context;
    private int length = 0;
    private int next = 0;

    public SimpleTextWatcher(Activity context, int length, int next) {
        this.context = context;
        this.length = length;
        this.next = next;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == length) {
            if(next == 0) {
                ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0, 0);
            } else {
                (context.findViewById(next)).requestFocus();
            }
        }
    }
}
