package afei.stdemonow;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import afei.api.Global;
import afei.api.LogX;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by chaofei on 17-12-8.
 */

public class BootupReceiver extends BroadcastReceiver {

    private final String TAG = "STdemonow:BootupReceiver";
    public static final String ACTION_BOOT="afei.stdemonow.boot";
    public static final String ACTION_UPDATE_UI="afei.stdemonow.updateUI";
    public static final String ACTION_DOWN_TEMP="afei.stdemonow.download_temp";
    private static int timerCount=0;
    private SimpleDateFormat mdhms = new SimpleDateFormat("MM-dd_HH:mm:ss");

    @Override
    public void onReceive(Context context, Intent intent) {
        String tagAction="";
        LogX.w(TAG,"BroadcastReceiver--->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+intent.getAction());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            LogX.d(TAG,"ACTION_BOOT_COMPLETED--->,"+intent.getAction());
            tagAction=Intent.ACTION_BOOT_COMPLETED;
        }else {
            String wifiSSID="";
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    LogX.d(TAG, "Wifi SSID:" + wifiInfo.getSSID());
                    wifiSSID=wifiInfo.getSSID();
                }
            }

            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

                if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                    LogX.d(TAG, "WIFI disable!");
                }

                if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                    LogX.d(TAG, "WIFI enabled!");
                    tagAction=intent.getAction()+":"+wifiSSID;
                }
            }
        }


        if (tagAction.length()>0){
            LogX.d(TAG, "-->tagAction : "+tagAction+",  to call service!");
            startTimerService(context,tagAction);
        }
        if (intent.getAction().equals(BootupReceiver.ACTION_BOOT)){
            LogX.w(TAG,"Add alarm!!!!--->"+intent.getAction());
            String action=intent.getStringExtra("action");
            LogX.w(TAG, "service start! "+action);
            start(context);
            //sendAll(context);
        }

    }
    private void sendAll(Context context){
        Intent intent =new Intent("afei.stdemo.BootupReceiver.actionIntent");
        LogX.w(TAG, "service start! sendAll:-->");
        context.sendBroadcast(intent);
    }
    private void start(Context context){
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        LogX.w(TAG, "start() 1, "+timeNow+","+mdhms.format(new Date()));
        int time1 = Integer.valueOf(timeNow);
        if ((true)||(time1 >= 815 && time1 <= 1131)||(time1 >= 1300 && time1 <= 1515)) {
            startTimerService(context,"");
        }else{
            stopTimerService(context);
            timerCount++;
            if (timerCount>100){
                timerCount=1;
            }
            timeNow = mdhms.format(new Date());
            return;
        }
    }

    private void stopTimerService(Context context){
        Intent intentService=new Intent(context, TimerService.class);
        context.stopService(intentService);
        LogX.w(TAG, "startTimerService() 1, "+mdhms.format(new Date()));
        /*
        NotificationManager motificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.tickerText = this.getClass().getName()+"===1";
        notification.when = System.currentTimeMillis();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        motificationManager.notify(0, notification);
*/
    }
    private void startTimerService(Context context,String tagAction){
        Intent intentService=new Intent(context, TimerService.class);
        intentService.putExtra("flag", tagAction);
        context.startService(intentService);
        LogX.w(TAG, "startTimerService() 1, "+mdhms.format(new Date()));
        /*
        NotificationManager motificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.tickerText = this.getClass().getName()+"===1";
        notification.when = System.currentTimeMillis();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        motificationManager.notify(0, notification);
*/
    }
}