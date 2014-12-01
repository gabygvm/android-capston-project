package org.magnum.symptom.client.gen.activities;

import java.util.concurrent.Callable;

import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;
import org.magnum.symptom.client.gen.entities.Answer;
import org.magnum.symptom.client.gen.entities.AnswerValue;
import org.magnum.symptom.client.gen.entities.CheckIn;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;

public class DoctorPatientSpecificCheckIn extends Activity {
	
	private ScrollView scrollView; 
	private LinearLayout linearLayout;
	private TextView firstQuestionTxt;

	
	private TextView lastQuestionTxt;

	private UserSvcApi svc = UserSvc.getOrShowLogin(this);
	private long checkInId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		checkInId = getIntent().getLongExtra("checkInId", 0);
	
		scrollView = new ScrollView(this);
		scrollView.setBackgroundColor(Color.parseColor("#ADDFFF"));
		
		linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(linearLayout);
		
		firstQuestionTxt = new TextView(this);		
		firstQuestionTxt.setText("How bad is your mouth pain/sore throat?");
		firstQuestionTxt.setTextSize((float) 20.0);
		linearLayout.addView(firstQuestionTxt);
		
		lastQuestionTxt = new TextView(this);
		lastQuestionTxt.setText("Does your pain stop you from eating/drinking?");
		lastQuestionTxt.setTextSize((float) 20.0);

		GetMedsFromDoctor();
	}

	
	private void GetMedsFromDoctor(){
		
		CallableTask.invoke(new Callable<CheckIn>(){
			@Override
			public CheckIn call() throws Exception {		
				if (svc != null)
					return svc.getPatCheckInByCheckInId(checkInId);//TODO add the correct function here.
				else
					return null;
			}		
		}, new TaskCallback<CheckIn>(){
			
			@Override
			public void success(CheckIn checkIn) {
				
				for(int i = 0; i < checkIn.getAnswers().size(); i++)
				{
					switch(checkIn.getAnswers().get(i).getAnswerType()){
						case PAIN_SORE:
							TextView answerTxt = new TextView(DoctorPatientSpecificCheckIn.this);
							
							if(checkIn.getAnswers().get(i).getAnswerValue() == AnswerValue.WELL_CONTROLLED)
								answerTxt.setText("Well Controlled");
							else if (checkIn.getAnswers().get(i).getAnswerValue() == AnswerValue.MODERATE)
								answerTxt.setText("Moderate");
							else if (checkIn.getAnswers().get(i).getAnswerValue() == AnswerValue.SEVERE)
								answerTxt.setText("Severe");
							
							answerTxt.setTextSize((float) 18.0);
							linearLayout.addView(answerTxt);
						break;
						
						case PAIN_MED_GENERIC:
							AddMedQuestionAndAnswer(checkIn.getAnswers().get(i), true);
						break;
						
						case PAIN_MED_SPECIFIC:
							AddMedQuestionAndAnswer(checkIn.getAnswers().get(i), false);
						break;
						
						case PAIN_STOP_EATING_DRINKING:
							linearLayout.addView(lastQuestionTxt);
							TextView answerTxt2 = new TextView(DoctorPatientSpecificCheckIn.this);
							
							if(checkIn.getAnswers().get(i).getAnswerValue() == AnswerValue.NO)
								answerTxt2.setText("No");
							else if (checkIn.getAnswers().get(i).getAnswerValue() == AnswerValue.SOME)
								answerTxt2.setText("Moderate");
							else if (checkIn.getAnswers().get(i).getAnswerValue() == AnswerValue.I_CANT_EAT)
								answerTxt2.setText("I can't eat");
							
							answerTxt2.setTextSize((float) 18.0);
							linearLayout.addView(answerTxt2);
						break;
					}
				}
				
				DoctorPatientSpecificCheckIn.this.setContentView(scrollView);
				ButterKnife.inject(DoctorPatientSpecificCheckIn.this);
			}
			@Override
			public void error(Exception e) {
				Toast.makeText(
						DoctorPatientSpecificCheckIn.this,
						"Failed Getting Patient's medicines",
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	private void AddMedQuestionAndAnswer(Answer ans, Boolean generic){
		
		TextView medQuestionTxt = new TextView(DoctorPatientSpecificCheckIn.this);
		if(generic == true)
			medQuestionTxt.setText("Did you take your medication?");
		else
			medQuestionTxt.setText("Did you take your " + ans.getMedTaken().getMedicine() + "?");
		medQuestionTxt.setTextSize((float) 20.0);
		linearLayout.addView(medQuestionTxt);
		
		TextView answerTxt = new TextView(DoctorPatientSpecificCheckIn.this);
		
		if(ans.getAnswerValue() == AnswerValue.YES)
		{
			answerTxt.setText("Yes, medication taken the day " + ans.getDateAndTimeTaken());
		}else if(ans.getAnswerValue() == AnswerValue.NO)
			answerTxt.setText("No");
		answerTxt.setTextSize((float) 18.0);
		linearLayout.addView(answerTxt);
	}
}
