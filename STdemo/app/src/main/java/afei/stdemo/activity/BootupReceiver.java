package afei.stdemo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import afei.stdemo.dailydown.DailyReceiver;

/**
 * Created by chaofei on 17-12-8.
 */

public class BootupReceiver extends BroadcastReceiver {

    private final String TAG = "STdemo:BootupReceiver";
    public static final String ALAM_INTENT="afei.stdemo.BootupReceiver.actionIntent";
    public static final String ACTION_UPDATE_UI="afei.stdemo.updateUI";
    private static int timerCount=0;
    private SimpleDateFormat mdhms = new SimpleDateFormat("MM-dd_HH:mm:ss");
    @Override
    public void onReceive(Context context, Intent intent) {
        String tagAction="";
        Log.w(TAG,"BroadcastReceiver--->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "ACTION_BOOT_COMPLETED!!!");
            }
            Log.d(TAG,"ACTION_BOOT_COMPLETED--->,"+intent.getAction());
            tagAction=Intent.ACTION_BOOT_COMPLETED;
        }else {
            String wifiSSID="";
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    Log.d(TAG, "Wifi SSID:" + wifiInfo.getSSID());
                    wifiSSID=wifiInfo.getSSID();
                }
            }

            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

                if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                    Log.d(TAG, "WIFI disable!");
                }

                if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                    Log.d(TAG, "WIFI enabled!");
                    tagAction=intent.getAction()+":"+wifiSSID;
                }
            }
        }

        if (tagAction.length()>0){
            Log.d(TAG, "-->tagAction : "+tagAction+",  to call service!");
            startTimerService(context,tagAction);
        }
        if (intent.getAction().equals(ALAM_INTENT)){
            Log.w(TAG,"Add alarm!!!!--->"+intent.getAction());
            String action=intent.getStringExtra("action");
            Log.w(TAG, "service start! "+action);
            start(context);
        }
    }

    private void start(Context context){
        Log.w(TAG, "startDownPre() 1, "+mdhms.format(new Date()));
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        int time1 = Integer.valueOf(timeNow);
        if (time1 >= 1815 || time1 <= 800) {
            startDailyDown(context);
        }else{
            timerCount++;
            if (timerCount>100){
                timerCount=1;
            }
            timeNow = mdhms.format(new Date());
            DataCallback.updateUI(context,DataCallback.TYPE_TIMERSERVICE_START,"" + timeNow, timerCount % 100);
            return;
        }
    }
    private void startDailyDown(Context context){
        Intent intent = new Intent(DailyReceiver.ACTION_DAILYDOWN_BEGIN);
        context.sendBroadcast(intent);
    }

    private void startTimerService(Context context,String tagAction){
        Intent intentService=new Intent(context, TimerService.class);
        intentService.putExtra("flag", tagAction);
        context.startService(intentService);
    }
}