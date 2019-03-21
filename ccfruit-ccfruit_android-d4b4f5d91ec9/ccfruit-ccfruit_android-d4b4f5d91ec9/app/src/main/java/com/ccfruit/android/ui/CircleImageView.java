package com.ccfruit.android.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ccfruit.android.Preference;
import com.ccfruit.android.util.GraphicUtil;

/**
 * Created by BetaS on 2016-02-05.
 */
public class CircleImageView extends ImageView {
    public CircleImageView(Context context) {
        super(context);
    }
    public CircleImageView(Context context, AttributeSet attr) {
        super(context, attr);
    }
    public CircleImageView(Context context, AttributeSet attr, int theme) {
        super(context, attr, theme);
    }

    public void setImageBitmap(Bitmap bm) {
        setImageBitmap(bm, Color.WHITE, 5);
        //setImageBitmap(bm, 0, 0);
    }
    public void setImageBitmap(Bitmap bm, int strokeColor, int strokeWidth) {
        if(bm != null) {
            Bitmap bitmap = GraphicUtil.cropCenterBitmap(bm).copy(Bitmap.Config.ARGB_8888,true);
            Canvas canvas = new Canvas(bitmap);

            int width = canvas.getWidth();
            int height = canvas.getHeight();

            RoundedBitmapDrawable round = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            round.setCircular(true);
            round.setAntiAlias(true);
            setImageDrawable(round);

            if(strokeWidth > 0) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth * 2);
                paint.setColor(strokeColor);
                paint.setAntiAlias(true);

                canvas.drawCircle(width / 2, height / 2, width / 2, paint);
            }
        }
    }
}
