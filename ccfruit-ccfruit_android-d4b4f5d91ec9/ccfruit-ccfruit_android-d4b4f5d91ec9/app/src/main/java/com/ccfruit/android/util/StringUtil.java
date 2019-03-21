package com.ccfruit.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.location.Address;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StringUtil {
	// byte[] to hex
	public static String byteArrayToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	public static byte[] getSHA1(byte[] input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return md.digest(input);
		} catch(NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA1 Algorithm not found", e);
		}
	}

	public static String password(byte[] input) {
		byte[] digest = null;

		digest = getSHA1(input); // 순수 sha1 hash 값
		digest = getSHA1(digest); // mysql password()는 한번 더 해쉬를 한 후 *를 덧붙임

		StringBuilder sb = new StringBuilder(1+digest.length);
		sb.append("*");
		//sb.append(ByteUtils.toHexString(digest).toUpperCase());
		sb.append(byteArrayToHex(digest).toUpperCase());
		return sb.toString();

	}

	public static String getPassword(String input) {
		if (input == null) {
			return null;
		}
		return password(input.getBytes());
	}

	public static String getAddressLine1(Address addr) {
		String str = "";
		if(addr.getAdminArea() != null) {
			str += addr.getAdminArea();
		}
		if(addr.getLocality() != null) {
			str += " "+addr.getLocality();
		}
		if(addr.getSubLocality() != null) {
			str += " "+addr.getSubLocality();
		}
		if(addr.getThoroughfare() != null) {
			str += " "+addr.getThoroughfare();
		}

		return str.trim();
	}

	public static String getStripTags(String html) {
		html = html.replaceAll("\\s*?\\<\\s*?br\\s*?/?\\>\\s*?","\n");
		html = html.replaceAll("\\s*?\\</?.*?\\>\\s*?","");
		return html;
	}

	public static String mysqlOldPassword(byte[] password) {
		int[] result = new int[2];
		int nr = 1345345333;
		int add = 7;
		int nr2 = 0x12345671;
		int tmp;

		int i;
		for (i = 0; i < password.length; i++) {
			if (password[i] == ' ' || password[i] == '\t')
				continue;

			tmp = (int) password[i];
			nr ^= (((nr & 63) + add) * tmp) + (nr << 8);
			nr2 += (nr2 << 8) ^ nr;
			add += tmp;
		}

		result[0] = nr & ((1 << 31) - 1);
		int val = ((1 << 31) - 1);
		result[1] = nr2 & val;
		String hash = String.format("%08x%08x",result[0],result[1]);
		return hash.toLowerCase();
	}

	public static void setGradeText(TextView view, int grade) {
		Context context = view.getContext();

		int color = 0xFFFFFFFF;
		try {
			color = view.getResources().getColor(context.getResources().getIdentifier(String.format("txt_grade_%02d", grade), "color", context.getPackageName()));
		} catch(Resources.NotFoundException e) { }

		view.setText("R"+grade);
		view.setTextColor(color);
	}

	public static boolean isEmail(String email) {
		if(email.matches(".+?\\@.+?\\..+?"))
			return true;
		return false;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public static boolean isBoolean(String s) {
		if(s.toLowerCase(Locale.US).equals("true"))
			return true;
		else if(s.toLowerCase(Locale.US).equals("false"))
			return true;
		
	    return false;
	}
	
	public static String getURLDecode(String str) {
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static Date parseDate(long date) {
		return parseDate(String.valueOf(date));
	}	
	
	@SuppressWarnings("deprecation")
	public static Date parseDate(String date) {
		if(date.length() != 14) {
			return null;
		}
		
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		int hour = Integer.parseInt(date.substring(8, 10));
		int minute = Integer.parseInt(date.substring(10, 12));
		int second = Integer.parseInt(date.substring(12, 14));
		
		return new Date(year-1900, month-1, day, hour, minute, second);
	}
	
	public static Date getNow() {
		return Calendar.getInstance().getTime();
	}

	public static String getDatePath() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
		return format.format(getNow());
	}
	public static Long getDatePath(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
		return Long.parseLong(format.format(date));
	}
	
	public static String getDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
		return format.format(date);
	}
	
	public static String md5(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] buffer = md.digest(data.getBytes());
			
			StringBuffer sb = new StringBuffer(); 
			for(int i = 0 ; i < buffer.length ; i++){
				sb.append(Integer.toString((buffer[i]&0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getMoneyString(double val) {
		return getMoneyString(""+val);
	}
	public static String getMoneyString(long val) {
		return getMoneyString(""+val);
	}
	public static String getMoneyString(String str){
		String[] strs = str.split("\\.");
		
		str = strs[0];
		boolean signed = false;
		if(str.startsWith("-")) {
			signed = true;
			str = str.substring(1);
		}
		int commaCount = str.length() / 3;
		int remainder = str.length() % 3;
		if(remainder == 0){
			commaCount --;
		}

		char[] strParsed = new char[str.length() + commaCount];
		for(int i = str.length() - 1 , count = 1, z = strParsed.length - 1; z >= 0; z--, count++){

			if(count % 4 == 0 && count != 0){
				strParsed[z] = ',';
			}else{
				strParsed[z] = str.charAt(i);
				i--;
			}
		}

		if(strs.length == 2) {
			str = strs[1];
			commaCount = str.length() / 3;
			remainder = str.length() % 3;
			if(remainder == 0){
				commaCount --;
			}
			
			char[] pointParsed = new char[str.length() + commaCount];
			for(int i = 0 , c = 0; i < str.length(); c++){

				if(c % 3 == 0 && c != 0){
					pointParsed[c] = ',';
				}else{
					pointParsed[c] = str.charAt(i);
					i++;
				}
			}
			
			if(signed)
				return "-"+new String(new String(strParsed)+"."+new String(pointParsed));
			else 
				return new String(new String(strParsed)+"."+new String(pointParsed));
		} else {
			if(signed)
				return "-"+new String(strParsed);
			else 
				return new String(strParsed);
		}
	}
	
	public static String getDiffStyleDate(Date date) {
		String[] periods = new String[] {"초", "분", "시간", "일", "주", "달", "년"};
		double[] lengths = new double[] {60, 60, 24, 7, 4.35, 12};
		
		long now = Calendar.getInstance().getTime().getTime();
		long target = date.getTime();
		
		int difference = 0;
		 
	    // is it future date or past date
	    if(now > target) {    
	        difference = (int)((now - target)/1000);
	    } else {
	        difference = (int)((target - now)/1000);
	    }
		 
	    int i=0;
	    for(i=0; difference >= lengths[i] && i < lengths.length-1; i++) {
	        difference /= lengths[i];
	    }
		 
		return difference+periods[i];
	}
	
	public static String getFacebookStyleDate(Date date) {
		String[] periods = new String[] {"초", "분", "시간", "일", "주", "달", "년"};
		double[] lengths = new double[] {60, 60, 24, 7, 4.35, 12};
		
		long now = Calendar.getInstance().getTime().getTime();
		long target = date.getTime();
		
		int difference = 0;
		String tense = "";
		 
	    // is it future date or past date
	    if(now > target) {    
	        difference = (int)((now - target)/1000);
	        tense = "전";
	 
	    } else {
	        difference = (int)((target - now)/1000);
	        tense = "이후";
	    }
		 
	    int i=0;
	    for(i=0; difference >= lengths[i] && i < lengths.length-1; i++) {
	        difference /= lengths[i];
	    }
		 
		return difference+periods[i]+" "+tense;
	}
	
	public static String getOnlineDate(Date date) {
		if(date == null)
			return "접속중이지 않음";
		
		String[] periods = new String[] {"초", "분", "시간", "일", "주", "개월", "년"};
		double[] lengths = new double[] {60, 60, 24, 7, 4.35, 12};
		
		long now = Calendar.getInstance().getTime().getTime();
		long target = date.getTime();
		
		int difference = 0;
		 
	    // is it future date or past date
	    if(now > target) {    
	        difference = (int)((now - target)/1000);
	    }
		 
	    int i=0;
	    for(i=0; difference >= lengths[i] && i < lengths.length-1; i++) {
	        difference /= lengths[i];
	    }
		 
		return difference+periods[i]+" 째 접속중";
	}
	
	public static int getJSONInteger(JSONObject data, String key, int def) {
		try {
			return data.getInt(key);
		} catch(JSONException e) {
			return def;
		}
	}
	
	public static long getJSONLong(JSONObject data, String key, long def) {
		try {
			return data.getLong(key);
		} catch(JSONException e) {
			return def;
		}
	}
}
