package org.magnum.symptom.reminder;

import java.util.Calendar;

import org.magnum.symptom.client.CheckInActivity;
import org.magnum.videoup.client.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class ReminderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder);		
		
		Button editImageButton = (Button)findViewById(R.id.setTimeButton);
		editImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SetReminder();
			}
		});	
	}
	
	private void SetReminder()
	{
		Intent intentAlarm = new Intent(this, AlarmReceiver.class);
		AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
		
		// Set the alarm to start at what the time picker says.
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
		calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		
		// setRepeating() lets you specify a precise custom interval--in this case,
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	}

}
