package com.ccfruit.android.ui.anim;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by betas on 1/4/16.
 */
public class ExpandAnimation extends Animation {
    private View v = null;
    private View start = null;
    private View target = null;

    private int startHeight = 0;
    private int targetHeight = 0;

    public ExpandAnimation(View v, View start, View target) {
        this.v = v;

        this.start = start;
        this.target = target;
        startHeight = start.getMeasuredHeight();
        targetHeight = target.getMeasuredHeight();

        setFillEnabled(true);
        setFillAfter(true);

        setDuration(500);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        lp.height = (int)((targetHeight-startHeight) * interpolatedTime) + startHeight;
        v.setLayoutParams(lp);

        start.setAlpha((float)(1.0-(1.0*interpolatedTime)));
        target.setAlpha((float)(1.0*interpolatedTime));
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
