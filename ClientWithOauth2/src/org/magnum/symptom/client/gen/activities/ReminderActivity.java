package org.magnum.symptom.client.gen.activities;

import java.util.Calendar;

import org.magnum.symptom.client.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class ReminderActivity extends Activity {

	private Calendar calendar;
	private TimePicker timePicker;
	private final int REQUEST_CODE = 2120;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder);		
		
		calendar = Calendar.getInstance();
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
		
		Button setTimeButton1 = (Button)findViewById(R.id.setTimeButton1);
		setTimeButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetReminder(1);
			}
		});
		Button setTimeButton2 = (Button)findViewById(R.id.setTimeButton2);
		setTimeButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetReminder(2);
			}
		});	
		Button setTimeButton3 = (Button)findViewById(R.id.setTimeButton3);
		setTimeButton3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetReminder(3);
			}
		});	
		Button setTimeButton4 = (Button)findViewById(R.id.setTimeButton4);
		setTimeButton4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetReminder(4);
			}
		});	
	}
		
	private void SetReminder(int alarmId)
	{
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		Intent intent2 = new Intent();
		calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		intent2.putExtra("alarmId", (alarmId + REQUEST_CODE));
		intent2.setAction("org.magnum.symptom.client");
		intent2.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId,  intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pendingIntent);
		Toast.makeText(getApplicationContext(), "Reminder Set Number" + String.valueOf(alarmId), Toast.LENGTH_LONG).show();
	}
}
