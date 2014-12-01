package org.magnum.symptom.client.gen.activities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;
import org.magnum.symptom.client.gen.entities.Answer;
import org.magnum.symptom.client.gen.entities.AnswerType;
import org.magnum.symptom.client.gen.entities.AnswerValue;
import org.magnum.symptom.client.gen.entities.CheckIn;
import org.magnum.symptom.client.gen.entities.Recipe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class CheckInActivity extends Activity {

	final int CHECK_IN_REQUEST_CODE = 354981;
	
	private final int WELL_CONTROLLED_ID = 1; 
	private final int MODERATE_ID = 2; 
	private final int SEVERE_ID = 3;
	private final int NO_ID = 4; 
	private final int SOME_ID = 5; 
	private final int I_CANT_EAT_ID = 6;
	private final int BUTTON_ID = 7;
	private final int YES_BASE_ID = 100;
	private final int NO_BASE_ID = 200;
	
	
	private ScrollView scrollView; 
	private LinearLayout linearLayout;
	private TextView firstQuestionTxt;
	private CheckBox answer1;
	private CheckBox answer2;
	private CheckBox answer3;
	
	private List<CheckBox> yesAnswers;
	private List<CheckBox> noAnswers;
	private List<String> dateAndTime;
	private int medAnswerCounter;
	
	private TextView lastQuestionTxt;
	private Button checkInButton;
	private CheckBox answer4;
	private CheckBox answer5;
	private CheckBox answer6;
	
	private Recipe lastRecipe;
	private List<Answer> answerList;
	private UserSvcApi svc = UserSvc.getOrShowLogin(this);
	private long docId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_check_in);
		
		docId = getIntent().getLongExtra("docId", 0);
	
		scrollView = new ScrollView(this);
		scrollView.setBackgroundColor(Color.parseColor("#ADDFFF"));
		
		linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);
		
		firstQuestionTxt = new TextView(this);		
		firstQuestionTxt.setText("How bad is your mouth pain/sore throat?");
		firstQuestionTxt.setTextSize((float) 20.0);
		linearLayout.addView(firstQuestionTxt);
		
		answer1 = new CheckBox(this);
		answer1.setText("Well Controlled");
		answer1.setId(WELL_CONTROLLED_ID);
		
		answer2 = new CheckBox(this);
		answer2.setText("Moderate");
		answer2.setId(MODERATE_ID);
		
		answer3 = new CheckBox(this);
		answer3.setText("Severe");
		answer3.setId(SEVERE_ID);
		
		linearLayout.addView(answer1);
		linearLayout.addView(answer2);
		linearLayout.addView(answer3);
		
		lastQuestionTxt = new TextView(this);
		lastQuestionTxt.setText("Does your pain stop you from eating/drinking?");
		lastQuestionTxt.setTextSize((float) 20.0);
		
		answer4 = new CheckBox(this);
		answer4.setText("No");
		answer4.setId(NO_ID);
		
		answer5 = new CheckBox(this);
		answer5.setText("Some");
		answer5.setId(SOME_ID);
		
		answer6 = new CheckBox(this);
		answer6.setText("I can't eat");
		answer6.setId(I_CANT_EAT_ID);
		
		checkInButton = new Button(this);
		checkInButton.setText("CHECK IN!");
		checkInButton.setId(BUTTON_ID);
		
		dateAndTime = new ArrayList<String>();
		dateAndTime.clear();
		
		answerList = new ArrayList<Answer>();
		
		GetMedsFromDoctor();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == CHECK_IN_REQUEST_CODE) {
	        if(resultCode == RESULT_OK){
	            String time = data.getStringExtra("time");
	            String date = data.getStringExtra("date");
	            int arrayPos = data.getIntExtra("arrayPos", 0xFF);
	            
	            if(arrayPos != 0xFF)
	            	dateAndTime.set(arrayPos, date + " at " + time);
	        }else{
				Toast.makeText(
						CheckInActivity.this,
						"Time Was Not Set, please select Yes again and set Time And Date by clicking the button",
						Toast.LENGTH_LONG).show();
		    }
	    }
	}
	
	@OnClick(BUTTON_ID)
	public void CheckInButtonClicked()
	{
		answerList.clear();	
		
		if(answer1.isChecked()){
			answerList.add(new Answer(AnswerType.PAIN_SORE, AnswerValue.WELL_CONTROLLED, null, null));
		}
		else if(answer2.isChecked()){
			answerList.add(new Answer(AnswerType.PAIN_SORE, AnswerValue.MODERATE, null, null));
		}
		else if(answer3.isChecked())
			answerList.add(new Answer(AnswerType.PAIN_SORE, AnswerValue.SEVERE, null, null));
		else{
			toastAnswerAllQuestions();
			return;
		}
		
		if(lastRecipe.getMedicines().size() == 1){
			
			if(yesAnswers.get(0).isChecked())
				answerList.add(new Answer(AnswerType.PAIN_MED_GENERIC, AnswerValue.YES, dateAndTime.get(0), lastRecipe.getMedicines().get(0)));
			else if(noAnswers.get(0).isChecked())
				answerList.add(new Answer(AnswerType.PAIN_MED_GENERIC, AnswerValue.NO, dateAndTime.get(0), lastRecipe.getMedicines().get(0)));
			else{
				toastAnswerAllQuestions();
				return;
			}
		}
		else{
			for(int i = 0; i < lastRecipe.getMedicines().size(); i++){
				if(yesAnswers.get(i).isChecked()){
					answerList.add(new Answer(AnswerType.PAIN_MED_SPECIFIC, AnswerValue.YES, 
							dateAndTime.get(i), lastRecipe.getMedicines().get(i)));
				}
				else if(noAnswers.get(i).isChecked()){
					answerList.add(new Answer(AnswerType.PAIN_MED_SPECIFIC, AnswerValue.NO, 
							null, lastRecipe.getMedicines().get(i)));
				}else{
					toastAnswerAllQuestions();
					return;
				}
			}
		}
		
		if(answer4.isChecked()){
			answerList.add(new Answer(AnswerType.PAIN_STOP_EATING_DRINKING, AnswerValue.NO, null, null));
		}
		else if(answer5.isChecked()){
			answerList.add(new Answer(AnswerType.PAIN_STOP_EATING_DRINKING, AnswerValue.SOME, null, null));
		}
		else if(answer6.isChecked())
			answerList.add(new Answer(AnswerType.PAIN_STOP_EATING_DRINKING, AnswerValue.I_CANT_EAT, null, null));
		else{
			toastAnswerAllQuestions();
			return;
		}
		
		saveCheckIn();
	}
	
	private void toastAnswerAllQuestions(){
		Toast.makeText(
				CheckInActivity.this,
				"Please answer all the questions",
				Toast.LENGTH_SHORT).show();
	}
	private void saveCheckIn(){
		CallableTask.invoke(new Callable<CheckIn>(){
			@Override
			public CheckIn call() throws Exception {		
				if (svc != null)
					return svc.AddPatientCheckIn(lastRecipe.getPatRecord().getId(), answerList);
				else
					return null;
			}		
		}, new TaskCallback<CheckIn>(){
			
			@Override
			public void success(CheckIn checkIn) {
				Intent returnIntent = new Intent();
				setResult(RESULT_OK, returnIntent);
				finish();
			}
			@Override
			public void error(Exception e) {
				Toast.makeText(
						CheckInActivity.this,
						"Failed saving check in",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@OnCheckedChanged(WELL_CONTROLLED_ID)
	public void onCheckedWellControlled(boolean checked){
		if(checked)
		{
			answer2.setChecked(false);
			answer3.setChecked(false);
		}
	}
	@OnCheckedChanged(MODERATE_ID)
	public void onCheckedModerate(boolean checked){
		if(checked)
		{
			answer1.setChecked(false);
			answer3.setChecked(false);
		}
	}
	@OnCheckedChanged(SEVERE_ID)
	public void onCheckedSevere(boolean checked){
		if(checked)
		{
			answer1.setChecked(false);
			answer2.setChecked(false);
		}
	}
	@OnCheckedChanged(NO_ID)
	public void onCheckedNo(boolean checked){
		if(checked)
		{
			answer5.setChecked(false);
			answer6.setChecked(false);
		}
	}
	@OnCheckedChanged(SOME_ID)
	public void onCheckedSome(boolean checked){
		if(checked)
		{
			answer4.setChecked(false);
			answer6.setChecked(false);
		}
	}
	@OnCheckedChanged(I_CANT_EAT_ID)
	public void onCheckedICantEat(boolean checked){
		if(checked)
		{
			answer4.setChecked(false);
			answer5.setChecked(false);
		}
	}
	
	private void GetMedsFromDoctor(){
		
		yesAnswers = new ArrayList<CheckBox>();
		noAnswers = new ArrayList<CheckBox>();
		
		medAnswerCounter = 0;
		yesAnswers.clear();
		noAnswers.clear();
		
		CallableTask.invoke(new Callable<Recipe>(){
			@Override
			public Recipe call() throws Exception {		
				if (svc != null)
					return svc.getCurrentPatientLastRecipeByDoctorId(docId);
				else
					return null;
			}		
		}, new TaskCallback<Recipe>(){
			
			@Override
			public void success(Recipe recipe) {
				
				TextView medQuestionTxt;
				
				lastRecipe = recipe;
				
				if(recipe.getMedicines().size() == 1)
				{
					medQuestionTxt = new TextView(CheckInActivity.this);
					medQuestionTxt.setText("Did you take your pain medication?");
					medQuestionTxt.setTextSize((float) 20.0);
					linearLayout.addView(medQuestionTxt);
					AddYesOrNoAnswer();
				}else{
					for(int i = 0; i < recipe.getMedicines().size(); i++){
						AddMedsQuestion(recipe, i);
						AddYesOrNoAnswer();
					}
				}
				
				linearLayout.addView(lastQuestionTxt);
				linearLayout.addView(answer4);
				linearLayout.addView(answer5);
				linearLayout.addView(answer6);
				linearLayout.addView(checkInButton);
				
				CheckInActivity.this.setContentView(scrollView);
				ButterKnife.inject(CheckInActivity.this);
			}
			@Override
			public void error(Exception e) {
				Toast.makeText(
						CheckInActivity.this,
						"Failed Getting Patient's medicines",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void AddYesOrNoAnswer(){
		
		yesAnswers.add(new CheckBox(this));
		yesAnswers.get(medAnswerCounter).setText("Yes");
		yesAnswers.get(medAnswerCounter).setId(YES_BASE_ID + medAnswerCounter);
		yesAnswers.get(medAnswerCounter).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					noAnswers.get(buttonView.getId() - YES_BASE_ID).setChecked(false);
					
					Intent tempIntent = new Intent(CheckInActivity.this, DateAndTimeOfMed.class);
					tempIntent.putExtra("arrayPos", buttonView.getId()-YES_BASE_ID);
					startActivityForResult(tempIntent, CHECK_IN_REQUEST_CODE);
				}
			}
		});
		
		linearLayout.addView(yesAnswers.get(medAnswerCounter));
		
		noAnswers.add(new CheckBox(this));
		noAnswers.get(medAnswerCounter).setText("No");
		noAnswers.get(medAnswerCounter).setId(NO_BASE_ID + medAnswerCounter);
		noAnswers.get(medAnswerCounter).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
					yesAnswers.get(buttonView.getId() - NO_BASE_ID).setChecked(false);	
			}
		});
		
		linearLayout.addView(noAnswers.get(medAnswerCounter));
		medAnswerCounter++;
		dateAndTime.add("");
	}
	private void AddMedsQuestion(Recipe recipe, int medCount){
		TextView medQuestionTxt = new TextView(CheckInActivity.this);
		medQuestionTxt.setText("Did you take your " + recipe.getMedicines().get(medCount).getMedicine() + "?");
		medQuestionTxt.setTextSize((float) 20.0);
		linearLayout.addView(medQuestionTxt);
	}
}
