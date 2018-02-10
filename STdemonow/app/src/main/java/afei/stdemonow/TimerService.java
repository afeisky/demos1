package afei.stdemonow;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

import afei.api.LogX;

/**
 * Created by chaofei on 17-12-8.
 */

public class TimerService extends Service {
    private static final String TAG = "STdemo:TimerService";
    public static String ACTION_ALARM="afei.stdemonow.alamtimer.ok";
    private Context mContext=null;
    private final IBinder myBinder = new LocalBinder();
    private static boolean isRunning=false;
    @Override
    public IBinder onBind(Intent intent) {
        LogX.w(TAG, "onBind()");
        //Toast.makeText(this, "onBind()", Toast.LENGTH_SHORT).show();
        return myBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LogX.w(TAG, "onCreate()");
        mContext=this.getApplicationContext();
        //Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //if (!isRunning) {
            startAlmTimer(this.getApplicationContext());
       //     isRunning = true;
        //}
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        LogX.w(TAG, "onDestroy()");
        isRunning=false;
        unregisterReceiver(broadcastReceiver);
        //Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
    public void stop(){
        this.stopSelf();
    }
    public class LocalBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    private void startAlmTimer(Context context){
        LogX.w(TAG, "startAlmTimer()");
        IntentFilter filter = new IntentFilter(ACTION_ALARM);
        registerReceiver(broadcastReceiver, filter);
        //Calendar calendar=Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.add(Calendar.SECOND, 20);
        if (down==null) {
            down=new NowDown(mContext);
        }
        runDown();
        startAlm();
    }
    private int timerLong=60000;
    private void startAlm(){
        Intent intent =new Intent(ACTION_ALARM);
        mContext.sendBroadcast(intent);
//mContext.sendBroadcastAsUser(intent, UserHandle.OWNER);
        PendingIntent pi=PendingIntent.getBroadcast(mContext, 0, intent, 0);
        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),timerLong, pi);

    }
    private NowDown down;

    private void runDown(){
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        LogX.e(TAG,"service timer: runDown."+timeNow);
        int time1 = Integer.valueOf(timeNow);
        if ((true)||(time1 >= 915 && time1 <= 1131)||(time1 >= 1300 && time1 <= 1515)) {
            down.start();
        }
    }
    private String lines="";
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            runDown();
        }
    };


}