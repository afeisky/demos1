package afei.demo.stdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chaofei on 17-12-8.
 */

public class BootupReceiver extends BroadcastReceiver {

    private final String TAG = "JsonViewer";
    private final String PACKAGE_NAME = "afei.demo.jsonviewer";
    private final String ALAM_INTENT="afei.demo.jsonviewer.intent";
    @Override
    public void onReceive(Context context, Intent intent) {
        String tagAction="";
        Log.d(TAG,"BroadcastReceiver--->time: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
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
            Intent intentService=new Intent(context, MyService.class);
            intentService.putExtra("action", tagAction);
            context.startService(intentService);
        }else if (intent.getAction().equals(ALAM_INTENT)){
            Log.e(TAG,"Add alarm!!!!--->"+intent.getAction());
            String action=intent.getStringExtra("action");
            Log.e(TAG, "service start! "+action);
            //Intent intentMain = new Intent(context,MainActivity.class);
            //intentMain.putExtra("action", action);
            //intent.putExtra(MainActivity.RETURN_INFO, infoEditText.getText().toString());
            //context.startActivity(intentMain);
        }
    }

}