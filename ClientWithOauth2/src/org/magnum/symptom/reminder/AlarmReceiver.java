package org.magnum.symptom.reminder;
import org.magnum.symptom.client.CheckInActivity;

import org.magnum.videoup.client.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.TaskStackBuilder;


public class AlarmReceiver extends BroadcastReceiver {
 
    @Override
    public void onReceive(Context context, Intent intent) {

    	NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
    	
    	Notification.Builder mBuilder = new Notification.Builder(context)
    				.setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Symptom Manager")
                    .setContentText("Check In Time. Go!");
    				
    	
    	Intent notificationIntent = new Intent(context, CheckInActivity.class);
    	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	
    	PendingIntent intents = PendingIntent.getActivity(context, 0, notificationIntent, 0);
    	mBuilder.setContentIntent(intents);
        notificationManager.notify(0, mBuilder.build());
    }
}

/*
 NotificationManager notificationManager = (NotificationManager) getBaseContext()
		                .getSystemService(Context.NOTIFICATION_SERVICE);
		    	
		    	Notification.Builder mBuilder = new Notification.Builder(getBaseContext())
		    				.setSmallIcon(R.drawable.ic_launcher)
		                    .setContentTitle("Symptom Manager")
		                    .setContentText("Check In Time. Go!");
		    				
		    	
		    	Intent notificationIntent = new Intent(getBaseContext(), CheckInActivity.class);
		    	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    	
		    	PendingIntent intents = PendingIntent.getActivity(getBaseContext(), 0, notificationIntent, 0);
		    	mBuilder.setContentIntent(intents);
		        notificationManager.notify(0, mBuilder.build());
 * */