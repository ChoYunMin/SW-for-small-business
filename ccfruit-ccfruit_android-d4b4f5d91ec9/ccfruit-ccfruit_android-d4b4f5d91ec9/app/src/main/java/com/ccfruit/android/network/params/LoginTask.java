package com.ccfruit.android.network.params;

import com.ccfruit.android.network.NetworkTask;
import com.ccfruit.android.util.StringUtil;

public class LoginTask extends NetworkTask.RequestType {

	public LoginTask(String email, String passwd) {
		super("user_login");

		params.put("email", email);
		params.put("passwd", StringUtil.getPassword(passwd));
	}

}
