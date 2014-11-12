package org.magnum.symptom.client;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import org.magnum.symptom.client.reminder.ReminderActivity;
import org.magnum.videoup.client.R;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PatientActivity extends Activity {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private ImageView _imageView;
	String mCurrentPhotoPath;
	Bitmap imageBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient);
		
		_imageView = (ImageView) findViewById(R.id.imageView1);
		
		Button checkInLaunch = (Button)findViewById(R.id.checkInButton); 
		checkInLaunch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				Intent checkInIntent = new Intent(PatientActivity.this, CheckInActivity.class);
				startActivity(checkInIntent);
			}
		});
		
		Button reminderButton = (Button)findViewById(R.id.alarmsButton); 
		reminderButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				Intent remiderIntent = new Intent(PatientActivity.this, ReminderActivity.class);
				startActivity(remiderIntent);
			}
		});
		
		Button editImageButton = (Button)findViewById(R.id.changePicButton);
		editImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dispatchTakePictureIntent();
			}
		});
		
		getPatientInfo();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        imageBitmap = (Bitmap) extras.get("data");
	        
	        _imageView.setImageBitmap(imageBitmap);
	    }
	}

	
	private void getPatientInfo()
	{
		final UserSvcApi svc = UserSvc.getOrShowLogin(this);
		
		if (svc != null)
		{
			CallableTask.invoke(new Callable<Patient>(){
			
				@Override
				public Patient call() throws Exception {
					// TODO Auto-generated method stub
					return svc.getPatientInfo();
				}
				
			}, new TaskCallback<Patient>(){
				
				@Override
				public void success(Patient patient) {
					//Paint here the info.
					TextView text = (TextView) findViewById(R.id.NameTxtV);
					text.setText("Name: " + patient.getName());
					
					text = (TextView) findViewById(R.id.LastNameTxtV);
					text.setText("Last Name: " + patient.getLastName());
					
					text = (TextView) findViewById(R.id.GenderTxtV);
					if(patient.getIsFemale() == true)
						text.setText("Gender: " + "Female");
					else
						text.setText("Gender: " + "Male");
					
					text = (TextView) findViewById(R.id.BirthdayTxtV);
					text.setText("Birthdate: " + patient.getBirthDate());
					
					text = (TextView) findViewById(R.id.MedicalRecordNameTxtV);
					text.setText("Medical Record: " + Long.toString(patient.getId()));
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
