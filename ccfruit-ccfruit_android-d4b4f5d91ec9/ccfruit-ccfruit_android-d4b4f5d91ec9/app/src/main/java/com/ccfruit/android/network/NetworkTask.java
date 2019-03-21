package com.ccfruit.android.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import com.ccfruit.android.Preference;
import com.ccfruit.android.util.PhoneInfo;
import com.ccfruit.android.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class NetworkTask {
	public static CookieManager cookies = new CookieManager();

	public static abstract class RequestType {
		private String name = "";
		protected HashMap<String, Object> params = new HashMap<String, Object>();
		
		protected RequestType(String name) {
            setApiName(name);
            params.put("device_code", PhoneInfo.getInstance().getDeviceCode());
		}
		
		public void setApiName(String name) {
			this.name = name;
		}
		public String getApiName() {
			return name;
		}
		public HashMap<String, Object> getParams() {
			return params;
		}
	}

	public static class RPCResult {
		private int id = 0;
		private int code = 0;
		private String msg = "";
		
		private int page = 0;
		private int pagesize = 0;
		
		private JSONObject data = null;
		
		public RPCResult(int id, JSONObject object) {
			try {
				this.id = id;
				code = object.getInt("code");
				msg = object.getString("msg");
				if(code == 0) {
					data = object.getJSONObject("result");

					page = StringUtil.getJSONInteger(object.getJSONObject("meta"), "page", 0);
					pagesize = StringUtil.getJSONInteger(object.getJSONObject("meta"), "pagesize", 0);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		public int getId() {
			return id;
		}
		public JSONObject getData() {
			return data;
		}
		public JSONObject getMeta() {
			JSONObject obj = new JSONObject();
			try {
				obj.put("msg", msg);
				obj.put("page", page);
				obj.put("pagesize", pagesize);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return obj;
		}
		public int size() {
			return data.length();
		}
		public int getCode() {
			return code;
		}
		public String getMessage() {
			return msg;
		}
	}
	public static interface OnRequestListener {
		public void onRequestEnd(RPCResult result);
	}
	
	protected Activity activity = null;
	protected OnRequestListener listener = null;
	public NetworkTask(Activity activity, OnRequestListener listener) {
		this.activity = activity;
		this.listener = listener;
	}

	public void send(RequestType params) {
		send(params, false);
	}
	public void send(RequestType params, boolean dialog) {
		send(0, params, dialog);
	}
	public void send(int id, RequestType params) {
		send(id, params, false);
	}
	public void send(int id, RequestType params, boolean dialog) {
		_send(id, params.getApiName(), params.getParams(), dialog);
	}
	
	private void _send(int id, String name, final HashMap<String, Object> data, boolean show) {
		ProgressDialog d = null;
		if(show) d = ProgressDialog.show(activity, "", "정보를 전송중입니다...", true, false);

		final ProgressDialog dialog = d;
		
		data.put("id", id);
		data.put("api_name", name);

		new Thread(new Runnable() {
			public void run() {
				JSONObject result = JSONClient.call(Preference.RPC_SERVER, data);

				if(dialog != null && dialog.isShowing()) {
					dialog.cancel();
				}
				
				if(listener != null) {
					if(result != null) {
						int id = 0;
						try {
							id = result.getInt("id");
						} catch(JSONException e) {
							id = 0;
						}
						
						final RPCResult rpc = new RPCResult(id, result);
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								listener.onRequestEnd(rpc);
							}
						});
					} else {
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								activity.finish();
								Toast.makeText(activity, "서버가 응답하지 않습니다.", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			}
		}).start();
	}
	
	public void sendImage(final int id, final String params, final HashMap<Integer, File> files) {
		final ProgressDialog dialog = new ProgressDialog(activity);
		dialog.setMessage("정보를 전송중입니다...");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setCancelable(false);
		dialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				for(int img_uid: files.keySet()) {
					dialog.setProgress(0);
					
					File file = files.get(img_uid);
					
					try {
						int len;
						byte[] buff = new byte[1024];
						
						// 서버 접속
						URL url = new URL(Preference.UPLOAD_SERVER+"?device_code="+PhoneInfo.getInstance().getDeviceCode()+"&"+params+"&idx="+img_uid);
						URLConnection conn = url.openConnection();
						
						// 기본 헤더
						String boundary = "+++++++++++++++++++++++++";
						conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
						conn.setRequestProperty("Connection", "keep-alive");
						conn.setDoInput(true);
						conn.setDoOutput(true);
						
						if(file.exists()) {
							// 전송파일 오픈
							FileInputStream in = new FileInputStream(file);
							
							dialog.setMax((int)file.length());
							long sended = 0;
							
							// Multipart FormData로 파일 전송
							DataOutputStream out = new DataOutputStream(conn.getOutputStream());
							out.writeBytes("--" + boundary + "\r\n");
							out.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\""+file.getName()+"\"\r\n");
							out.writeBytes("Content-Type: image/png\r\n");
							out.writeBytes("\r\n");
	
							// 파일 읽기
							while((len = in.read(buff)) > 0) {
								// ?�버???�어?�인 만큼 ?�송
								out.write(buff, 0, len);
								sended += len;
								dialog.setProgress((int)sended);
							}
							
							// 파일 전송
							out.writeBytes("\r\n--" + boundary + "--\r\n");
							
							// 서버 연결 마무리
							out.flush();
							out.close();
							
							// 전송파일 해제
							in.close();
						} else {
							Log.e("Error", "File Not Found");
							return;
						}
						
						String data = "";
						InputStream in = conn.getInputStream();
						while((len = in.read(buff)) > 0) {
							data += new String(buff, 0, len);
						}
						in.close();
						
						Log.e("log", " "+data);
						
						final String result = data;
						
						if(listener != null) {
							if(result != null && result.length() > 0) {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										try {
											listener.onRequestEnd(new RPCResult(id, new JSONObject(result)));
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
							} else {
								activity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										activity.finish();
										Toast.makeText(activity, "서버가 응답하지 않습니다.", Toast.LENGTH_LONG).show();
									}
								});
								break;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(dialog != null && dialog.isShowing()) {
					dialog.cancel();
				}
			}
		}).start();
	}
}
