package org.magnum.symptom.client.gen.activities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import org.magnum.symptom.client.R;
import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;
import org.magnum.symptom.client.gen.entities.Patient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//TODO important: go to checkIns
//TODO important: go to medicines
//TODO important: on saved instance.
//TODO important: save picture in memory.


//TODO optional: Save picture data in database... is this too much data?
//TODO optional: when logged in as a doctor, get patient's picture from database.



public class PatientActivity extends Activity {
	
	@InjectView(R.id.NameTxtV)
	protected TextView nameTextView;
	
	@InjectView(R.id.LastNameTxtV)
	protected TextView lastNameTextView;
	
	@InjectView(R.id.GenderTxtV)
	protected TextView genderTextView;
	
	@InjectView(R.id.BirthdayTxtV)
	protected TextView birthdayTextView;
	
	@InjectView(R.id.MedicalRecordNameTxtV)
	protected TextView MedRecordTextView;
	
	@InjectView(R.id.checkInButton)
	protected Button checkInLaunch;
	
	@InjectView(R.id.alarmsButton)
	protected Button reminderButton; 
	
	@InjectView(R.id.changePicButton)
	protected Button editImageButton;
	
	@InjectView(R.id.recipeButton)
	protected Button recipeButton;
	
	@InjectView(R.id.imageView1)
	protected ImageView imageView;
	
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	final UserSvcApi svc = UserSvc.getOrShowLogin(this);
	
	private String mCurrentPhotoPath;
	private Bitmap imageBitmap;
	
	private Boolean _DoctorLoggedIn;
	private Long _patId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_patient);	
		ButterKnife.inject(this);
		
		_DoctorLoggedIn = getIntent().getBooleanExtra("isDoctor", false);
		if(_DoctorLoggedIn)
		{
			_patId = getIntent().getLongExtra("patId", Long.MAX_VALUE);
			checkInLaunch.setText("CheckIns");
			recipeButton.setText("Current Medicines");
			reminderButton.setVisibility(View.GONE);
			editImageButton.setVisibility(View.GONE);
			imageView.setVisibility(View.GONE);
		}
		else
			_patId = Long.MAX_VALUE;
		
		getPatientInfo();
	}
	
	@OnClick(R.id.checkInButton)
	public void onClickCheckInButton(){
		Intent checkInIntent = new Intent(PatientActivity.this, CheckInActivity.class);
		startActivity(checkInIntent);
	}
	@OnClick(R.id.alarmsButton)
	public void onClickAlarmsButton(){
		Intent remiderIntent = new Intent(PatientActivity.this, ReminderActivity.class);
		startActivity(remiderIntent);
	}
	@OnClick(R.id.changePicButton)
	public void onClickChangePicButton(){
		dispatchTakePictureIntent();
	}
	@OnClick(R.id.recipeButton)
	public void onClickRecipePicButton(){
		//TODO: add case when patient is the one logged in, so it goes to the last recipe active.
		Intent recipeIntent;
		if(_DoctorLoggedIn)
		{
			recipeIntent = new Intent(PatientActivity.this, MedicineActivity.class);
			recipeIntent.putExtra("patientId", _patId);
		}
		else
			recipeIntent = new Intent(PatientActivity.this, MedicineActivity.class);
		startActivity(recipeIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        imageBitmap = (Bitmap) extras.get("data");
	        
	        imageView.setImageBitmap(imageBitmap);
	    }
	}
	
	private void getPatientInfo()
	{
		if (svc != null)
		{
			CallableTask.invoke(new Callable<Patient>(){
			
				@Override
				public Patient call() throws Exception {
					if(_DoctorLoggedIn)
						return svc.getPatientById(_patId);
					else
						return svc.getPatientInfo();
				}
				
			}, new TaskCallback<Patient>(){
				
				@Override
				public void success(Patient patient) {
					//Paint here the info.
					//TextView text = (TextView) findViewById(R.id.NameTxtV);
					nameTextView.setText("Name: " + patient.getName());//text.setText("Name: " + patient.getName());
					
					//text = (TextView) findViewById(R.id.LastNameTxtV);
					lastNameTextView.setText("Last Name: " + patient.getLastName()); //text.setText("Last Name: " + patient.getLastName());
					
					//text = (TextView) findViewById(R.id.GenderTxtV);
					if(patient.getIsFemale() == true)
						genderTextView.setText("Gender: " + "Female"); //text.setText("Gender: " + "Female");
					else
						genderTextView.setText("Gender: " + "Male");//text.setText("Gender: " + "Male");
					
					//text = (TextView) findViewById(R.id.BirthdayTxtV);
					birthdayTextView.setText("Birthdate: " + patient.getBirthDate());//text.setText("Birthdate: " + patient.getBirthDate());
					
					//text = (TextView) findViewById(R.id.MedicalRecordNameTxtV);
					MedRecordTextView.setText("Medical Record: " + Long.toString(patient.getId()));//text.setText("Medical Record: " + Long.toString(patient.getId()));
				}
	
				@Override
				public void error(Exception e) {
					Log.e(PatientActivity.class.getName(), "Error getting the user info.", e);
					
					Toast.makeText(
							PatientActivity.this,
							"Failed Getting user info",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	
	private void dispatchTakePictureIntent() 
	{
	    Intent intentTakePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    
	    if (intentTakePic.resolveActivity(getPackageManager()) != null)
	    {
	    	// Create the File where the photo should go
	     /*   File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } 
	        catch (IOException ex) {
	            // Error occurred while creating the File
	        }
	        if (photoFile != null) {
	        	//intentTakePic.putExtra(MediaStore.EXTRA_OUTPUT,
	        */     //       Uri.fromFile(photoFile));
	        	startActivityForResult(intentTakePic, REQUEST_IMAGE_CAPTURE);
	        //}
	    }
	}
	private File createImageFile() throws IOException
	{
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patient, menu);
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
