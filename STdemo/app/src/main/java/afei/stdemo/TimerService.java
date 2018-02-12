package afei.stdemo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

import afei.api.LogX;

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
        if (!isRunning) {
            mHandler.sendEmptyMessageDelayed(msg_id, 0);
            repeat();
            //startAlmTimer();
            isRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        LogX.w(TAG, "onDestroy() 1");
        isRunning=false;
        //if (pi!=null && alarm!=null){  alarm.cancel(pi);}
        //Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
        mHandler.removeMessages(msg_id);
        notificationManager.cancel(0);
        super.onDestroy();
        LogX.w(TAG, "onDestroy() 2");
    }
    public void stop(){
        this.stopSelf();
    }
    public class LocalBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }
    PendingIntent pi;
    AlarmManager alarm;
    private void startAlmTimer(){
        Intent intent =new Intent(BootupReceiver.ALAM_INTENT);
        pi=PendingIntent.getBroadcast(this, 0, intent, 0);
        //Calendar calendar=Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.add(Calendar.SECOND, 20);
        alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),3000000, pi);
        //first send once:
        this.getApplicationContext().sendBroadcast(intent);
    }
    private final int msg_id = 1;
    private int msg_delay_secondes = 3000000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msg_id:
                    repeat();
                    removeMessages(msg_id);
                    if (isRunning) {
                        LogX.w(TAG, "handleMessage()"+yyyymdhms.format(new Date()));
                        sendEmptyMessageDelayed(msg_id, msg_delay_secondes);//this.sendMessageDelayed()
                        break;
                    }
            }
        }
    };
    private void repeat(){
        LogX.w(TAG, "repeat() "+yyyymdhms.format(new Date()));
        startDailyDown();
        notify1();
    }
    private SimpleDateFormat yyyymdhms = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
    private void startDailyDown(){
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        int time1 = Integer.valueOf(timeNow);
        if (time1 >= 1815 || time1 <= 900) {
            msg_delay_secondes=3000000;
            LogX.w(TAG, "startDailyDown() 1, "+yyyymdhms.format(new Date()));
            Intent intent = new Intent("afei.stdemodaily.begin");
            mContext.sendBroadcast(intent);
        }else if (time1 >= 1700 || time1 <= 1815) {
                msg_delay_secondes=300000;
            }

    }

    NotificationManager notificationManager = null;

    private void notify1(){
        LogX.d(TAG, "notify1() "+this.getPackageName()+","+getText(R.string.app_name));
        CharSequence title = getText(R.string.app_name);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED); //only go MainActivity onice.
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification.Builder nbuilder = new Notification.Builder(this);
        nbuilder.setContentTitle(this.getPackageName());
        nbuilder.setContentText("Running");
        nbuilder.setSmallIcon(R.drawable.ic_launcher);
        nbuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        nbuilder.setAutoCancel(true);
        nbuilder.setContentIntent(pendingIntent);

        Notification notification = nbuilder.build();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

}