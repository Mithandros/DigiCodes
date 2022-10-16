package com.maksof.linkapp;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NotificationIntentService extends JobIntentService {


    public static final int JOB_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";
    int NOTIFICATION_ID = 123;
    static Context mContext;
    public NotificationIntentService() {
        super();
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationIntentService.class, JOB_ID, work);
        mContext = context;
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                if(PreferenceUtils.get("latestNotification",mContext)!=null)
                    processStartNotification();
            }

            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        super.onStartCommand(intent, flags, startId);
        return super.START_STICKY;
    }
    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {

        JSONArray notifications = null;
        try {
            notifications = new JSONArray(PreferenceUtils.get("latestNotification",mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Do something. For example, fetch fresh data from backend to create a rich notification?
        String channel_id= createNotificationChannel();
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id);
        String msg;
        try {
            msg= notifications.getJSONObject(0).getString("notification");
        } catch (JSONException e) {
            msg = "";
            e.printStackTrace();
        }
        if (!"".equals(msg) && msg != null) {
            builder.setContentTitle("Scheduled Notification")
                    .setContentTitle("RekorTech")
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorPrimaryDark))
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.splash_logo)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    NOTIFICATION_ID,
                    new Intent(this, mainPage.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pendingIntent);
            builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this));
            builder.build().flags =Notification.FLAG_INSISTENT;
            final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID,builder.build());
            PreferenceUtils.save(null, "latestNotification", mContext);
    }}
    private String createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Channel_id";
            CharSequence channelName = "Application_name";
            String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_HIGH;
            boolean channelEnableVibrate = true;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
            return channelId;
        } else {
            return null;
        }
    }
}
