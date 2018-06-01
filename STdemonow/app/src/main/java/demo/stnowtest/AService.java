package demo.stnowtest;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import demo.api.Global;
import demo.api.LogX;
import demo.api.TimeX;

/**
 * Created by chaofei on 17-12-8.
 */

public class AService extends Service {
    private static final String TAG = "STdemo:AService";
    public static String ACTION_ALARM = "demo.stnow.service.ACTION_ALARM";
    public static Context mContext = null;
    private final IBinder myBinder = new LocalBinder();
    private static boolean isRunning = false;

    private Handler mServiceHandler;

    private Notification mNotification;
    private NotificationManager mManager;

    @Override
    public IBinder onBind(Intent intent) {
        LogX.e(TAG, "onBind()");
        //Toast.makeText(this, "onBind()", Toast.LENGTH_SHORT).show();
        return myBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Global.init();
        LogX.e(TAG, "onCreate()");
        log("onCreate()");
        mContext = this.getApplicationContext();
        onCreate1();
    }

    private void onCreate1(){
        //Toast.makeText(this, "onCreate()", Toast.LENGTH_SHORT).show();
        // Monitor USB status
        IntentFilter mAStatusIntentFilter = new IntentFilter(ACTION_1_CHANGED);
        registerReceiver(broadcastReceiver, mAStatusIntentFilter,ACTION_1_CHANGED, null);

        //HandlerThread handlerThread = new HandlerThread(this.getClass().getName());
        //handlerThread.start();
        //mServiceHandler = new ServiceHandler(handlerThread.getLooper());
        BootupReceiver.createNofify("STnow","AAAA");
        // Print version info
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            LogX.d(TAG, "Version name=" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            LogX.e(TAG, "Fail to get application version name.");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogX.e(TAG, "onStartCommand()");
        log("onStartCommand()");
        //if (aTask==null) {
        //    LogX.e(TAG, "onStartCommand() 111");
        //    aTask = new ATask();
        //    aTask.start();
        //}

        //if (!isRunning) {
        //new PollingThread().start();
        startAlmTimer(this.getApplicationContext());
        //     isRunning = true;
        //}
        return START_STICKY;//reboot service.
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        LogX.e(TAG, "onDestroy()");
        log("onDestroy()");
        isRunning = false;
        down = null;
        unregisterReceiver(broadcastReceiver);
        //Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    public void stop() {
        LogX.e(TAG, "stop()");
        log("stop()");
        this.stopSelf();
    }

    public class LocalBinder extends Binder {
        AService getService() {
            return AService.this;
        }
    }

    private void startAlmTimer(Context context) {
        LogX.e(TAG, "startAlmTimer()");
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        BootupReceiver.createNofify(null,"-->"+timeNow);
        //Calendar calendar=Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        //calendar.add(Calendar.SECOND, 20);

        //////runDown();
        //new MailX().getAll(Global.workPath);

        //Intent intent = new Intent(BootupReceiver.ACTION_NOFITY);
        //intent.setComponent(new ComponentName(this.getPackageName(), BootupReceiver.class.getName()));//for androidO

        //mContext.sendBroadcast(intent);
        //mContext.sendBroadcastAsUser(intent, UserHandle.OWNER);

        //Intent intent = new Intent(ACTION_1_CHANGED);
        //intent.setComponent(new ComponentName(this.getPackageName(),BootupReceiver.class.getName()));//for androidO
        //PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), timerLong, pi);
        //sendBroadcast(it);

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent it = new Intent(mContext, BootupReceiver.class);
        it.setAction(BootupReceiver.ACTION_NOFITY);
        sendBroadcast(it);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0,
                it, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = System.currentTimeMillis();//SystemClock.elapsedRealtime();
        manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime,timerLong, pendingIntent);
        //manager.setExact();
    }

    private int timerLong = 5000;

    private static NowDown down;

