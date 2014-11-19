package org.magnum.symptom.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.magnum.videoup.client.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DoctorPatientsActivity extends Activity {

	@InjectView(R.id.searchView1)
	protected SearchView _patientNameSV;// = (SearchView) findViewById(R.id.searchView1);
	//private EditText _patientName;

	@InjectView(R.id.searchView2)
	protected SearchView _patientLastNameSV;// = (SearchView) findViewById(R.id.searchView2);
	//private EditText _patientLastName;

	private static ArrayList<String> _patientsAllNameList = new ArrayList<String>();
	private UserSvcApi _svc = UserSvc.getOrShowLogin(this);
	private ListView _patientsListView;
	private ArrayAdapter<String> _patientListAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor_patients);
		 //final SearchView _patientNameSV = (SearchView) findViewById(R.id.searchView1);
		 //final SearchView _patientLastNameSV = (SearchView) findViewById(R.id.searchView2);

		_patientsListView = (ListView) findViewById(R.id.listViewDoctorPatients);
		_patientListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,_patientsAllNameList);
		_patientsListView.setAdapter(_patientListAdapter);
		
		ButterKnife.inject(this);
		
		//Button searchButton = (Button)findViewById(R.id.searchByNameButton); 
		//searchButton.setOnClickListener(new OnClickListener() {
		//@Override
		//public void onClick(View v) {
				
		
	}

	@OnClick(R.id.searchByNameButton)
	public void searchByName() {
		
		//_patientName.setText(_patientNameSV.getQuery());
		//_patientLastName.setText(_patientLastNameSV.getQuery());
		
		CallableTask.invoke(new Callable<List<Patient>>(){
			
			@Override
			public List<Patient> call() throws Exception {
			
			//	String patientsName = (String) _patientNameSV.getQuery();//_patientName.getText().toString();
			//	String patientsLastName = (String) _patientLastNameSV.getQuery();//_patientLastName.getText().toString();
				
				if (_svc != null)
				{
					return _svc.getPatientsByDoctorWithNameAndLastName("Name03", "LastName03");
				}
				else
					return null;
			}
			
		}, new TaskCallback<List<Patient>>(){
			
			@Override
			public void success(List<Patient> list) {
	
				_patientsListView.setVisibility(1);
				for(int i = 0; i < list.size(); i++)
				{
					_patientsAllNameList.add(list.get(i).getName() + " " + list.get(i).getLastName());
				}
				_patientListAdapter.notifyDataSetChanged();
			}
			@Override
			public void error(Exception e) {
				Log.e(PatientActivity.class.getName(), "Error getting the doctor's Patient List", e);
				
				Toast.makeText(
						DoctorPatientsActivity.this,
						"Failed Getting doctor's Patient List",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
		
	/*public void searchByNameAndLastName() {
		
		final UserSvcApi svc = UserSvc.getOrShowLogin(this);
		String patientsName = _patientName.getText().toString();
		String patientsLastName = _patientLastName.getText().toString();
		
		if (svc != null)
		{
			CallableTask.invoke(new Callable<List<Patient>>(){
				
				@Override
				public List<Patient> call() throws Exception {
					String patientsName = _patientName.getText().toString();
					String patientsLastName = _patientLastName.getText().toString();
					
					return svc.getPatientsByDoctorWithNameAndLastName(patientsName, patientsLastName);
				}
				
			}, new TaskCallback<List<Patient>>(){
				
				@Override
				public void success(List<Patient> list) {
					//Paint here the info.
		//			_patientsListView.setVisibility(1);

					for(int i = 0; i < list.size(); i++)
					{
						_patientsAllNameList.add(list.get(i).getName() + " " + list.get(i).getLastName());
					}
		//			_patientListAdapter.notifyDataSetChanged();
				}
	
				@Override
				public void error(Exception e) {
					Log.e(PatientActivity.class.getName(), "Error getting the doctor's Patient List", e);
					
					Toast.makeText(
							DoctorPatientsActivity.this,
							"Failed Getting doctor's Patient List",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}*/
	
	/*(R.id.listViewDoctorPatients)
	public void selectSpecificPatient()
	{
		Toast.makeText(getBaseContext(), "Patient selection", Toast.LENGTH_LONG).show();
	}*/
	
	
	
	
/*	public static class DoctorPatientsList extends ListFragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(  
				     inflater.getContext(), R.layout.activity_doctor_patients,  
				     _patientsList);  
				   setListAdapter(adapter);
			return super.onCreateView(inflater, container, savedInstanceState);
		}
	}
	*/	
	/*
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
	}*/
}








