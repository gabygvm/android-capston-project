package org.magnum.symptom.client.gen.activities;

import java.util.concurrent.Callable;

import org.magnum.symptom.client.R;
import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * 
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 * 
 */
public class LoginScreenActivity extends Activity {

	@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;

	@InjectView(R.id.server)
	protected EditText server_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		ButterKnife.inject(this);
	}

	@OnClick(R.id.loginButton)
	public void login() {
		String user = userName_.getText().toString();
		String pass = password_.getText().toString();
		String server = "https://192.168.56.1:8443";//server_.getText().toString();

		final UserSvcApi svc = UserSvc.init(server, user, pass);
		
		
		CallableTask.invoke(new Callable<String>(){
			
			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub
				return svc.getUserRole();
			}
			
		}, new TaskCallback<String>(){
			
			@Override
			public void success(String result) {
				// OAuth 2.0 grant was successful and we
				// can talk to the server, open up the video listing

				if(result.equals("ROLE_PATIENT"))
				{
					startActivity(new Intent(
						LoginScreenActivity.this,
						PatientActivity.class));
				}else if (result.equals("ROLE_DOCTOR"))
				{
					startActivity(new Intent(
							LoginScreenActivity.this,
							DoctorActivity.class));
				}
			}

			@Override
			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);
				
				Toast.makeText(
						LoginScreenActivity.this,
						"Login failed, check your Internet connection and credentials.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
