package com.ccfruit.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccfruit.android.R;

/**
 * Created by betas on 12/31/15.
 */
public class SidemenuButton extends LinearLayout {

    private String target = "";
    private String text = "";
    private int icon = 0;

    public SidemenuButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SidemenuButton(Context context, AttributeSet attrs, int theme) {
        super(context, attrs, theme);

        target = attrs.getAttributeValue(null, "target");
        icon = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        text = getResources().getText(attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "text", 0), "").toString();

        initialize();
    }

    private void initialize() {
        View v = inflate(getContext(), R.layout.ui_sidemenu_item, this);

        ((ImageView)v.findViewById(R.id.img_icon)).setImageResource(icon);
        ((TextView)v.findViewById(R.id.txt_title)).setText(text);

        setFocusable(true);
        setClickable(true);
    }
}
