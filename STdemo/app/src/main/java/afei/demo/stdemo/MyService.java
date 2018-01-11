package afei.demo.stdemo;

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

public class MyService extends Service {
    private static final String TAG = "JsonViewer:MyService";
    private Context mContext=null;
    private final IBinder myBinder = new LocalBinder();
    public static final String ActionUI="afei.demo.jsonviewer.updateUI";
    private static final String ActionDown="afei.demo.jsonviewer.down";
    private static boolean isRunning=false;
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind()");
        //Toast.makeText(this, "onBind()", Toast.LENGTH_SHORT).show();
        return myBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate()");
        mContext=this.getApplicationContext();
        //Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            start(intent);
            isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy()");
        isRunning=false;
        unregisterReceiver(broadcastReceiver);
        //Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
    }
    public void stop(){
        this.stopSelf();
    }
    public class LocalBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
    private int n=0;

    private void start(Intent intent) {
        regReceiver();
        startAlmTimer();
        startDownPre();
    }
    private SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private void startDown(){
        Log.e(TAG, "startDown() 11, "+Global.mDown.isRunning());
        if (Global.mDown.isRunning()) {
            //Global.mDown.stop();
        } else {
            Global.mDown.start(1);
        }
        Log.e(TAG, "startDown() 22, "+Global.mDown.isRunning());
    }

    private void startDownPre(){
        Log.e(TAG, "startDownPre() 1, "+ymdhms.format(new Date()));
        if (Global.mDown == null) {
            Log.e(TAG, "startDownPre() 2");
            Global.mDown = new Down(mContext);
        }
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        int time1 = Integer.valueOf(timeNow);
        if (time1 >= 1815 || time1 <= 800) {
            startDown();
        }else{
            timerCount++;
            if (timerCount>100){
                timerCount=1;
            }
            timeNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            updateUI("Timer:" + timeNow, timerCount % 100);
            return;
        }
    }
    private void regReceiver(){
        IntentFilter filter = new IntentFilter(MyService.ActionDown);
        registerReceiver(broadcastReceiver, filter);

    }
    private void startAlmTimer(){
        Intent intent =new Intent(ActionDown);
        PendingIntent pi=PendingIntent.getBroadcast(this, 0, intent, 0);
        //Calendar calendar=Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.add(Calendar.SECOND, 20);
        AlarmManager alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),900000, pi);
    }

    private int timerCount=0;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startDownPre();
        }
    };
    private void updateUI(String comment,int percent){
        Log.e(TAG,"555:"+comment+",percent:"+percent);
        Intent intent = new Intent(MyService.ActionUI);
        Bundle mBundle = new Bundle();
        DataCallback  dcb=new DataCallback(5, comment, percent);
        mBundle.putSerializable("data",dcb);
        intent.putExtras(mBundle);
        mContext.sendBroadcast(intent);
    }
    private void updateUI(String comment){
        Log.e(TAG,"666:"+comment);
        Intent intent = new Intent(MyService.ActionUI);
        Bundle mBundle = new Bundle();
        DataCallback  dcb=new DataCallback(5, comment, 0);
        mBundle.putSerializable("data",dcb);
        intent.putExtras(mBundle);
        mContext.sendBroadcast(intent);
    }
}