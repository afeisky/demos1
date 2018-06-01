package demo.stnowtest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import demo.api.Global;
import demo.api.LogX;

public class MainActivity extends Activity {
    private static final String TAG = "STnow:MainActivity";
    private AlertDialog mDialog;
    private LayoutInflater mLayoutInflater;
    private Button button1;
    private TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Global.init();
        LogX.w(TAG, "--1");
        setContentView(R.layout.layout_main);
        button1 = (Button) findViewById(R.id.button1);
        button1.setText(Global.getDirPRE());
        button1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditPREDialog();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView1 = (TextView) findViewById(R.id.textView1);

        if (hasGrantExternalRW(this)) {

        }
        init();
    }

    private void init(){
        Toast.makeText(this, "[STnow] MainActivity:onCreate()", Toast.LENGTH_SHORT).show();
        IntentFilter filter = new IntentFilter(BootupReceiver.ACTION_UPDATE_UI);
        registerReceiver(broadcastReceiver, filter);

        //IntentFilter intentFilter = new IntentFilter(ACTION_1_RECEIVE);
        //registerReceiver(broadcastReceiver, intentFilter, ACTION_1_RECEIVE, null);

        //Intent it = new Intent(ACTION_1_RECEIVE);
        //it.setComponent(new ComponentName(this.getPackageName(), MainActivity.class.getName()));//for androidO
        ///sendBroadcast(it);

        Intent it = new Intent(BootupReceiver.ACTION_BEGIN);
        it.setComponent(new ComponentName(this.getPackageName(), BootupReceiver.class.getName()));//for androidO
        sendBroadcast(it);

        //new NowBK().getBKdata("now","");
        //NowDown down = new NowDown(this.getApplicationContext());
        //down.start(Global.workPath);

        ///AServerUtils.startAService(this.getApplicationContext(), 5, MainActivity.class, AService.ACTION_1_CHANGED);

        LogX.w(TAG ,"--2");

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogX.w(TAG, "Main Receiver:" + intent.getAction());
            String comment = intent.getStringExtra("comment");
            textView1.setText(comment);
        }
    };

    private void EditPREDialog() {

        final EditText inputServer = new EditText(this);
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name))
                //.setIcon()
                .setView(inputServer)
                .setNegativeButton(getString(R.string.app_name), null);
        builder.setPositiveButton(getString(R.string.app_name),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputName = inputServer.getText().toString();
                        Global.SetDirPRE(inputName);
                        button1.setText(Global.getDirPRE());
                    }
                });
        builder.show();
    }

    @Override
    public void onDestroy() {
        LogX.e(TAG, "onDestroy()");
        unregisterReceiver(broadcastReceiver);
        Toast.makeText(this, "[STnow] MainActivity:onDestroy()", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }


    private int PERMISSIONS_CODE = 1;

    public boolean hasGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            LogX.w(TAG, "hasGrantExternalRW--11-");
            return false;
        }
        LogX.w(TAG, "hasGrantExternalRW--22-");
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogX.w(TAG, "onRequestPermissionsResult--1-");
        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                LogX.w(TAG, "READ_EXTERNAL_STORAGE--2-");
                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    LogX.d(TAG, "READ_EXTERNAL_STORAGE--3-");
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                        LogX.w(TAG, "READ_EXTERNAL_STORAGE--4-");
                        init();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CODE);
                        LogX.w(TAG, "READ_EXTERNAL_STORAGE--5-");
                        //mWebView.loadData("Please set app right!", "text/html", "utf8");
                    }
                }
            }
        }
    }
}
