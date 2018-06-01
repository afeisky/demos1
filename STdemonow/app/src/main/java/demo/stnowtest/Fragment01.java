package demo.stnowtest;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import demo.api.LogX;


public class Fragment01 extends android.support.v4.app.Fragment {
    private static final String TAG = "STdownnow:Fragment01";
    private View  mView;
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

    private boolean isInit=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment01_1, container, false);
        LogX.e(TAG,"fragmemt01  onCreateView()");
        init();
        return mView;
    }

    public void init(){
        mMessageView = (TextView) mView.findViewById(R.id.download_message);
        mProgressbar = (ProgressBar) mView.findViewById(R.id.download_progress);
        mWebView= (WebView)  mView.findViewById(R.id.webView1);

        editFrom = (Button) mView.findViewById(R.id.editfrom);
        editFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), onDateSetListenerFrom, mYear, mMonth, mDay).show();
            }
        });
        editTo = (Button) mView.findViewById(R.id.editto);
        editTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), onDateSetListenerTo, mYear, mMonth, mDay).show();
            }
        });
        String days="2014-01-01";//String.format("%4d-%02d-%02d",mYear,mMonth,mDay);
        editFrom.setText(days);
        days="2018-12-31";
        editTo.setText(days);

        btnDown = (Button) mView.findViewById(R.id.download_btn);
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        btnDown.setText("Start");
        LogX.d(TAG, "----Game Start!!!");
        //String action=getIntent().getStringExtra("action");
        //if (action!=null && action.length()>0){
        //    LogX.d(TAG, "----onCreate(): action "+action);
        //    htmldata+="----onCreate(): action"+action+"</br>";
        //}
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(false);
        start();
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
    private void start(){
        LogX.w(TAG, "start");
        //IntentFilter filter = new IntentFilter(BootupReceiver.ACTION_UPDATE_UI);
        //getActivity().registerReceiver(broadcastReceiver, filter);

        //new HistDown().start(this.getApplicationContext(),editFrom.getText().toString(),editTo.getText().toString());
        //new ParseBK().start(this.getApplicationContext());
        //new NowDown3().start(this.getApplicationContext());
        //new Input2db().start(this.getApplicationContext());
        //Intent intent =new Intent(BootupReceiver.ACTION_BOOT);
        //getActivity().getApplicationContext().sendBroadcast(intent);
        new DownLHB(getActivity().getApplicationContext()).start();
    }
    private void startTimerService(Context context, String tagAction){
        Intent intentService=new Intent(context, TimerService.class);
        intentService.putExtra("flag", tagAction);
        context.startService(intentService);
    }
    private ArrayList strTimer = new ArrayList();
    private ArrayList lines = new ArrayList();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                LogX.w(TAG, "Main Receiver:" + intent.getAction());
                if (intent.getAction().equals(BootupReceiver.ACTION_UPDATE_UI)) {
                    int type = intent.getIntExtra("type", 0);
                    String comment = intent.getStringExtra("comment");
                    int percent = intent.getIntExtra("percent", 0);
                    //textView.setText(intent.getExtras().getString("data"));
                    String str = comment;
                    if (type == 2) {
                        mMessageView.setText(comment);
                        mProgressbar.setProgress(percent);
                        btnDown.setText("Stop");
                    } else {
                        if (type == 1) {
                            btnDown.setText("Start");
                        }
                        str = comment;
                        lines.add("<div>" + str + "</div>");
                        if (lines.size() > 100) {
                            lines.remove(0);
                        }
                        str = "";
                        for (int i = 0; i < lines.size(); i++) {
                            str += lines.get(i);
                        }
                        mWebView.loadData(str, "text/html", "utf8");
                    }
                    LogX.w(TAG, str);
                }
            }catch (Exception e){
                LogX.e(TAG,""+e.getMessage());
            }
        }
    };
    //---
}
