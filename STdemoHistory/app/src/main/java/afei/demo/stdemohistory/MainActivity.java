package afei.demo.stdemohistory;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import afei.demo.stdemohistory.Api.DbContentProvider;
import afei.demo.stdemohistory.Api.SqliteHelperContext;
import afei.demo.stdemohistory.Api.SqliteHelper;
import afei.demo.stdemohistory.Api.LogX;
import afei.demo.stdemohistory.Api.TBK;
import afei.demo.stdemohistory.Api.TST;


public class MainActivity extends Activity  {
    private static final String TAG = "STdownHistory:MainActivity";

    private WebView mWebView;
    private View mLoading;
    private Intent mIntent;
    private TextView mMessageView;
    private ProgressBar mProgressbar;
    private Button editFrom;
    private Button editTo;
    private Button btnDown;
    private String htmldata="";

    private int mYear=2018;
    private int mMonth=1;
    private int mDay=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMessageView = (TextView) findViewById(R.id.download_message);
        mProgressbar = (ProgressBar) findViewById(R.id.download_progress);
        mWebView= (WebView) findViewById(R.id.webView1);

        editFrom = (Button) findViewById(R.id.editfrom);
        editFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, onDateSetListenerFrom, mYear, mMonth, mDay).show();
            }
        });
        editTo = (Button) findViewById(R.id.editto);
        editTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, onDateSetListenerTo, mYear, mMonth, mDay).show();
            }
        });
        String days="2014-01-01";//String.format("%4d-%02d-%02d",mYear,mMonth,mDay);
        editFrom.setText(days);
        days="2018-12-31";
        editTo.setText(days);

        btnDown = (Button) findViewById(R.id.download_btn);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
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
            Log.w(TAG, "----Game Over!");
        }

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(false);

    }
    private DatePickerDialog.OnDateSetListener onDateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            editFrom.setText(String.format("%4d-%02d-%02d",year,monthOfYear,dayOfMonth));
        }
    };
    private DatePickerDialog.OnDateSetListener onDateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            editTo.setText(String.format("%4d-%02d-%02d",year,monthOfYear,dayOfMonth));
        }
    };
    private void init(){
        IntentFilter filter = new IntentFilter(HistDown.ACTION_UPDATE_UI);
        registerReceiver(broadcastReceiver, filter);

        //new HistDown().start(this.getApplicationContext(),editFrom.getText().toString(),editTo.getText().toString());
        //new ParseBK().start(this.getApplicationContext());
        new NowDown().start(this.getApplicationContext());
        //new Input2db().start(this.getApplicationContext());
    }

    private ArrayList strTimer = new ArrayList();
    private ArrayList lines = new ArrayList();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w(TAG, "Main Receiver:"+intent.getAction());
            if (intent.getAction().equals(HistDown.ACTION_UPDATE_UI)) {
                int type = intent.getIntExtra("type",0);
                String comment = intent.getStringExtra("comment");
                int percent = intent.getIntExtra("percent",0);
                //textView.setText(intent.getExtras().getString("data"));
                String str =  comment;
                if (type==2){
                    mMessageView.setText(comment);
                    mProgressbar.setProgress(percent);
                    btnDown.setText("Stop");
                }else {
                    if (type==1){
                        btnDown.setText("Start");
                    }
                    str=comment;
                    lines.add("<div>"+str + "</div>");
                    if (lines.size() > 100) {
                        lines.remove(0);
                    }
                    str = "";
                    for (int i = 0; i < lines.size(); i++) {
                        str += lines.get(i);
                    }
                    mWebView.loadData(str, "text/html", "utf8");

                }
                Log.w(TAG, str);
            }
        }
    };
    //---
    public static boolean hasGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
            Log.w(TAG, "hasGrantExternalRW--11-");
            return false;
        }
        Log.w(TAG, "hasGrantExternalRW--22-");
        return true;
    }

    private int PERMISSIONS_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.w(TAG, "onRequestPermissionsResult--1-");
        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                Log.w(TAG, "READ_EXTERNAL_STORAGE--2-");
                if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.d(TAG, "READ_EXTERNAL_STORAGE--3-");
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        //授权成功后的逻辑
                        Log.w(TAG, "READ_EXTERNAL_STORAGE--4-");
                        init();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_CODE);
                        Log.w(TAG, "READ_EXTERNAL_STORAGE--5-");
                        mWebView.loadData("Please set app right!", "text/html", "utf8");
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();

    }

}