    private void runDown() {
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        LogX.e(TAG, "service timer: runDown." + timeNow + " , time:" + TimeX.getToday());
        int time1 = Integer.valueOf(timeNow);
        if ((true) || (time1 >= 915 && time1 <= 1131) || (time1 >= 1300 && time1 <= 1515)) {
            LogX.w(TAG, "-->down go run. " + timeNow);
            if (down == null) {
                LogX.w(TAG, "-->down is running ???  " + TimeX.getToday());
                down = new NowDown(mContext);
                down.start(Global.workPath);
            }
        }
    }
    private String lines = "";
    public static String ACTION_1_CHANGED = "android.permission.ACTION_1_CHANGE";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogX.w(TAG, " broadcastReceiver--");
            String timeNow = new SimpleDateFormat("HHmm").format(new Date());
            BootupReceiver.createNofify(null,"-->"+timeNow);
        }
    };

/*
    private NotifyUtils notificationUtils=null;
    private void createNofify(String title,String content) {

        LogX.d(TAG, "createNofify()");
        Intent it = new Intent();
        it.setAction(BootupReceiver.ACTION_BEGIN);
        it.putExtra("flag",1);
        PendingIntent piBoard = PendingIntent.getBroadcast(this, 0,
                it, PendingIntent.FLAG_UPDATE_CURRENT);


        notificationUtils = new NotifyUtils(this);
        notificationUtils.sendNotification(title, content,piBoard);
        //notificationUtils.changeTitleContent("cccAAA", "dddBBB");
    }

    private void updateNofify(String title,String content) {
        notificationUtils.changeTitleContent(title, content);
    }

    private void show1(){
        RemoteViews contentViews = new RemoteViews(getPackageName(),R.layout.layout_notify);
        contentViews.setImageViewResource(R.id.imageNo, R.mipmap.ic_launcher);
        contentViews.setTextViewText(R.id.titleNo, "AAA");
        contentViews.setTextViewText(R.id.textNo, "BBB");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.icon_switch_circle_green)
                .setContentTitle("My notification")
                .setTicker("new message");
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setContent(contentViews);
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
*/
    public void log(String str){
        Intent it = new Intent(BootupReceiver.ACTION_LOG);
        it.putExtra("data", str); // String
        it.setComponent(new ComponentName(this.getPackageName(), BootupReceiver.class.getName()));//for androidO
        sendBroadcast(it);
    }

    public static int MSG_WHAT_ID_DOWN_START=1;
    public static int MSG_WHAT_ID_DOWN_DONE=2;
    public static void sendMessage(int what,MsgData data){
        Message msg = new Message();
        msg.what = what;
        //msg.getData().putInt("result",result);
        //msg.getData().putString("data", data);
        msg.obj=(MsgData)data;
        mHandler.sendMessage(msg);
    }
    public static void sendMessage(int what,int result,String comment){
        Message msg = new Message();
        msg.what = what;
        MsgData mdata=new MsgData(0);
        mdata.result=result;
        mdata.comment=comment;
        msg.obj=(MsgData)mdata;
        mHandler.sendMessage(msg);
    }
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            LogX.d(TAG, " ServiceHandler message," + " what=" + what
                    + ", arg1=" + msg.arg1 + ", arg2=" + msg.arg2);
            if  (what==MSG_WHAT_ID_DOWN_START) {
                new NowDown(mContext).start2(Global.workPath);
            }
            else if  (what==MSG_WHAT_ID_DOWN_DONE) {

            }
        }
    };


    public static void sendBroadDownDone(int result,String comment){
        Intent it = new Intent(BootupReceiver.ACTION_DOWN_DONE);
        it.putExtra("result", result); // String
        it.putExtra("comment", comment); // String
        it.setComponent(new ComponentName(mContext.getPackageName(), BootupReceiver.class.getName()));//for androidO
        mContext.sendBroadcast(it);
    }

    public static class MsgData {
        public int result;
        public String filename;
        public String url;
        public String comment;
        MsgData(int _result){
            result=_result;
        }
    }
}