package com.ioannisnicos.ethiomoviesstore.notification;

import android.app.PendingIntent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ioannisnicos.ethiomoviesstore.MainActivity;
import com.ioannisnicos.ethiomoviesstore.ManageSubscriptions;
import com.ioannisnicos.ethiomoviesstore.R;


import java.util.Map;

import androidx.core.app.NotificationCompat;

public class  MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFireBaseMessagingService.class.getSimpleName();

    enum PUSH_NOTIFICATION_SOURCE{
        CONSOLE, API_WITHOUT_NOTIFICATION, API_WITH_NOTIFICATION, UNKNOWN_SOURCE;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i(TAG,"New token: "+s);
        //Making an API call - Thread, Volley, okHttp, Retrofit
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        PUSH_NOTIFICATION_SOURCE notificationSource = getNotificationSource(remoteMessage);

        Log.i(TAG,"Remote Message received from : "+notificationSource);

        switch (notificationSource){
            case CONSOLE:
//                ((NotfApplication)getApplication()).triggerNotificationWithBackStackImage(
//                        NotificationActivity.class,
//                        getString(R.string.MOVIES_CHANNEL_ID),
//                        remoteMessage.getData().get("title"),
//                        remoteMessage.getNotification().getBody(),
//                        null,null,null,
//                        NotificationCompat.PRIORITY_HIGH,
//                        false,
//                        getResources().getInteger(R.integer.notificationId),
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
                break;
            case API_WITH_NOTIFICATION:

//                ((NotfApplication)getApplication()).triggerNotificationWithBackStackImage(
//                        SettingActivity.class,
//                        getString(R.string.MOVIES_CHANNEL_ID),
//                        remoteMessage.getNotification().getTitle(),
//                        remoteMessage.getNotification().getBody(),
//                        remoteMessage.getData().get("big_text"),
//                        remoteMessage.getData().get("large_img"),
//                        remoteMessage.getData().get("big_img"),
//                        NotificationCompat.PRIORITY_HIGH,
//                        false,
//                        getResources().getInteger(R.integer.notificationId),
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
                break;
            case API_WITHOUT_NOTIFICATION:
                if(remoteMessage.getData().get("type").equals("subscrition")){
                ((NotfApplication)getApplication()).triggerNotificationWithBackStackImageSubscription(
                        ManageSubscriptions.class,
                        getString(R.string.ACCOUNT_CHANNEL_ID),
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("body"),
                        remoteMessage.getData().get("body2"),
                        remoteMessage.getData().get(""),
                        NotificationCompat.PRIORITY_HIGH,
                        false,
                        (int) System.currentTimeMillis(),
                        //getResources().getInteger(R.integer.notificationId),
                        PendingIntent.FLAG_UPDATE_CURRENT
                ); }
                if(remoteMessage.getData().get("type").equals("rent")){
                    ((NotfApplication)getApplication()).triggerNotificationWithBackStackImageRent(
                            MainActivity.class,
                            getString(R.string.ACCOUNT_CHANNEL_ID),
                            remoteMessage.getData().get("title"),
                            remoteMessage.getData().get("body"),
                            remoteMessage.getData().get("body2"),
                            remoteMessage.getData().get("large_img"),
                            remoteMessage.getData().get("big_img"),
                            NotificationCompat.PRIORITY_HIGH,
                            false,
                            (int) System.currentTimeMillis(),
                            //getResources().getInteger(R.integer.notificationId),
                            PendingIntent.FLAG_UPDATE_CURRENT
                    ); }
                break;
            case UNKNOWN_SOURCE:
                Log.i(TAG,"Since it's unknown source, don't want to do anything");
                break;

            default:break;
        }
    }

    private PUSH_NOTIFICATION_SOURCE getNotificationSource(RemoteMessage remoteMessage){

        PUSH_NOTIFICATION_SOURCE notificationSource;

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data =  remoteMessage.getData();

        if (notification !=null && data !=null){
                if(data.size()==0){
                   notificationSource =  PUSH_NOTIFICATION_SOURCE.CONSOLE;
                }else {
                   notificationSource =  PUSH_NOTIFICATION_SOURCE.API_WITH_NOTIFICATION;
                }
        }else if (remoteMessage.getData()!=null){
               notificationSource =  PUSH_NOTIFICATION_SOURCE.API_WITHOUT_NOTIFICATION;
        } else {
               notificationSource = PUSH_NOTIFICATION_SOURCE.UNKNOWN_SOURCE;
        }
        return notificationSource;
    }
}