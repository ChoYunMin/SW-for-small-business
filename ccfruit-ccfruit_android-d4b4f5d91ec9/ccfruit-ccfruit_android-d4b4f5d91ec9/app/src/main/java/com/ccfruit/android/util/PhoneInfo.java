package com.ccfruit.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PhoneInfo {
	protected static Context mContext;
	
	private static PhoneInfo mPhoneInfo = null;
	
	protected String mDeviceId;
	protected String mLineNumber;
	
	public static String PREF_NAME = "refreshclub";
	
	public static void refresh(Context context) {
		mPhoneInfo = new PhoneInfo(context);
	}
	public static PhoneInfo getInstance() {
		return mPhoneInfo;
	}
	
	private PhoneInfo(Context context) {
		mContext = context;
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		 
		// Device Code
		try {
			mDeviceId = tm.getDeviceId() + "|" + tm.getSimSerialNumber() + "|" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
			mDeviceId = getMd5(mDeviceId);
		} catch(Exception e) {
			e.printStackTrace();
			mDeviceId = "None";
		}
		
		// Line Number
		mLineNumber = tm.getLine1Number();
		if (mLineNumber == null || mLineNumber.length() <= 0)
			mLineNumber = null;
	}

	public void setLastLocation(Location loc) {
		mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putFloat("lat", (float)loc.getLatitude()).commit();
		mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putFloat("lng", (float)loc.getLongitude()).commit();
	}
	public float getLastLat() {
		return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getFloat("lat", 0);
	}
	public float getLastLng() {
		return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getFloat("lng", 0);
	}
	
	public void setPushCode(String regId) {
		Log.e("GCM Code", "regID = "+regId);
		
		mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putString("push_code", regId).commit();
	}
	public String getPushCode() {
		return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("push_code", "");
	}

	public void setADID(String adid) {
		Log.e("adid", adid);
		mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putString("adid", adid).commit();
	}
	public String getADID() {
		return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString("adid", "");
	}

	/**
	 * @return	Returns string value of device code encrypted by MD5
	 * */
	public String getDeviceCode() {
		return mDeviceId;
	}
	
	/**
	 * @return	Returns string value of phone Number
	 * */
	public String getPhoneNumber() {
		return mLineNumber;
	}

	/**
	 * @return	Returns string of OS information(ex. 4.0.0)
	 * */
	public String getOS() {
		String result = "Android " + Build.VERSION.RELEASE;
		return result;
	}
	/**
	 * Returns OS version (Integer value)
	 * @return		Returns OS version with integer value. 
	 * 
	 * */
	public int getOSInt(){
		return Build.VERSION.SDK_INT;
	}
	
	/**
	 * @return	Returns string of device type(ex. NexusS)
	 * */
	public String getDeviceType() {
		String result = Build.MODEL;
		return result;
	}
	
	/**
	 * Return package name
	 * @return		Returns package name
	 * */
	public String getPackageName(){
		return mContext.getPackageName();
	}
	
	/**
	 * Returns app version (like 1.0.2)
	 * @return			Returns app version. If app version is not found, 
	 * return "Unknown" string instead.  
	 * */
	public int getAppVersion() {
		String packageName = null;
		int appVersion = 0;
		try {
			PackageInfo _package;
			packageName = mContext.getPackageName();
			_package = mContext.getPackageManager().getPackageInfo(packageName, 0);
			appVersion = _package.versionCode;
		} catch (NameNotFoundException e1) {
			appVersion = 0;
		}
		return appVersion;
	}
	public String getAppVersionName() {
		String packageName = null;
		String appVersion = "0";
		try {
			PackageInfo _package;
			packageName = mContext.getPackageName();
			_package = mContext.getPackageManager().getPackageInfo(packageName, 0);
			appVersion = _package.versionName;
		} catch (NameNotFoundException e1) {
			appVersion = "0";
		}
		return appVersion;
	}
	
	public boolean isGalaxySeries(){
		if(getDeviceType().contains("SHW-M")){
			return true;
		}
		return false;
	}

	/**
	 * Determine whether this is Google Nexus-S
	 * @return			Return true if this is Nexus-S, false if it's not 
	 * */
	public boolean isNexusS(){
		if(getDeviceType().contains("Nexus S")){
			return true;
		}
		return false;
	}

	/**
	 * Determine whether this is Vega Racer
	 * @return			Return true if this is Vega-Racer, false if it's not 
	 * */
	public boolean isVegaRacer(){
		if(getDeviceType().contains("IM-A770")){
			return true;
		}
		return false;
	}
	/**
	 * Determine whether this is LG Optimus 3D
	 * @return			Return true if this is LG Optimus 3D, false if it's not 
	 * */
	public boolean isOptimus3D(){
		if(getDeviceType().contains("LG-SU760")){
			return true;
		}
		return false;
	}

	/**
	 * @return	Returns MD5 encrypted string 
	 * */
	private String getMd5(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(s.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String md5 = number.toString(16);
			while (md5.length() < 32)
				md5 = "0" + md5;
			return md5;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

}
