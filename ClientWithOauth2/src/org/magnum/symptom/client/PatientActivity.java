package org.magnum.symptom.client;

import org.magnum.symptom.reminder.ReminderActivity;
import org.magnum.videoup.client.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PatientActivity extends Activity {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private ImageView _imageView;
	
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
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        
	        _imageView.setImageBitmap(imageBitmap);
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
