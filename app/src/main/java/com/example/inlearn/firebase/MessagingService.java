package com.example.inlearn.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.system.Os;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.inlearn.MainActivity;
import com.example.inlearn.NotificationActivity;
import com.example.inlearn.R;
import com.example.inlearn.utils.SharedPreferencedHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    SharedPreferencedHelper sharedPreferencedHelper;
    Context mContext;

    @Override
    public void onNewToken(String s) {
        sharedPreferencedHelper = new SharedPreferencedHelper(getApplicationContext());
        Log.e("TOKENs",s);
        sharedPreferencedHelper.saveSPString(sharedPreferencedHelper.FCM_TOKEN,s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getNotification().getTitle();
        String msg = remoteMessage.getNotification().getBody();

        mContext = this;
        Log.e("TITLE", title);

        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (mContext.getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "my_channel")
                    .setContentText(msg)
                    .setContentTitle(title)
                    .setSound(sound)
                    .setSmallIcon(R.drawable.notification)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);


            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notification.build());
        }


    }
}
