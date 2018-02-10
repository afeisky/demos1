package afei.stdemodaily.dailydown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import afei.api.LogX;


/**
 * Created by chaofei on 18-1-11.
 */

public class DailyReceiver extends BroadcastReceiver {

    private final String TAG = "afei.stdemodaily.DailyReceiver";
    public static final String ACTION_DAILYDOWN_BEGIN="afei.stdemodaily.begin";
    public static final String ACTION_DAILYDOWN_END="afei.stdemodaily.end";
    private SimpleDateFormat yyyymdhms = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_DAILYDOWN_BEGIN)) {
            String action = intent.getStringExtra("action");
            LogX.w(TAG, "dailydown start! "+yyyymdhms.format(new Date()) + action);
            startDailyService(context,"");
        }else if (intent.getAction().equals(ACTION_DAILYDOWN_END)) {
            String action = intent.getStringExtra("action");
            LogX.w(TAG, "dailydown stop! "+yyyymdhms.format(new Date()) + action);
            stopDailyService(context);
        }
    }

    private boolean startDailyDown_pre(){
        String timeNow = new SimpleDateFormat("HHmm").format(new Date());
        int time1 = Integer.valueOf(timeNow);
        if (time1 >= 1815 || time1 <= 900) {
            return true;
        }
        return false;
    }
    private void startDailyService(Context context,String tagAction){
        //if (!startDailyDown_pre()) {return;}
        Toast.makeText(context, ACTION_DAILYDOWN_BEGIN, Toast.LENGTH_SHORT).show();
        Intent intentService=new Intent(context, DailyService.class);
        intentService.putExtra("flag", tagAction);
        context.startService(intentService);
    }

    private void stopDailyService(Context context){
        Toast.makeText(context, ACTION_DAILYDOWN_END, Toast.LENGTH_SHORT).show();
        Intent intentService=new Intent(context, DailyService.class);
        context.stopService(intentService);
    }

}
