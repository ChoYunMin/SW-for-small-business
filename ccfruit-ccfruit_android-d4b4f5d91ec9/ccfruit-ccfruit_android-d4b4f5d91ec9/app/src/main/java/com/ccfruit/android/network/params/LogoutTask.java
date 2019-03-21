package com.ccfruit.android.network.params;

import com.ccfruit.android.network.NetworkTask;
import com.ccfruit.android.util.PhoneInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class LogoutTask extends NetworkTask.RequestType {

	public LogoutTask() {
		super("user_logout");
	}

}
