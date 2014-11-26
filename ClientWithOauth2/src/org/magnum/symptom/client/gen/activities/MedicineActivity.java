package org.magnum.symptom.client.gen.activities;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.magnum.symptom.client.R;
import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;
import org.magnum.symptom.client.gen.entities.Recipe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//TODO important: addMedicine
//TODO important: deleteMedicine
//TODO important: on saved instance
//TODO important: compare if the medicine string is already on the list.
//Then say that this medicine is already assigned to this patient.

//TODO optional: Let doc write medicine description, example take every 8 hours and so on, and show it in another list when 
//the medicine is clicked.
//TODO optional: Let doc see ALL recipes he has given to this particular patient.

public class MedicineActivity extends Activity {

	@InjectView(R.id.MedToAddEditText)
	protected EditText medToAddText;
	
	@InjectView(R.id.instructionTextView)
	protected TextView instructionTextView;
	
	@InjectView(R.id.AddMedButton)
	protected Button addMedButton;
	
	@InjectView(R.id.DeleteMedButton)
	protected Button deleteMedButton;
	
	@InjectView(R.id.DoneButton)
	protected Button doneButton;
	
	@InjectView(R.id.docMedList)
	protected ListView medListView;
	
	private static ArrayList<String> medNameList = new ArrayList<String>();
	private ArrayAdapter<String> listAdapter;
	private ArrayList<Long> medIdList = new ArrayList<Long>();
	
	private Long _patId;
	private UserSvcApi _svc = UserSvc.getOrShowLogin(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine);
		
		ButterKnife.inject(this);
		
		listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,medNameList);
		medListView.setAdapter(listAdapter);
		
		_patId = getIntent().getLongExtra("patientId", Long.MAX_VALUE);
		if(_patId != Long.MAX_VALUE)
		{
			
		}
		else
		{
			addMedButton.setVisibility(View.INVISIBLE);
			deleteMedButton.setVisibility(View.INVISIBLE);
		}
		
		getMedicineList();
	}

	@OnClick(R.id.AddMedButton)
	public void AddMedButton()
	{
		instructionTextView.setText("To add a medicine write it down here and press Done");
		doneButton.setVisibility(View.VISIBLE);
		instructionTextView.setVisibility(View.VISIBLE);
		medToAddText.setVisibility(View.VISIBLE);
	}
	@OnClick(R.id.DeleteMedButton)
	public void DeleteMedButton()
	{
		instructionTextView.setText("To delete a medicine select it. When finished deleting press Done");
		doneButton.setVisibility(View.VISIBLE);
		instructionTextView.setVisibility(View.VISIBLE);
		medToAddText.setVisibility(View.INVISIBLE);
		medToAddText.setText("");
	}
	@OnClick(R.id.DoneButton)
	public void DoneButton()
	{
		doneButton.setVisibility(View.INVISIBLE);
		instructionTextView.setVisibility(View.INVISIBLE);
		medToAddText.setVisibility(View.INVISIBLE);
		medToAddText.setText("");
	}

	private void getMedicineList()
	{
		medListView.setVisibility(0);
		medNameList.clear();
		medIdList.clear();
		
		if (_svc != null)
		{
			CallableTask.invoke(new Callable<Recipe>(){
			
				@Override
				public Recipe call() throws Exception {
					if(_patId != Long.MAX_VALUE)
						return _svc.getPatRecipesByPatIdAndCurrentDoc(_patId);
					else //TODO change this to current patient logged in.
						return _svc.getPatRecipesByPatIdAndCurrentDoc(_patId);
				}
				
			}, new TaskCallback<Recipe>(){
				
				@Override
				public void success(Recipe recipe) {
					
					medListView.setVisibility(1);
					//I only receive the last receipt, so doctor knows what he made his patient take the last time.
					for(int i = 0; i < recipe.getMedicines().size(); i++)
					{
						medNameList.add(recipe.getMedicines().get(i).getMedicine());
						medIdList.add(recipe.getMedicines().get(i).getId());
					}
					
					listAdapter.notifyDataSetChanged(); 
				}
	
				@Override
				public void error(Exception e) {
					Log.e(PatientActivity.class.getName(), "Error getting the patient record.", e);
					
					Toast.makeText(
							MedicineActivity.this,
							"Failed Getting patient record",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
