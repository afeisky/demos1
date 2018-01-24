package afei.stdemo.dailydown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import afei.api.LogX;


/**
 * Created by chaofei on 18-1-11.
 */

public class DailyReceiver extends BroadcastReceiver {

    private final String TAG = "afei.stdemo.dailydown.DailyReceiver";
    public static final String ACTION_DAILYDOWN_BEGIN="afei.stdemo.dailydown.DailyReceiver.begin";
    public static final String ACTION_DAILYDOWN_END="afei.stdemo.dailydown.DailyReceiver_end";
    public static DailyDown mDown=null;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_DAILYDOWN_BEGIN)) {
            String action = intent.getStringExtra("action");
            LogX.w(TAG, "dailydown start! " + action);
            if (mDown==null){
                mDown=new DailyDown(context);
            }
            if (mDown.isRunning()) {
                //mDown.stop();
            }else{
                new Thread(mDown).start();
            }
        }else if (intent.getAction().equals(ACTION_DAILYDOWN_END)) {
            String action = intent.getStringExtra("action");
            LogX.w(TAG, "dailydown start! " + action);
            if (mDown!=null){
                if (mDown.isRunning()) {
                    mDown.stop();
                }
                mDown=null;
            }
        }
    }

}
