package com.ccfruit.android.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

public class JSONClient {
	public static final String ENCODING = "utf8";

	private static HttpURLConnection initServer(String server) throws IOException {
		URL url = new URL(server);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		
		conn.setDefaultUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection","keep-alive");
		conn.setRequestProperty("content-type","application/x-www-form-urlencoded");
		try {
			String str = "";
			List<HttpCookie> cookie = NetworkTask.cookies.getCookieStore().get(new URI(server));
			for(HttpCookie c: cookie) {
				str += c.toString()+";";
			}
			conn.setRequestProperty("cookie", str);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		conn.setConnectTimeout(5000);
		
		return conn;
	}
	
	
	public static JSONObject call(String server, HashMap<String, Object> params) {
		String param = "";
		try {
			for(String key: params.keySet()) {
				param += key+"="+URLEncoder.encode(params.get(key).toString(), ENCODING)+"&";
			}
			if(param.length() > 0) param = param.substring(0, param.length()-1);

			// Connecting
			HttpURLConnection conn = initServer(server);
			conn.connect();
			
			Log.i("param", param);
			
			// Sending
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.write(param.getBytes(ENCODING));
			dos.flush();
			
			try {
				NetworkTask.cookies.put(new URI(server), conn.getHeaderFields());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			// Error
			InputStream source = conn.getInputStream();
			BufferedReader source_result = null;
			if(source == null) {
				Log.e("Error", "HTTP Error!");
				source_result = new BufferedReader(new InputStreamReader(conn.getErrorStream(), ENCODING));
				dos.close();
				return null;
			} else {
				source_result = new BufferedReader(new InputStreamReader(source, ENCODING));
			}

			// Result
			StringBuilder result = new StringBuilder(); 
			char[] buff = new char[1024]; int len = 0;
			while((len=source_result.read(buff)) != -1) {
				result.append(new String(buff, 0, len));
			}
			
			int maxLogSize = 1000;
			for(int i = 0; i <= result.length() / maxLogSize; i++) {
			    int start = i * maxLogSize;
			    int end = (i+1) * maxLogSize;
			    end = end > result.length() ? result.length() : end;
				Log.i("result", result.substring(start, end));
			}
			
			dos.close();
			
			return new JSONObject(new String(result.toString().getBytes()));
		} catch(ConnectException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
