package demo.stnowtest;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import demo.api.FileX;
import demo.api.Global;
import demo.api.LogX;

/**
 * Created by chaofei on 17-12-8.
 */

public class BootupReceiver extends BroadcastReceiver {

    private static final String TAG = "STdemonow:BootupReceiver";
    public static final String ACTION_BOOT_COMPLETED="android.intent.action.BOOT_COMPLETED";
    public static final String ACTION_BEGIN="afei.stdemonow.begin";
    public static final String ACTION_NOFITY="afei.stdemonow.nofity";
    public static final String ACTION_UPDATE_UI="afei.stdemonow.updateUI";
    public static final String ACTION_LOG="afei.stdemonow.log";
    public static final String ACTION_DOWN_DONE="afei.stdemonow.down_done";
    private static int timerCount=0;
    private SimpleDateFormat mdhms = new SimpleDateFormat("MM-dd_HH:mm:ss");
    private static Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        String tagAction = "";
        if (ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Global.init();
        }
        LogX.w(TAG, "BroadcastReceiver--->" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + intent.getAction());
        String timeNow = mdhms.format(new Date());
        String action=intent.getAction();
        if (ACTION_BEGIN.equals(intent.getAction()) || ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            String data = intent.getStringExtra("action");
            Toast.makeText(context, "BootupReceiver() "+data, Toast.LENGTH_SHORT).show();
            LogX.w(TAG, "BootupReceiver.startService() ----> "+action+", "+mdhms.format(new Date()));
            startService(context,action);
        } else if (ACTION_NOFITY.equals(intent.getAction())) {
            String data = intent.getStringExtra("action");
            //Toast.makeText(context, "BootupReceiver()", Toast.LENGTH_SHORT).show();
            LogX.w(TAG, "BootupReceiver ->" + action);
            createNofify(ACTION_NOFITY,timeNow);
            down_start();//AService.sendMessage(1, new AService.MsgData(0));
        } else if (ACTION_LOG.equals(intent.getAction())) {
            String data = intent.getStringExtra("data");
            LogX.w(TAG, "BootupReceiver ->" + action);
            createNofify(ACTION_NOFITY,timeNow+" "+data);
        }else if (ACTION_DOWN_DONE.equals(intent.getAction())) {
            int result = intent.getIntExtra("result",-1);
            String comment = intent.getStringExtra("comment");
            LogX.w(TAG, "BootupReceiver ->" + action);
            createNofify(ACTION_NOFITY,timeNow+" done:"+result+"]"+comment);
        }else {
            LogX.w(TAG, "?? BootupReceiver ->" + action);
        }
    }
    private void startService(Context context,String action){
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        //LogX.w(TAG, "start() 1, "+timeNow+","+mdhms.format(new Date()));
        int time1 = Integer.valueOf(timeNow);
        if ((true)||(time1 >= 815 && time1 <= 1131)||(time1 >= 1300 && time1 <= 1515)) {

            Intent intentService=new Intent(context, AService.class);
            //intentService.putExtra("flag", tagAction);
            context.startService(intentService);
        }
    }
    private void stopService(Context context){
        LogX.w(TAG, "BootupReceiver.stopService() "+mdhms.format(new Date()));
    }

    private static NotifyUtils notificationUtils=null;
    public static void createNofify(String title,String content) {
        content=content.replace(",","]");
        if (Global.strNofity.length()>500) {
            int pos=Global.strNofity.lastIndexOf("\n");
            Global.strNofity = Global.strNofity.substring(0,pos);
        }
        //Global.strNofity=timeNow+" "+data+"\n"+Global.strNofity;
        Global.strNofity=content+"\n"+Global.strNofity;
        LogX.d(TAG, "createNofify() "+Global.strNofity.length());
        Intent it = new Intent();
        //it.setAction(BootupReceiver.ACTION_BEGIN);
        //it.putExtra("flag", 1);
        PendingIntent piBoard = PendingIntent.getBroadcast(mContext, 0,
                it, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationUtils = new NotifyUtils(mContext);
        title=Global.getDirPRE();
        notificationUtils.sendNotification(title, Global.strNofity, piBoard);

        //send sendBroadcast to UI
        Intent intent = new Intent();
        intent.putExtra("comment", Global.strNofity);
        intent.setAction(BootupReceiver.ACTION_UPDATE_UI);
        mContext.sendBroadcast(intent);

        FileX.add2File(Global.getWorkPath()+"/time.txt", content+"\n");
    }

    private void down_start(){
        new NowDown(mContext).start2(Global.workPath);
    }


}