package org.magnum.symptom.client;

import org.magnum.videoup.client.R;
import org.magnum.videoup.client.R.id;
import org.magnum.videoup.client.R.layout;
import org.magnum.videoup.client.R.menu;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class DoctorPatientsActivity extends Activity {

	@InjectView(R.id.userName)
	protected EditText _patientName;

	@InjectView(R.id.password)
	protected EditText _patientLastName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_patients);
	
		ButterKnife.inject(this);
	}

	@OnClick(R.id.searchByNameButton)
	public void searchByNameAndLastName() {
		
		String patientsName = _patientName.getText().toString();
		String patientsLastName = _patientLastName.getText().toString();;
		
		final UserSvcApi svc = UserSvc.getOrShowLogin(this);
		if (svc != null)
		{
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctor_patients, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
