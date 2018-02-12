package afei.stdemodaily.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import afei.stdemodaily.Data.DataCallback;
import afei.stdemodaily.Data.Global;
import afei.stdemodaily.R;
import afei.stdemodaily.dailydown.DailyReceiver;
import afei.api.LogX;
import afei.stdemodaily.dailydown.DailyService;

import static android.text.Html.FROM_HTML_MODE_LEGACY;


public class MainActivity extends Activity  {
    private static final String TAG = "STdemodaily:MainActivity";

    private EditText mEditText;
    private View mLoading;
    private Intent mIntent;
    private TextView mMessageView;
    private ProgressBar mProgressbar;
    private Button btnDown;
    private String htmldata="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageView = (TextView) findViewById(R.id.download_message);
        mProgressbar = (ProgressBar) findViewById(R.id.download_progress);
        mEditText= (EditText) findViewById(R.id.editText);
        btnDown = (Button) findViewById(R.id.download_btn);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnDown.setText("Start");
        SimpleDateFormat ymdhms1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        LogX.init("","STdemo"+ymdhms1.format(new Date())+"log.txt");
        LogX.d(TAG, "----Game Start!!!");
        String action=getIntent().getStringExtra("action");
        if (action!=null && action.length()>0){
            Log.d(TAG, "----onCreate(): action "+action);
            htmldata+="----onCreate(): action"+action+"</br>";
        }
        if (hasGrantExternalRW(this)) {
            init();//sn_down_api();//init();
        } else {
            LogX.w(TAG, "----Game Over!");
        }

        //WebSettings webSettings = mWebView.getSettings();
        //webSettings.setUseWideViewPort(false);
        //webSettings.setLoadWithOverviewMode(false);

    }
/*
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
*/

    private void init(){
        IntentFilter filter = new IntentFilter(Global.ACTION_UPDATE_UI);
        registerReceiver(broadcastReceiver, filter);
        Intent intent = new Intent(DailyReceiver.ACTION_DAILYDOWN_BEGIN);
        sendBroadcast(intent);

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
            Global.mDown=new DailyDown();
        }
        //String timeNow=new SimpleDateFormat("HHmm").format(new Date());
        //int time1=Integer.valueOf(timeNow);
        //if ((time1>=1515 || time1<=800)) {
        //Global.mDown.start(0);
        //}
        */
    }
    private ArrayList strTimer = new ArrayList();
    private ArrayList lines = new ArrayList();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogX.w(TAG, "Main Receiver:"+intent.getAction());
            if (intent.getAction().equals(Global.ACTION_UPDATE_UI)) {
                DataCallback data = (DataCallback) intent.getSerializableExtra("data");
                //textView.setText(intent.getExtras().getString("data"));
                String str = "[" + data.type + "] " + data.comment;
                if (data.type==DataCallback.TYPE_TIMERSERVICE_START){
                    strTimer.add(data.comment);
                    if (strTimer.size() > 3) {
                        strTimer.remove(0);
                    }
                    str="";
                    for (int i = 0; i < strTimer.size(); i++) {
                        str += strTimer.get(i)+" , ";
                    }
                    mMessageView.setText(str+"["+data.percent+"]");
                    mProgressbar.setProgress(data.percent);
                }else if (data.type==DataCallback.TYPE_DAILYDOWN_DO){
                    mMessageView.setText(data.comment);
                    mProgressbar.setProgress(data.percent);
                    btnDown.setText("Stop");
                }else {
                    if (data.type==DataCallback.TYPE_DAILYDOWN_FINISH){
                        btnDown.setText("Start");
                    }
                    str=data.comment;
                    lines.add("<div>"+str + "</div>");
                    if (lines.size() > 100) {
                        lines.remove(0);
                    }
                    str = "";
                    for (int i = 0; i < lines.size(); i++) {
                        str += lines.get(i);
                    }
                    //mWebView.loadData(str, "text/html", "utf8");
                    mEditText.setText(Html.fromHtml(str,FROM_HTML_MODE_LEGACY));


                }
                LogX.w(TAG, str);
            }
        }
    };
    private void startService(){
        Intent intent=new Intent(this.getApplicationContext(), DailyService.class);
        intent.putExtra("flag", 1);
        this.getApplicationContext().startService(intent);

    }
    private void stopService(){
        Intent intent=new Intent(this.getApplicationContext(), DailyService.class);
        this.getApplicationContext().stopService(intent);
    }

    //---
    public static boolean hasGrantExternalRW(Activity activity) {
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

    private int PERMISSIONS_CODE = 1;

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
                    Log.d(TAG, "READ_EXTERNAL_STORAGE--3-");
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                        LogX.w(TAG, "READ_EXTERNAL_STORAGE--4-");
                        init();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CODE);
                        LogX.w(TAG, "READ_EXTERNAL_STORAGE--5-");
                        //mWebView.loadData("Please set app right!", "text/html", "utf8");
                        mEditText.setText(Html.fromHtml("Please set app right!",FROM_HTML_MODE_LEGACY));
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        LogX.w(TAG, "onDestroy()-1");
        //mWebView.destroy();
        stopService();
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
        LogX.w(TAG, "onDestroy()-2");
    }

}