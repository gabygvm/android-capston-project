package org.magnum.symptom.client.gen.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.magnum.symptom.client.R;
import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;
import org.magnum.symptom.client.gen.entities.CheckIn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

public class DoctorPatientCheckInList extends Activity {

	@InjectView(R.id.patientsCheckInListView)
	protected ListView checkInListView;
	
	private static ArrayList<String> checkInNames = new ArrayList<String>();
	private ArrayAdapter<String> checkIntListAdapter;
	private ArrayList<Long> checkInIdList = new ArrayList<Long>();
	private Long patientId;
	
	private final UserSvcApi svc = UserSvc.getOrShowLogin(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_patient_check_in_list);
		ButterKnife.inject(this);
		
		checkIntListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,checkInNames);
		checkInListView.setAdapter(checkIntListAdapter);
		
		patientId = getIntent().getLongExtra("patientId", Long.MAX_VALUE);
		
		getCheckInList();
	}

	@OnItemClick(R.id.patientsCheckInListView)
	public void onItemClick(int position)
	{
		Intent intent = new Intent(this, DoctorPatientSpecificCheckIn.class);
		intent.putExtra("checkInId", checkInIdList.get(position));
		startActivity(intent);
	}
	
	private void getCheckInList()
	{
		checkInListView.setVisibility(0);
		checkInNames.clear();
		checkInIdList.clear();
		
		if (svc != null)
		{
			CallableTask.invoke(new Callable<List<CheckIn>>(){
			
				@Override
				public List<CheckIn> call() throws Exception {
					
					return svc.getPatCheckInsForCurrentDoc(patientId);
				}
				
			}, new TaskCallback<List<CheckIn>>(){
				
				@Override
				public void success(List<CheckIn> checkIns) {
					
					checkInListView.setVisibility(1);
					for(int i = 0; i < checkIns.size(); i++)
					{
						checkInIdList.add(checkIns.get(i).getId());
						checkInNames.add("CheckIn " + String.valueOf(i));
					}
					checkIntListAdapter.notifyDataSetChanged();
				}
	
				@Override
				public void error(Exception e) {				
					Toast.makeText(
							DoctorPatientCheckInList.this,
							"Failed Getting patient's checkIns",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doctor_patient_check_in_list, menu);
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
