package org.magnum.symptom.client.gen.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.magnum.symptom.client.R;
import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;
import org.magnum.symptom.client.gen.entities.Patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class DoctorPatientsActivity extends Activity {

	private final String BUNDLE_VIEW_NAME = "viewName";
	private final String BUNDLE_VIEW_LASTNAME = "viewLastName";	
	private final String BUNDLE_PATIENTS_IDS = "patientsIds";
	
	@InjectView(R.id.searchView1)
	protected SearchView patientNameSV;

	@InjectView(R.id.searchView2)
	protected SearchView patientLastNameSV;

	@InjectView(R.id.listViewDoctorPatients)
	protected ListView patientsListView;
	
	
	private static ArrayList<String> patientsAllNameList = new ArrayList<String>();
	private ArrayAdapter<String> patientListAdapter;
	
	private ArrayList<Long> patientIdList = new ArrayList<Long>();
	private UserSvcApi svc = UserSvc.getOrShowLogin(this);
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_doctor_patients);
		ButterKnife.inject(this);
		
		patientListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,patientsAllNameList);
		patientsListView.setAdapter(patientListAdapter);
		
		if(savedInstanceState != null)
		{
			long[] ids = savedInstanceState.getLongArray(BUNDLE_PATIENTS_IDS);
			
			patientIdList.clear();
			for(int i = 0; i < ids.length; i++)
				patientIdList.add(ids[i]);
			
			patientNameSV.setQuery(savedInstanceState.getString(BUNDLE_VIEW_NAME), false);
			patientLastNameSV.setQuery(savedInstanceState.getString(BUNDLE_VIEW_LASTNAME), false);
			patientsListView.setVisibility(1);
			patientListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		long[] ids = new long[patientIdList.size()];
		
		for(int i = 0; i < patientIdList.size(); i++)
			ids[i] = patientIdList.get(i);
		
		outState.putLongArray(BUNDLE_PATIENTS_IDS, ids);
		outState.putString(BUNDLE_VIEW_NAME, patientNameSV.getQuery().toString());
		outState.putString(BUNDLE_VIEW_LASTNAME, patientLastNameSV.getQuery().toString());
	};
	
	@OnClick(R.id.searchByNameButton)
	public void SearchByName() {
		
		patientsListView.setVisibility(0);
		patientsAllNameList.clear();
		patientIdList.clear();
		
		CallableTask.invoke(new Callable<List<Patient>>(){
			
			@Override
			public List<Patient> call() throws Exception {
			
				String patientsName = patientNameSV.getQuery().toString();
				String patientsLastName = patientLastNameSV.getQuery().toString();
				
				if (svc != null)
					return svc.getPatientsByDoctorWithNameAndLastName(patientsName, patientsLastName);
				else
					return null;
			}		
		}, new TaskCallback<List<Patient>>(){
			
			@Override
			public void success(List<Patient> list) {
	
				patientsListView.setVisibility(0);
				patientsAllNameList.clear();
				patientIdList.clear();
				patientsListView.setVisibility(1);
				for(int i = 0; i < list.size(); i++)
				{
					patientIdList.add(list.get(i).getId());
					patientsAllNameList.add(list.get(i).getName() + " " + list.get(i).getLastName());
				}
				patientListAdapter.notifyDataSetChanged();
			}
			@Override
			public void error(Exception e) {
				//Log.e(PatientActivity.class.getName(), "Error getting the doctor's Patient List", e);
				patientsListView.setVisibility(0);
				patientsAllNameList.clear();
				patientIdList.clear();
				patientListAdapter.notifyDataSetChanged();
				Toast.makeText(
						DoctorPatientsActivity.this,
						"Failed Getting doctor's Patient List",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@OnItemClick(R.id.listViewDoctorPatients)
	public void onItemClick(int position)
	{
		Intent intent = new Intent(DoctorPatientsActivity.this,	PatientActivity.class);
		intent.putExtra("patId", patientIdList.get(position));
		intent.putExtra("isDoctor", true);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
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








