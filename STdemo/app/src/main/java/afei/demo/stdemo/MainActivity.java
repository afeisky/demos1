package afei.demo.stdemo;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;

public class MainActivity extends Activity  {
    private static final String TAG = "JsonViewer";

    private WebView mWebView;
    private View mLoading;
    private Intent mIntent;
    private TextView mMessageView;
    private ProgressBar mProgressbar;
    private TextView textView1;
    private Button btnDown;
    private String htmldata="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageView = (TextView) findViewById(R.id.download_message);
        mProgressbar = (ProgressBar) findViewById(R.id.download_progress);
        textView1 = (TextView) findViewById(R.id.download_message);
        mWebView= (WebView) findViewById(R.id.webView1);
        btnDown = (Button) findViewById(R.id.download_btn);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init2();
            }
        });
        btnDown.setText("Start");
        Log.d(TAG, "----Game Start!!!");
        String action=getIntent().getStringExtra("action");
        if (action!=null && action.length()>0){
            Log.d(TAG, "----onCreate(): action "+action);
            htmldata+="----onCreate(): action"+action+"</br>";
        }
        if (hasGrantExternalRW(this)) {
            init();//sn_down_api();//init();
        } else {
            Log.d(TAG, "----Game Over!");
        }
    }

    private String strHtml="";
    private void updateUI(String str){
        strHtml=str+"<br>"+strHtml;
        mWebView.loadData(strHtml, "text/html","utf8");
        //if (str.equals("Finish!")) {
        mWebView.scrollTo(0,strHtml.length());
        //}

    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                DataCallback data=(DataCallback)msg.obj;
                if (data.type==4) {//SocketServer
                    updateUI(data.comment);
                }else if (data.type==2){//finish!
                    btnDown.setText("Start");
                }else{
                    btnDown.setText("Stop");
                    if (data.type==1){
                        mMessageView.setText(data.comment);
                        mProgressbar.setProgress(data.percent);
                    }else {
                        updateUI(data.comment);
                    }
                }
            }if (msg.what == 2) {

            }

        }
    };

    public void showCallback() {
        Message msg=new Message();
        msg.what=1;
        msg.obj=new DataCallback(0,"",0);
        mHandler.sendMessage(msg);
    }

    private String ALAM_INTENT="afei.demo.jsonviewer.intent";
    static final String ID = "id";
    static final String TIME = "alarm_time";

    private void init(){
        IntentFilter filter = new IntentFilter(MyService.ActionUI);
        registerReceiver(broadcastReceiver, filter);

        startService();
        //-----
        /*
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALAM_INTENT);
        intent.setData(Uri.parse("test,reciver!!!"));
        intent.setClass(this, MainActivity.class);
        intent.putExtra("ID", 1);
//          intent.putExtra(LABEL, label);
//          intent.putExtra(TIME, atTimeInMillis);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        intent = new Intent("aaa");
        intent.putExtra("msg","fafdsf");
        intent.setAction(ALAM_INTENT);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),5*1000,pi);
        Log.e(TAG,"Add alarm!!!!");

        if (Global.mDown==null){
            Global.mDown=new Down();
        }
        //String timeNow=new SimpleDateFormat("HHmm").format(new Date());
        //int time1=Integer.valueOf(timeNow);
        //if ((time1>=1515 || time1<=800)) {
        //Global.mDown.start(0);
        //}
        */
    }
    private ArrayList lines = new ArrayList();
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "Main Receiver:"+intent.getAction());
            if (intent.getAction().equals(MyService.ActionUI)) {
                DataCallback data = (DataCallback) intent.getSerializableExtra("data");
                //textView.setText(intent.getExtras().getString("data"));
                String str = "["+data.type+"] "+data.comment;
                lines.add(str + "</br>");
                if (lines.size()>15){
                    lines.remove(0);
                }
                str="";
                for (int i=0;i<lines.size();i++){
                    str+=lines.get(i);
                }
                mWebView.loadData(str, "text/html", "utf8");
                Log.e(TAG, str);
            }
        }
    };

    private void startService(){
        Intent intentService=new Intent(this.getApplication(), MyService.class);
        intentService.putExtra("flag", 1);
        this.getApplication().startService(intentService);
    }
    private void stopService(){
        Intent intentService=new Intent(this.getApplication(), MyService.class);
        this.getApplication().stopService(intentService);
    }
    private void init2(){

        if (Global.mDown.isRunning()) {
            Global.mDown.stop();
        } else {
            Global.mDown.start(1);
        }
    }

    //---
    public static boolean hasGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            Log.d(TAG, "hasGrantExternalRW--11-");
            return false;
        }
        Log.d(TAG, "hasGrantExternalRW--22-");
        return true;
    }

    private int PERMISSIONS_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult--1-");
        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                Log.d(TAG, "READ_EXTERNAL_STORAGE--2-");
                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.d(TAG, "READ_EXTERNAL_STORAGE--3-");
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                        Log.d(TAG, "READ_EXTERNAL_STORAGE--4-");
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CODE);
                        Log.d(TAG, "READ_EXTERNAL_STORAGE--5-");
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }

}