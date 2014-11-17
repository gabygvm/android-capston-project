package org.magnum.symptom.client;

import java.util.concurrent.Callable;

import org.magnum.videoup.client.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DoctorActivity extends Activity {	
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private ImageView _imageView;
	private Bitmap _imageBitmap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doctor);
		
		_imageView = (ImageView) findViewById(R.id.imageView1);
		
		ButterKnife.inject(this);
		
		getDoctorInfo();
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
	        _imageBitmap = (Bitmap) extras.get("data");    
	        _imageView.setImageBitmap(_imageBitmap);
	    }
	}

	@OnClick(R.id.patientsButton)
	public void PatientsButton() {
		startActivity(new Intent(
				DoctorActivity.this,
				DoctorPatientsActivity.class));
	}
	
	@OnClick(R.id.changePicButton)
	public void ChangePicButton() {
		dispatchTakePictureIntent();
	}
	
	private void getDoctorInfo()
	{
		final UserSvcApi svc = UserSvc.getOrShowLogin(this);
		
		if (svc != null)
		{
			CallableTask.invoke(new Callable<Doctor>(){
			
				@Override
				public Doctor call() throws Exception {
					// TODO Auto-generated method stub
					return svc.getDoctorInfo();
				}
				
			}, new TaskCallback<Doctor>(){
				
				@Override
				public void success(Doctor doctor) {
					//Paint here the info.
					TextView text = (TextView) findViewById(R.id.NameTxtV);
					text.setText("Name: " + doctor.getName());
					
					TextView text1 = (TextView) findViewById(R.id.LastNameTxtV);
					text1.setText("Last Name: " + doctor.getLastName());
					
					TextView text2 = (TextView) findViewById(R.id.GenderTxtV);
					if(doctor.getIsFemale() == true)
						text2.setText("Gender: " + "Female");
					else
						text2.setText("Gender: " + "Male");
					
					TextView text3 = (TextView) findViewById(R.id.BirthdayTxtV);
					text3.setText("Birthdate: " + doctor.getBirthDate());
				}
	
				@Override
				public void error(Exception e) {
					Log.e(PatientActivity.class.getName(), "Error getting the user info.", e);
					
					Toast.makeText(
							DoctorActivity.this,
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
	       	startActivityForResult(intentTakePic, REQUEST_IMAGE_CAPTURE);
	    }
	}
}
