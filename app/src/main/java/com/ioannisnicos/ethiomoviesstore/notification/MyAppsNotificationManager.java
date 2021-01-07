
package com.ioannisnicos.ethiomoviesstore.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ioannisnicos.ethiomoviesstore.MainActivity;
import com.ioannisnicos.ethiomoviesstore.R;
import com.ioannisnicos.ethiomoviesstore.retrofit.ApiClient;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import static android.content.Context.NOTIFICATION_SERVICE;

class MyAppsNotificationManager {

    private Context context;

    private static MyAppsNotificationManager instance;
    private NotificationManagerCompat notificationManagerCompat;
    private NotificationManager notificationManager;

    private MyAppsNotificationManager(Context context){
        this.context = context;
        notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
    }

    public static MyAppsNotificationManager getInstance(Context context){
        if(instance==null){
            instance = new MyAppsNotificationManager(context);
        }
        return instance;
    }

    public void registerNotificationChannelChannel(String channelId, String channelName, String channelDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(channelDescription);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


    public void triggerNotificationWithBackStackImageSubscription(Class targetNotificationActivity, String channelId, String title, String text, String bigText, String larImg, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag){

        Intent intent = new Intent(context, targetNotificationActivity);
        //intent.putExtra("count", title);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        //
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId)

                .setSmallIcon(R.drawable.ic_ethiomovies)
                .setContentTitle("Hi, " + title)
                .setContentText( text )
                .setStyle(new NotificationCompat.BigTextStyle().bigText( text  ))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setChannelId(channelId)
                .setOngoing(false)
                .setAutoCancel(true);

              if(larImg==null)  notificationManagerCompat.notify((int) System.currentTimeMillis(),builder.build());
              else{
                    Glide.with(context)
                            .asBitmap()
                            .load(ApiClient.BASE_URL+larImg)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                    builder.setLargeIcon(resource);
                                    notificationManager.notify(notificationId,builder.build());
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
              }


    }


    public void triggerNotificationWithBackStackImageRent(Class targetNotificationActivity, String channelId, String title, String text, String bigText, String larImg, String bigImg, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag){

        Intent intent = new Intent(context, targetNotificationActivity);
        intent.putExtra("notification", "rent");
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(intent);
        //
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId)

                .setSmallIcon(R.drawable.ic_ethiomovies)
                .setContentTitle("Hi, " + title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setChannelId(channelId)
                .setOngoing(false)
                .setAutoCancel(true);

        //  notificationManagerCompat.notify((int) System.currentTimeMillis(),builder.build());
        final Bitmap[] b = {null};

        if(larImg==null) {
            Glide.with(context)
                    .asBitmap()
                    .load(bigImg)
                    .fitCenter()
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource).bigLargeIcon(b[0]));
                            notificationManagerCompat.notify(notificationId,builder.build());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else{
           //final Bitmap[] b = {null};
            Glide.with(context)
                    .asBitmap()
                    .load(ApiClient.BASE_URL+larImg)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            b[0] = resource;
                            builder.setLargeIcon(resource);
                            //notificationManager.notify(notificationId,builder.build());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }

        if(bigImg==null) notificationManagerCompat.notify((int) System.currentTimeMillis(),builder.build());
        else{
            Glide.with(context)
                    .asBitmap()
                    .load(bigImg)
                    .fitCenter()
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(resource).bigLargeIcon(b[0]));
                            notificationManagerCompat.notify(notificationId,builder.build());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
    }

    public void cancelNotification(int notificationId){
        notificationManager.cancel(notificationId);
    }

}
