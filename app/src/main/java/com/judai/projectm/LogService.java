package com.judai.projectm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Date;

public class LogService extends Service {
    private static final int NOTI_ID = 1;
    Handler handler = new Handler();
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("LogService","run: "+new Date().toString());
                handler.postDelayed(this,1000);
                returnNOtification(new Date().toString());
            }
        },1000);
        return START_NOT_STICKY;
    }

    public void returnNOtification(String text) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
        Notification mBuilder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle(RoomChat.ROOM)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(bitmap)
                .build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        /* cập nhật thông báo */
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTI_ID, mBuilder);
        }
        startForeground(123,mBuilder);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
