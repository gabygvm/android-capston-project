package org.magnum.symptom.client.gen.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.magnum.symptom.client.R;
import org.magnum.symptom.client.gen.CallableTask;
import org.magnum.symptom.client.gen.TaskCallback;
import org.magnum.symptom.client.gen.UserSvc;
import org.magnum.symptom.client.gen.UserSvcApi;
import org.magnum.symptom.client.gen.entities.Doctor;
import org.magnum.symptom.client.gen.entities.Patient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
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

//TODO important: go to medicines
//TODO important: on saved instance.
//TODO important: save picture in memory.


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
	
	
	private static final int REQUEST_IMAGE_CAPTURE = 200;
	private static final int CHECK_IN_REQ_CODE = 201;
	
	final UserSvcApi svc = UserSvc.getOrShowLogin(this);
	
	private String mCurrentPhotoPath;
	private Bitmap imageBitmap;
	
	private Boolean _DoctorLoggedIn;
	private Long _patId;
	private Long currentPatId;
	
	private List<Long> docsId;
	private int checkInCounter = 0;
	
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
		{
			recipeButton.setVisibility(View.INVISIBLE);
			_patId = Long.MAX_VALUE;
		}
		
		docsId = new ArrayList<Long>();
		getPatientInfo();
	}
	
	@OnClick(R.id.checkInButton)
	public void onClickCheckInButton(){
		if(_DoctorLoggedIn == false){
			checkInCounter = 0;
			getPatientDoctorsIdAndStartCheckingIn();
		}
		else{
			Intent intent = new Intent(PatientActivity.this, DoctorPatientCheckInList.class);
			intent.putExtra("patientId", _patId);
			startActivity(intent);
		}
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
	        
	        
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        byte[] byteArray = stream.toByteArray();
	        int length = byteArray.length;
	        String encodedImage = Base64.encodeToString(byteArray, Base64.URL_SAFE);
	        SavePatientPicture(encodedImage);
	    }
	    else if(requestCode == CHECK_IN_REQ_CODE){
	    	checkInCounter++;
	    	if(checkInCounter < docsId.size())
	    	{
	    		Intent checkInIntent = new Intent(PatientActivity.this, CheckInActivity.class);
	    		checkInIntent.putExtra("docId", docsId.get(checkInCounter));
	    		startActivityForResult(checkInIntent, CHECK_IN_REQ_CODE);
	    	}
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
					nameTextView.setText("Name: " + patient.getName());
					
					lastNameTextView.setText("Last Name: " + patient.getLastName());
					
					if(patient.getIsFemale() == true)
						genderTextView.setText("Gender: " + "Female");
					else
						genderTextView.setText("Gender: " + "Male");
					
					birthdayTextView.setText("Birthdate: " + patient.getBirthDate());
					
					MedRecordTextView.setText("Medical Record: " + Long.toString(patient.getId()));
					currentPatId = patient.getId();
					
					/*if(patient.getImage() != null){
						imageBitmap = BitmapFactory.decodeByteArray(patient.getImage() , 0, patient.getImage().length);
						imageView.setImageBitmap(imageBitmap);
					}*/
					/*if(patient.getImageBase64() != null){
						byte[] image = Base64.decode(patient.getImageBase64(), Base64.URL_SAFE);
						 int length = image.length;
						imageBitmap = (Bitmap)BitmapFactory.decodeByteArray(image, 0, image.length);
						imageView.setImageBitmap(imageBitmap);
						imageView.setVisibility(View.VISIBLE);
					}*/
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
	
	private void SavePatientPicture(final String photo){
		if (svc != null)
		{
			CallableTask.invoke(new Callable<Patient>(){
			
				@Override
				public Patient call() throws Exception {
					return svc.SavePatientPhoto(photo);
				}
				
			}, new TaskCallback<Patient>(){
				
				@Override
				public void success(Patient patient) {
					Toast.makeText(
							PatientActivity.this,
							"Photo Saved",
							Toast.LENGTH_SHORT).show();
				}
	
				@Override
				public void error(Exception e) {					
					Toast.makeText(
							PatientActivity.this,
							"Failed Saving Photo",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	private void getPatientDoctorsIdAndStartCheckingIn(){
		if (svc != null)
		{
			docsId.clear();
			CallableTask.invoke(new Callable<List<Doctor>>(){
			
				@Override
				public List<Doctor> call() throws Exception {
					return svc.getDoctorsFromPatientId(currentPatId);
				}
				
			}, new TaskCallback<List<Doctor>>(){
				
				@Override
				public void success(List<Doctor> doc) {
					if(doc.size() != 0)
					{
						Intent checkInIntent = new Intent(PatientActivity.this, CheckInActivity.class);
						
						for(int i = 0; i < doc.size(); i++)
							docsId.add(doc.get(i).getId());
						
						checkInIntent.putExtra("docId", docsId.get(0));
						
						startActivityForResult(checkInIntent, CHECK_IN_REQ_CODE);
					}else{
						Toast.makeText(
								PatientActivity.this,
								"No doctor found for this patient",
								Toast.LENGTH_SHORT).show();
					}
				}
	
				@Override
				public void error(Exception e) {
					Toast.makeText(
							PatientActivity.this,
							"Failed Getting patient's doctors",
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
