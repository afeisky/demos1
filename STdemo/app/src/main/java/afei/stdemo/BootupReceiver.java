package afei.stdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import afei.api.LogX;

/**
 * Created by chaofei on 17-12-8.
 */

public class BootupReceiver extends BroadcastReceiver {
    private final String TAG = "STdemo:BootupReceiver";
    public static final String ALAM_INTENT="afei.stdemo.BootupReceiver";
    private static int timerCount=0;
    private SimpleDateFormat mdhms = new SimpleDateFormat("MM-dd_HH:mm:ss");
    public static final String ACTION_DOWN_TEMP="afei.stdemo.download_temp";
    @Override
    public void onReceive(Context context, Intent intent) {
        String tagAction="";
        LogX.w(TAG,"BroadcastReceiver--->"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+intent.getAction());
        if (ACTION_DOWN_TEMP.equals(intent.getAction())) {
            Toast.makeText(context, ACTION_DOWN_TEMP, Toast.LENGTH_SHORT).show();
            new DownOne(context).start(intent);
            return;
        }
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
        }else
        if (intent.getAction().equals(ALAM_INTENT)){
            Toast.makeText(context, "afei.stdemo.BootupReceiver.actionIntent", Toast.LENGTH_SHORT).show();
            LogX.w(TAG,"Add alarm!!!!--->"+intent.getAction());
            String action=intent.getStringExtra("action");
            LogX.w(TAG, "service start! "+action);
            //startTimerService(context,"");
            //Intent intent1 = new Intent("afei.stdemodaily.begin");
            //context.sendBroadcast(intent1);
            startTimerService(context,tagAction);
        }
    }

    private void startTimerService(Context context,String tagAction){
        Intent intentService=new Intent(context, TimerService.class);
        intentService.putExtra("flag", tagAction);
        context.startService(intentService);
    }
}