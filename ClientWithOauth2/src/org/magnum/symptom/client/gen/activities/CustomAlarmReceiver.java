package org.magnum.symptom.client.gen.activities;
import org.magnum.symptom.client.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CustomAlarmReceiver extends BroadcastReceiver {
 
	private final int REQUEST_CODE = 2120;
    
	@Override
    public void onReceive(Context context, Intent intent) {

		int alarmId = intent.getIntExtra("alarmId", 0);
		
		if(alarmId != 0)
		{
			alarmId = alarmId - REQUEST_CODE;
			Toast.makeText(context, "Intent Detected number" + String.valueOf(alarmId), Toast.LENGTH_LONG).show();
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
	
			Notification.Builder mBuilder = new Notification.Builder(context)
					.setSmallIcon(R.drawable.heart_beat)
					.setContentTitle("Symptom Manager")
					.setContentText("Check In Time. Go!");
	
			Intent notificationIntent = new Intent(context, CheckInActivity.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
	
			PendingIntent intents = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);
			mBuilder.setContentIntent(intents);
			notificationManager.notify(0, mBuilder.build());
		}
	}
}

