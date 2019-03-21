package com.ccfruit.android.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
	public static int getInt(JSONObject obj, String key) {
		try {
			return obj.getInt(key);
		} catch(JSONException e) {
			return 0;
		}
	}

	public static long getLong(JSONObject obj, String key) {
		try {
			return obj.getLong(key);
		} catch(JSONException e) {
			return 0;
		}
	}

	public static String getString(JSONArray obj, int key) {
		try {
			return obj.getString(key);
		} catch(JSONException e) {
			return "";
		}
	}
	public static String getString(JSONObject obj, String key) {
		try {
			return obj.getString(key);
		} catch(JSONException e) {
			return "";
		}
	}
}
