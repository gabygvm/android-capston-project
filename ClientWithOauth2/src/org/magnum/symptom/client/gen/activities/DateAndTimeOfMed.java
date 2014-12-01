package org.magnum.symptom.client.gen.activities;

import org.magnum.symptom.client.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TimePicker;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DateAndTimeOfMed extends Activity {

	@InjectView(R.id.medDatePicker)
	protected DatePicker medDatePicker;
	
	@InjectView(R.id.medTimePicker)
	protected TimePicker medTimePicker;
	
	private int arrayPos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_date_and_time_of_med);
		
		ButterKnife.inject(this);
		
		arrayPos = getIntent().getIntExtra("arrayPos", 0xFF);
	}

	@OnClick(R.id.setDateAndTimeButton)
	public void setDateAndTimeButton()
	{
		Intent returnIntent = new Intent();
		String date;
		String time;
		
		date = String.valueOf(medDatePicker.getDayOfMonth()) + "-" +
				String.valueOf(medDatePicker.getMonth() + 1) + "-" +
				String.valueOf(medDatePicker.getYear());
		
		time = String.valueOf(medTimePicker.getCurrentHour() + ":" +
				String.valueOf(medTimePicker.getCurrentMinute()));
		
		returnIntent.putExtra("date", date);
		returnIntent.putExtra("time", time);
		
		returnIntent.putExtra("arrayPos", arrayPos);
		setResult(RESULT_OK,returnIntent);
		finish();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.date_and_time_of_med, menu);
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
