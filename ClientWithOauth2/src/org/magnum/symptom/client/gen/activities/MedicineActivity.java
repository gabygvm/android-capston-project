package org.magnum.symptom.client.gen.activities;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import org.magnum.symptom.client.R;
import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;
import org.magnum.symptom.client.gen.entities.Medicine;
import org.magnum.symptom.client.gen.entities.Recipe;

import android.app.Activity;
import android.os.Bundle;
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
import butterknife.OnItemClick;

//TODO optional: Let doc write medicine description, example take every 8 hours and so on, and show it in another list when 
//the medicine is clicked.
//TODO optional: Let doc see ALL recipes he has given to this particular patient. 
//I can save several recipes from a doc to a patient, but im currently updating the last one not creating new ones...
//Let's see if i have the time

public class MedicineActivity extends Activity {

	@InjectView(R.id.MedToAddEditText)
	protected EditText medToAddEditText;
	
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
	private Boolean isDelete = false;
	private Boolean isAdd = false;
	
	private Recipe currentRecipe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine);
		
		ButterKnife.inject(this);
		
		listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1,medNameList);
		medListView.setAdapter(listAdapter);
		
		_patId = getIntent().getLongExtra("patientId", Long.MAX_VALUE);
		if(_patId == Long.MAX_VALUE)
		{
			addMedButton.setVisibility(View.INVISIBLE);
			deleteMedButton.setVisibility(View.INVISIBLE);
		}
		
		getMedicineList();
		if(savedInstanceState != null)
		{
			if(savedInstanceState.getBoolean("isAdd", false)){
				isAdd = true;
				isDelete = false;
				AddMedButtonView();
			}
			if(savedInstanceState.getBoolean("isDelete", false)){
				isAdd = false;
				isDelete = true;
				DeleteMedButtonView();
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("isDelete", isDelete);
		outState.putBoolean("isAdd", isAdd);
	};
	
		
	@OnClick(R.id.AddMedButton)
	public void AddMedButton()
	{
		isDelete = false;
		isAdd = true;
		AddMedButtonView();
	}
	@OnClick(R.id.DeleteMedButton)
	public void DeleteMedButton()
	{
		isDelete = true;
		isAdd = false;
		DeleteMedButtonView();
	}
	@OnClick(R.id.DoneButton)
	public void DoneButton()
	{		
		if(isAdd){
			AddMedicine(medToAddEditText.getText().toString());
		}
		isDelete = false;
		isAdd = false;
		doneButton.setVisibility(View.INVISIBLE);
		instructionTextView.setVisibility(View.INVISIBLE);
		medToAddEditText.setVisibility(View.INVISIBLE);
		medToAddEditText.setText("");
	}
	@OnItemClick(R.id.docMedList)
	public void onItemClick(int position)
	{	
		if(isDelete){
			DeleteMedicine(position);
		}
	}
	

	private void getMedicineList()
	{
		ClearList();
		currentRecipe = null;
		
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
					
					UpdateMedList(recipe); 
				}
	
				@Override
				public void error(Exception e) {
					MakeToast("Failed loading patient's recipe");
				}
			});
		}
	}

	private void AddMedicine(final String medName){
		if(medName.equals("")){
			MakeToast("Write a Medicine to add");
			return;
		}
		else{
			for(int med = 0; med < currentRecipe.getMedicines().size(); med++)
			{
				if(medName.toUpperCase().equals(currentRecipe.getMedicines().get(med).getMedicine()))
				{
					MakeToast("Medicine already in Recipe");
					return;
				}
			}
		}
			
		if(currentRecipe == null){
			MakeToast("No Recipe created for this patient");
			return;
		}
		
		if(_svc != null){
			CallableTask.invoke(new Callable<Recipe>(){
				
				@Override
				public Recipe call() throws Exception {
					currentRecipe.getMedicines().add(new Medicine(medName));
					return _svc.AddMedFromRecipe(currentRecipe);
				}
				
			}, new TaskCallback<Recipe>(){
				
				@Override
				public void success(Recipe recipe) {
					UpdateMedList(recipe); 
				}
	
				@Override
				public void error(Exception e) {
					MakeToast("Error adding medicine");
				}
			});
		}
	}
	private void DeleteMedicine(final int index){
		if(_svc != null){
			CallableTask.invoke(new Callable<Recipe>(){
				
				@Override
				public Recipe call() throws Exception {
					return _svc.DeleteMedFromRecipe(currentRecipe.getId(), medIdList.get(index));
				}
				
			}, new TaskCallback<Recipe>(){
				
				@Override
				public void success(Recipe recipe) {
					UpdateMedList(recipe); 
				}
	
				@Override
				public void error(Exception e) {
					MakeToast("Error deleting medicine");
				}
			});
		}
	}

	
	private void UpdateMedList(Recipe recipe){
		currentRecipe = recipe;
		ClearList();
		for(int i = 0; i < recipe.getMedicines().size(); i++)
		{
			medNameList.add(recipe.getMedicines().get(i).getMedicine());
			medIdList.add(recipe.getMedicines().get(i).getId());
		}
		medListView.setVisibility(1);
		listAdapter.notifyDataSetChanged();
	}
	private void ClearList() {
		medListView.setVisibility(0);
		medNameList.clear();
		medIdList.clear();
	}
	
	private void AddMedButtonView(){
		instructionTextView.setText("To add a medicine write it down here and press Done");
		doneButton.setVisibility(View.VISIBLE);
		instructionTextView.setVisibility(View.VISIBLE);
		medToAddEditText.setVisibility(View.VISIBLE);
	}
	private void DeleteMedButtonView(){
		instructionTextView.setText("To delete a medicine select it. When finished deleting press Done");
		doneButton.setVisibility(View.VISIBLE);
		instructionTextView.setVisibility(View.VISIBLE);
		medToAddEditText.setVisibility(View.INVISIBLE);
		medToAddEditText.setText("");
	}
	private void MakeToast(String message){
		Toast.makeText(	MedicineActivity.this, message, Toast.LENGTH_SHORT).show();
	}
}
