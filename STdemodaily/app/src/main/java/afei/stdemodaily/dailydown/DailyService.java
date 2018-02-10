package afei.stdemodaily.dailydown;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;

import afei.api.LogX;
import afei.stdemodaily.R;
import afei.stdemodaily.activity.MainActivity;

/**
 * Created by chaofei on 17-12-8.
 */

public class DailyService extends Service {
    private static final String TAG = "STdemodaily:DailyService";
    private Context mContext=null;
    private final IBinder myBinder = new LocalBinder();
    private boolean isRunning=false;
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
            start1();//startAlmTimer();
            notify1();
        //    isRunning = true;
        //}
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        LogX.w(TAG, "onDestroy() 1");
        isRunning=false;
        notificationManager.cancel(0);
        //Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        LogX.w(TAG, "onDestroy() 2");
    }
    public void stop(){
        this.stopSelf();
    }
    public class LocalBinder extends Binder {
        DailyService getService() {
            return DailyService.this;
        }
    }
    //PendingIntent pi;
    //AlarmManager alarm;
    //private void startAlmTimer(){
    //    Intent intent =new Intent(BootupReceiver.ALAM_INTENT);
    //    pi=PendingIntent.getBroadcast(this, 0, intent, 0);
    //    //Calendar calendar=Calendar.getInstance();
    //    //calendar.setTimeInMillis(System.currentTimeMillis());
    //    //calendar.add(Calendar.SECOND, 20);
    //    alarm=(AlarmManager)getSystemService(ALARM_SERVICE);
    //    alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),3000000, pi);
    //    //first send once:
    //    this.getApplicationContext().sendBroadcast(intent);
    //}
    private DailyDown mDown=null;
    private void start1(){
        if (mDown==null){
            mDown=new DailyDown(mContext);
        }
        if (mDown.isRunning()) {
            LogX.w(TAG, "start1() mDown.isRunning()");
            if (mDown.checkRunlongtime()){
                //new Thread(mDown).start();
            }
        }else{
            LogX.w(TAG, "start1() mDown.isRunning() else ");
            new Thread(mDown).start();
        }

    }
    NotificationManager notificationManager = null;

    private void notify1(){
        LogX.e(TAG, "notify1() ");
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