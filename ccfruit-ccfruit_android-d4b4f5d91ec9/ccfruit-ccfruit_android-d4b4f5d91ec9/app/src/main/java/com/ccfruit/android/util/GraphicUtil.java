package com.ccfruit.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.ccfruit.android.Preference;

public class GraphicUtil {

	public static void addHDivider(ViewGroup group) {
		Context context = group.getContext();
		
		View v = new View(context);
		v.setBackgroundColor(0xFFe3e3e3);

		group.addView(v, LayoutParams.MATCH_PARENT, Preference.getPixelByDp(context, 1));
	}
	
	public static void addVDivider(ViewGroup group) {
		Context context = group.getContext();
		
		View v = new View(context);
		v.setBackgroundColor(0xFFe3e3e3);

		group.addView(v, Preference.getPixelByDp(context, 1), LayoutParams.MATCH_PARENT);
	}
	public static Bitmap resizeBitmap(Bitmap src, int max) {
		if(src == null)
			return null;

		int width = src.getWidth();
		int height = src.getHeight();
		float rate = 0.0f;

		if (width > height) {
			rate = max / (float) width;
			height = (int) (height * rate);
			width = max;
		} else {
			rate = max / (float) height;
			width = (int) (width * rate);
			height = max;
		}

		return Bitmap.createScaledBitmap(src, width, height, true);
	}

	/**
	 * Bitmap을 ratio에 맞춰서 max값 만큼 resize한다.
	 *
	 * @param src
	 * @param max
	 * @param isKeep 작은 크기인 경우 유지할건지 체크..
	 * @return
	 */
	public static Bitmap resize(Bitmap src, int max, boolean isKeep) {
		if(!isKeep)
			return resizeBitmap(src, max);

		int width = src.getWidth();
		int height = src.getHeight();
		float rate = 0.0f;

		if (width > height) {
			if (max > width) {
				rate = max / (float) width;
				height = (int) (height * rate);
				width = max;
			}
		} else {
			if (max > height) {
				rate = max / (float) height;
				width = (int) (width * rate);
				height = max;
			}
		}

		return Bitmap.createScaledBitmap(src, width, height, true);
	}

	/**
	 * Bitmap 이미지를 정사각형으로 만든다.
	 *
	 * @param src 원본
	 * @param max 사이즈
	 * @return
	 */
	public static Bitmap resizeSquare(Bitmap src, int max) {
		if(src == null)
			return null;

		return Bitmap.createScaledBitmap(src, max, max, true);
	}

    public static Bitmap cropCenterBitmap(Bitmap src) {
        return cropCenterBitmap(src, Math.min(src.getWidth(), src.getHeight()), Math.min(src.getWidth(), src.getHeight()));
    }
	public static Bitmap cropCenterBitmap(Bitmap src, int w, int h) {
		if(src == null)
			return null;

		int width = src.getWidth();
		int height = src.getHeight();

		if(width < w && height < h)
			return src;

		int x = 0;
		int y = 0;

		if(width > w) {
            x = (width-w)/2;
        }

		if(height > h) {
            y = (height-h)/2;
        }

		int cw = w; // crop width
		int ch = h; // crop height

		if(w > width)
			cw = width;

		if(h > height)
			ch = height;

		return Bitmap.createBitmap(src, x, y, cw, ch);
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
