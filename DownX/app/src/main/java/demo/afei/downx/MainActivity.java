package demo.afei.downx;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


/*
-usage: send adb broadcast to download file:

create file: _down
[url]http://www.baidu.com/aa.jpeg
[name]a.jpeg

cmd:
adb push _downx /sdcard/Downx/
adb shell am broadcast  -a demo.afei.downx.start

 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        if (hasGrantExternalRW(this)){
            init();
        }
    }

    private void init(){
        if (Receiver.init()) {
            Intent it = new Intent(Receiver.ACTION_BEGIN);
            it.setComponent(new ComponentName(this.getPackageName(), Receiver.class.getName()));//for androidO
            sendBroadcast(it);
        }
        textView1=(TextView) findViewById(R.id.textView1);
        textView1.setText(Receiver.labelPathFileName);
    }

    //---
    public static boolean hasGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            Log.w(TAG, " hasGrantExternalRW: fail-");
            return false;
        }
        return true;
    }

    private int PERMISSIONS_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                        init();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CODE);
                        Log.w(TAG, "onRequestPermissionsResult--fail-");
                    }
                }
            }
        }
    }
}
