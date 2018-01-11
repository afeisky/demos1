package afei.stdemo.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chaofei on 17-12-8.
 */

public class TimerService extends Service {
    private static final String TAG = "STdemo:TimerService";
    private Context mContext=null;
    private final IBinder myBinder = new LocalBinder();
    private static boolean isRunning=false;
    @Override
    public IBinder onBind(Intent intent) {
        Log.w(TAG, "onBind()");
        //Toast.makeText(this, "onBind()", Toast.LENGTH_SHORT).show();
        return myBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "onCreate()");
        mContext=this.getApplicationContext();
        //Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            startAlmTimer();
            isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        Log.w(TAG, "onDestroy()");
        isRunning=false;
        //Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
    }
    public void stop(){
        this.stopSelf();
    }
    public class LocalBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    private void startAlmTimer(){
        Intent intent =new Intent(BootupReceiver.ALAM_INTENT);
        PendingIntent pi=PendingIntent.getBroadcast(this, 0, intent, 0);
        //Calendar calendar=Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.add(Calendar.SECOND, 20);
        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),900000, pi);
        //first send once:
        this.getApplicationContext().sendBroadcast(intent);
    }

}