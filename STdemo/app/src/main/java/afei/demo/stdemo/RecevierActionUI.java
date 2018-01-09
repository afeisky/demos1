package afei.demo.stdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by chaofei on 18-1-8.
 */

public class RecevierActionUI extends BroadcastReceiver {

    private final String TAG = "JsonViewer";
    private final String PACKAGE_NAME = "afei.demo.jsonviewer";
    private final String ALAM_INTENT="afei.demo.jsonviewer.intent";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,"ActionUI!!!!--->"+intent.getAction());
        if (intent.getAction().equals(MyService.ActionUI)) {
            DataCallback data = (DataCallback) intent.getSerializableExtra("data");
            String str = data.comment;
        }

    }

}