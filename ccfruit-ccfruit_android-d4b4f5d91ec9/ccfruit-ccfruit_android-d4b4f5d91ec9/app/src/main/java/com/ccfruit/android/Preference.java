package com.ccfruit.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;

public class Preference {
	public static Bitmap no_image;
	public static String BASE_URL = "http://homezzang.co.kr/";

	public static String RPC_SERVER = BASE_URL+"api/api.php";
	public static String UPLOAD_SERVER = BASE_URL+"api/upload.php";

	public static int getIdentifier(Context context, String base, int idx) {
		return context.getResources().getIdentifier(String.format(base+"_%d", idx), "id", context.getPackageName());
	}

	public static int getPixelFromDP(Context context, int dp) {
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}
	public static float getSystemPixelByDp(Context context){
		return context.getResources().getDisplayMetrics().density;
	}
	public static int getPixelByDp(Context context, double dp){
		return (int)(getSystemPixelByDp(context) * dp);
	}
	public static int getPixelBySp(Context context, int sp){
		return (int)context.getResources().getDisplayMetrics().scaledDensity * sp;
	}
	public static int getPixelByDimen(Context context, int res) {
		return (int)context.getResources().getDimensionPixelSize(res);
	}
}
