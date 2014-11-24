/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.symptom.client.gen;

import org.magnum.symptom.client.gen.activities.LoginScreenActivity;
import org.magnum.symptom.client.gen.oauth.SecuredRestBuilder;
import org.magnum.symptom.client.gen.unsafe.EasyHttpClient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;

public class UserSvc {

	public static final String CLIENT_ID = "userAndroid";

	private static UserSvcApi _userSvc;

	public static synchronized UserSvcApi getOrShowLogin(Context ctx) {
		if (_userSvc != null) {
			return _userSvc;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized UserSvcApi init(String server, String user,
			String pass) {

		_userSvc = new SecuredRestBuilder()
				.setLoginEndpoint(server + UserSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(UserSvcApi.class);

		return _userSvc;
	}
}
